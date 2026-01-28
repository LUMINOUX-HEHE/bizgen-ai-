package com.bizgenai.service;

import com.bizgenai.ai.AiRequest;
import com.bizgenai.ai.AiResponse;
import com.bizgenai.ai.AiService;
import com.bizgenai.dto.mapper.GenerationMapper;
import com.bizgenai.dto.request.GenerateContentRequest;
import com.bizgenai.dto.response.GenerationResponse;
import com.bizgenai.entity.*;
import com.bizgenai.exception.GenerationException;
import com.bizgenai.exception.ValidationException;
import com.bizgenai.guardrail.GuardrailResult;
import com.bizgenai.repository.GenerationRepository;
import com.bizgenai.validation.ValidationResult;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class GenerationService {

    private static final Logger log = LoggerFactory.getLogger(GenerationService.class);

    private final TemplateService templateService;
    private final BlueprintService blueprintService;
    private final ValidationService validationService;
    private final PromptAssemblyService promptAssemblyService;
    private final AiService aiService;
    private final GuardrailService guardrailService;
    private final GenerationRepository generationRepository;
    private final GenerationMapper generationMapper;
    private final ObjectMapper objectMapper;

    public GenerationService(
            TemplateService templateService,
            BlueprintService blueprintService,
            ValidationService validationService,
            PromptAssemblyService promptAssemblyService,
            AiService aiService,
            GuardrailService guardrailService,
            GenerationRepository generationRepository,
            GenerationMapper generationMapper,
            ObjectMapper objectMapper
    ) {
        this.templateService = templateService;
        this.blueprintService = blueprintService;
        this.validationService = validationService;
        this.promptAssemblyService = promptAssemblyService;
        this.aiService = aiService;
        this.guardrailService = guardrailService;
        this.generationRepository = generationRepository;
        this.generationMapper = generationMapper;
        this.objectMapper = objectMapper;
    }

    public GenerationResponse generateContent(GenerateContentRequest request) {
        log.info("Starting content generation for template: {}", request.getTemplateId());

        // 1. Validate template exists and is active
        Template template = templateService.getTemplateWithCategory(request.getTemplateId());
        log.debug("Template found: {}", template.getName());

        // 2. Load blueprint for template
        Blueprint blueprint = blueprintService.loadBlueprint(template.getBlueprintPath());
        log.debug("Blueprint loaded: {}", blueprint.getTemplateId());

        // 3. Validate user inputs against blueprint schema
        ValidationResult validation = validationService.validateInputs(
                request.getInputs(),
                blueprint.getSections()
        );
        if (!validation.isValid()) {
            throw new ValidationException(validation.getErrors());
        }
        log.debug("Input validation passed");

        // 4. Assemble the full prompt
        AiRequest aiRequest = promptAssemblyService.assemble(
                blueprint,
                request.getInputs(),
                request.getOptions()
        );
        log.debug("Prompt assembled");

        // 5. Call AI service
        long startTime = System.currentTimeMillis();
        AiResponse aiResponse = aiService.generate(aiRequest);
        long generationTime = System.currentTimeMillis() - startTime;

        if (!aiResponse.isSuccess()) {
            throw new GenerationException("AI generation failed: " + aiResponse.getErrorMessage());
        }
        log.debug("AI generation completed in {}ms", generationTime);

        // 6. Apply guardrails
        GuardrailResult guardrailResult = guardrailService.apply(
                aiResponse.getVariations(),
                blueprint,
                template.getCategory()
        );
        log.debug("Guardrails applied: {} warnings, {} disclaimers",
                guardrailResult.getWarnings().size(),
                guardrailResult.getDisclaimers().size());

        // 7. Persist generation
        Generation generation = createGenerationEntity(
                template,
                request,
                aiRequest,
                guardrailResult,
                generationTime
        );
        generation = generationRepository.save(generation);
        log.info("Generation saved with id: {}", generation.getId());

        // 8. Return response
        return generationMapper.toResponse(generation);
    }

    private Generation createGenerationEntity(
            Template template,
            GenerateContentRequest request,
            AiRequest aiRequest,
            GuardrailResult guardrailResult,
            long generationTime
    ) {
        Generation generation = Generation.builder()
                .template(template)
                .inputData(toJson(request.getInputs()))
                .assembledPrompt(aiRequest.getSystemPrompt() + "\n\n---\n\n" + aiRequest.getUserPrompt())
                .status(GenerationStatus.COMPLETED)
                .generationTimeMs(generationTime)
                .disclaimers(toJson(guardrailResult.getDisclaimers()))
                .warnings(toJson(guardrailResult.getWarnings().stream()
                        .map(w -> w.getMessage())
                        .collect(Collectors.toList())))
                .build();

        // Add variations
        List<String> variations = guardrailResult.getProcessedVariations();
        for (int i = 0; i < variations.size(); i++) {
            GenerationVariation variation = GenerationVariation.builder()
                    .variationNumber(i + 1)
                    .content(variations.get(i))
                    .placeholders(toJson(findPlaceholdersInVariation(guardrailResult, i)))
                    .build();
            generation.addVariation(variation);
        }

        return generation;
    }

    private List<String> findPlaceholdersInVariation(GuardrailResult result, int variationIndex) {
        return result.getDetectedPlaceholders().stream()
                .map(GuardrailResult.Placeholder::getName)
                .distinct()
                .collect(Collectors.toList());
    }

    private String toJson(Object obj) {
        try {
            return objectMapper.writeValueAsString(obj);
        } catch (JsonProcessingException e) {
            log.error("Failed to serialize to JSON", e);
            return "[]";
        }
    }

    @Transactional(readOnly = true)
    public GenerationResponse getGenerationById(String id) {
        Generation generation = generationRepository.findByIdWithTemplateAndCategory(id)
                .orElseThrow(() -> new GenerationException("Generation not found: " + id, "NOT_FOUND"));

        return generationMapper.toResponse(generation);
    }
}
