package com.bizgenai.service;

import com.bizgenai.ai.AiRequest;
import com.bizgenai.ai.AiResponse;
import com.bizgenai.ai.AiService;
import com.bizgenai.dto.mapper.GenerationMapper;
import com.bizgenai.dto.request.GenerateContentRequest;
import com.bizgenai.dto.response.GenerationResponse;
import com.bizgenai.entity.*;
import com.bizgenai.exception.ValidationException;
import com.bizgenai.guardrail.GuardrailResult;
import com.bizgenai.repository.GenerationRepository;
import com.bizgenai.validation.ValidationResult;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GenerationServiceTest {

    @Mock
    private TemplateService templateService;

    @Mock
    private BlueprintService blueprintService;

    @Mock
    private ValidationService validationService;

    @Mock
    private PromptAssemblyService promptAssemblyService;

    @Mock
    private AiService aiService;

    @Mock
    private GuardrailService guardrailService;

    @Mock
    private GenerationRepository generationRepository;

    @Mock
    private GenerationMapper generationMapper;

    @Mock
    private ObjectMapper objectMapper;

    @InjectMocks
    private GenerationService generationService;

    private Template testTemplate;
    private Category testCategory;
    private Blueprint testBlueprint;

    @BeforeEach
    void setUp() {
        testCategory = Category.builder()
                .id("cat-1")
                .name("marketing")
                .displayName("Marketing")
                .build();

        testTemplate = Template.builder()
                .id("tpl-1")
                .name("Instagram Post")
                .category(testCategory)
                .blueprintPath("marketing/instagram-post.json")
                .build();

        testBlueprint = Blueprint.builder()
                .templateId("instagram-post")
                .version("1.0.0")
                .sections(List.of())
                .outputFormat(Blueprint.OutputFormat.builder()
                        .variationCount(3)
                        .build())
                .build();
    }

    @Test
    void shouldGenerateContentSuccessfully() {
        // Arrange
        GenerateContentRequest request = GenerateContentRequest.builder()
                .templateId("tpl-1")
                .inputs(Map.of(
                        "productName", "Summer Collection",
                        "keyBenefits", "Sustainable and affordable"
                ))
                .build();

        when(templateService.getTemplateWithCategory("tpl-1")).thenReturn(testTemplate);
        when(blueprintService.loadBlueprint(any())).thenReturn(testBlueprint);
        when(validationService.validateInputs(any(), any())).thenReturn(ValidationResult.success());
        when(promptAssemblyService.assemble(any(), any(), any())).thenReturn(
                AiRequest.builder()
                        .systemPrompt("System prompt")
                        .userPrompt("User prompt")
                        .variationCount(3)
                        .build()
        );
        when(aiService.generate(any())).thenReturn(
                AiResponse.success(
                        List.of("Variation 1", "Variation 2", "Variation 3"),
                        100,
                        1500L
                )
        );
        when(guardrailService.apply(any(), any(), any())).thenReturn(
                GuardrailResult.builder()
                        .processedVariations(List.of("Variation 1", "Variation 2", "Variation 3"))
                        .disclaimers(List.of())
                        .warnings(List.of())
                        .build()
        );

        Generation savedGeneration = Generation.builder()
                .id("gen-1")
                .template(testTemplate)
                .status(GenerationStatus.COMPLETED)
                .build();
        when(generationRepository.save(any())).thenReturn(savedGeneration);

        GenerationResponse expectedResponse = GenerationResponse.builder()
                .generationId("gen-1")
                .templateId("tpl-1")
                .templateName("Instagram Post")
                .build();
        when(generationMapper.toResponse(any())).thenReturn(expectedResponse);

        // Act
        GenerationResponse result = generationService.generateContent(request);

        // Assert
        assertNotNull(result);
        assertEquals("gen-1", result.getGenerationId());
        verify(templateService).getTemplateWithCategory("tpl-1");
        verify(aiService).generate(any());
        verify(generationRepository).save(any());
    }

    @Test
    void shouldThrowValidationExceptionForInvalidInputs() {
        // Arrange
        GenerateContentRequest request = GenerateContentRequest.builder()
                .templateId("tpl-1")
                .inputs(Map.of())
                .build();

        when(templateService.getTemplateWithCategory("tpl-1")).thenReturn(testTemplate);
        when(blueprintService.loadBlueprint(any())).thenReturn(testBlueprint);
        when(validationService.validateInputs(any(), any())).thenReturn(
                ValidationResult.failure(List.of(
                        new ValidationException.FieldError("productName", "Product Name is required")
                ))
        );

        // Act & Assert
        assertThrows(ValidationException.class, () -> generationService.generateContent(request));
        verify(aiService, never()).generate(any());
    }
}
