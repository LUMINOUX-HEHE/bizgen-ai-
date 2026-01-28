package com.bizgenai.service;

import com.bizgenai.ai.AiRequest;
import com.bizgenai.blueprint.PromptBuilder;
import com.bizgenai.dto.request.GenerationOptionsRequest;
import com.bizgenai.entity.Blueprint;
import com.bizgenai.entity.DomainKnowledge;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class PromptAssemblyService {

    private static final Logger log = LoggerFactory.getLogger(PromptAssemblyService.class);
    private final PromptBuilder promptBuilder;
    private final DomainKnowledgeService domainKnowledgeService;

    public PromptAssemblyService(PromptBuilder promptBuilder, DomainKnowledgeService domainKnowledgeService) {
        this.promptBuilder = promptBuilder;
        this.domainKnowledgeService = domainKnowledgeService;
    }

    public AiRequest assemble(
            Blueprint blueprint,
            Map<String, Object> userInputs,
            GenerationOptionsRequest options
    ) {
        log.debug("Assembling prompt for blueprint: {}", blueprint.getTemplateId());

        // Load domain knowledge
        List<DomainKnowledge> domainKnowledge = List.of();
        if (blueprint.getDomainKnowledgeRefs() != null && !blueprint.getDomainKnowledgeRefs().isEmpty()) {
            domainKnowledge = domainKnowledgeService.loadByReferences(blueprint.getDomainKnowledgeRefs());
        }

        return promptBuilder.buildRequest(blueprint, userInputs, domainKnowledge, options);
    }

    public AssembledPrompt assembleWithDetails(
            Blueprint blueprint,
            Map<String, Object> userInputs,
            GenerationOptionsRequest options
    ) {
        AiRequest request = assemble(blueprint, userInputs, options);
        return new AssembledPrompt(request.getSystemPrompt(), request.getUserPrompt(), request);
    }

    public record AssembledPrompt(String systemPrompt, String userPrompt, AiRequest aiRequest) {
    }
}
