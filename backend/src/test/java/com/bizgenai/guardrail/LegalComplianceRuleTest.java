package com.bizgenai.guardrail;

import com.bizgenai.entity.Blueprint;
import com.bizgenai.entity.Category;
import com.bizgenai.guardrail.rules.LegalComplianceRule;
import com.bizgenai.guardrail.rules.RuleResult;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class LegalComplianceRuleTest {

    private LegalComplianceRule rule;
    private Blueprint blueprint;
    private Category legalCategory;
    private Category marketingCategory;

    @BeforeEach
    void setUp() {
        rule = new LegalComplianceRule();
        blueprint = Blueprint.builder()
                .templateId("privacy-policy")
                .version("1.0.0")
                .build();
        legalCategory = Category.builder()
                .id("cat-legal")
                .name("legal")
                .displayName("Legal Documents")
                .build();
        marketingCategory = Category.builder()
                .id("cat-marketing")
                .name("marketing")
                .displayName("Marketing")
                .build();
    }

    @Test
    void shouldApplyOnlyToLegalCategory() {
        assertTrue(rule.appliesTo(legalCategory, blueprint));
        assertFalse(rule.appliesTo(marketingCategory, blueprint));
    }

    @Test
    void shouldWarnAboutMissingSections() {
        // Arrange - content missing required sections
        String content = "This is a privacy policy without proper sections.";

        // Act
        RuleResult result = rule.apply(content, blueprint);

        // Assert
        assertNotNull(result);
        assertTrue(result.getWarnings().size() > 0);
        assertTrue(result.getWarnings().stream()
                .anyMatch(w -> w.getType() == GuardrailResult.WarningType.REVIEW_REQUIRED));
    }

    @Test
    void shouldWarnAboutProblematicPhrases() {
        // Arrange
        String content = "This privacy policy is fully compliant with all regulations.";

        // Act
        RuleResult result = rule.apply(content, blueprint);

        // Assert
        assertNotNull(result);
        assertTrue(result.getWarnings().stream()
                .anyMatch(w -> w.getType() == GuardrailResult.WarningType.LEGAL_NOTICE));
    }

    @Test
    void shouldNotWarnForWellStructuredContent() {
        // Arrange
        String content = """
                Privacy Policy
                
                Contact us at privacy@company.com
                
                Data Collection: We collect the following information...
                
                User Rights: You have the right to...
                
                Changes: We may update this policy...
                """;

        // Act
        RuleResult result = rule.apply(content, blueprint);

        // Assert
        assertNotNull(result);
        // Should not have critical legal notice warnings
        assertFalse(result.getWarnings().stream()
                .anyMatch(w -> w.getType() == GuardrailResult.WarningType.LEGAL_NOTICE));
    }
}
