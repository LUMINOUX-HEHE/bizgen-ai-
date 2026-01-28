package com.bizgenai.guardrail;

import com.bizgenai.entity.Blueprint;
import com.bizgenai.entity.Category;
import com.bizgenai.guardrail.rules.PlaceholderDetectionRule;
import com.bizgenai.guardrail.rules.RuleResult;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class PlaceholderDetectionRuleTest {

    private PlaceholderDetectionRule rule;
    private Blueprint blueprint;
    private Category category;

    @BeforeEach
    void setUp() {
        rule = new PlaceholderDetectionRule();
        blueprint = Blueprint.builder()
                .templateId("test")
                .version("1.0.0")
                .build();
        category = Category.builder()
                .id("cat-1")
                .name("marketing")
                .build();
    }

    @Test
    void shouldDetectPlaceholders() {
        // Arrange
        String content = "Welcome to [COMPANY_NAME]. Contact us at [EMAIL].";

        // Act
        RuleResult result = rule.apply(content, blueprint);

        // Assert
        assertNotNull(result);
        assertEquals(2, result.getWarnings().size());
        assertEquals(2, result.getPlaceholders().size());
        assertTrue(result.getWarnings().stream()
                .allMatch(w -> w.getType() == GuardrailResult.WarningType.MISSING_FIELD));
    }

    @Test
    void shouldNotDetectPlaceholdersWhenNoneExist() {
        // Arrange
        String content = "Welcome to Acme Corp. Contact us at hello@acme.com.";

        // Act
        RuleResult result = rule.apply(content, blueprint);

        // Assert
        assertNotNull(result);
        assertTrue(result.getWarnings().isEmpty());
        assertTrue(result.getPlaceholders().isEmpty());
    }

    @Test
    void shouldApplyToAllCategories() {
        // Act & Assert
        assertTrue(rule.appliesTo(category, blueprint));
        assertTrue(rule.appliesTo(null, blueprint));
    }

    @Test
    void shouldDetectMultiplePlaceholdersWithUnderscores() {
        // Arrange
        String content = "[COMPANY_LEGAL_NAME] - [STREET_ADDRESS] - [CITY_STATE_ZIP]";

        // Act
        RuleResult result = rule.apply(content, blueprint);

        // Assert
        assertEquals(3, result.getPlaceholders().size());
    }
}
