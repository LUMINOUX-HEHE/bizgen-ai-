package com.bizgenai.guardrail.rules;

import com.bizgenai.entity.Blueprint;
import com.bizgenai.entity.Category;
import com.bizgenai.guardrail.GuardrailResult;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Component
public class ContentSafetyRule implements GuardrailRule {

    private static final List<String> INAPPROPRIATE_TERMS = Arrays.asList(
            "guaranteed results",
            "100% success",
            "risk-free investment",
            "get rich quick",
            "miracle cure"
    );

    @Override
    public boolean appliesTo(Category category, Blueprint blueprint) {
        return true; // Applies to all content
    }

    @Override
    public RuleResult apply(String content, Blueprint blueprint) {
        List<GuardrailResult.Warning> warnings = new ArrayList<>();
        String lowerContent = content.toLowerCase();

        for (String term : INAPPROPRIATE_TERMS) {
            if (lowerContent.contains(term.toLowerCase())) {
                warnings.add(GuardrailResult.Warning.builder()
                        .type(GuardrailResult.WarningType.COMPLIANCE_CHECK)
                        .message("Content contains potentially problematic phrase: '" + term + "'. Please review for compliance.")
                        .severity(GuardrailResult.Severity.WARNING)
                        .build());
            }
        }

        return RuleResult.builder()
                .modifiedContent(content)
                .warnings(warnings)
                .build();
    }

    @Override
    public int getOrder() {
        return 3;
    }
}
