package com.bizgenai.service;

import com.bizgenai.entity.Blueprint;
import com.bizgenai.entity.Category;
import com.bizgenai.guardrail.GuardrailEngine;
import com.bizgenai.guardrail.GuardrailResult;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GuardrailServiceTest {

    @Mock
    private GuardrailEngine guardrailEngine;

    @InjectMocks
    private GuardrailService guardrailService;

    private Blueprint testBlueprint;
    private Category testCategory;

    @BeforeEach
    void setUp() {
        testBlueprint = Blueprint.builder()
                .templateId("test-blueprint")
                .version("1.0.0")
                .requiredDisclaimers(List.of())
                .build();

        testCategory = Category.builder()
                .id("cat-1")
                .name("marketing")
                .displayName("Marketing")
                .requiresDisclaimer(false)
                .build();
    }

    @Test
    void shouldApplyGuardrails() {
        // Arrange
        List<String> variations = List.of(
                "Content variation 1",
                "Content variation 2"
        );

        GuardrailResult expectedResult = GuardrailResult.builder()
                .processedVariations(variations)
                .disclaimers(List.of())
                .warnings(List.of())
                .build();

        when(guardrailEngine.apply(any(), any(), any())).thenReturn(expectedResult);

        // Act
        GuardrailResult result = guardrailService.apply(variations, testBlueprint, testCategory);

        // Assert
        assertNotNull(result);
        assertEquals(2, result.getProcessedVariations().size());
        verify(guardrailEngine).apply(variations, testBlueprint, testCategory);
    }
}
