package com.bizgenai.guardrail.rules;

import com.bizgenai.entity.Blueprint;
import com.bizgenai.entity.Category;
import com.bizgenai.guardrail.GuardrailResult;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Component
public class LegalComplianceRule implements GuardrailRule {

    private static final List<String> REQUIRED_SECTIONS = Arrays.asList(
            "contact", "data collection", "user rights", "changes"
    );

    private static final List<String> PROBLEMATIC_PHRASES = Arrays.asList(
            "fully compliant",
            "guarantees compliance",
            "100% legal",
            "certified legal",
            "legally binding"
    );

    @Override
    public boolean appliesTo(Category category, Blueprint blueprint) {
        return category != null && "legal".equalsIgnoreCase(category.getName());
    }

    @Override
    public RuleResult apply(String content, Blueprint blueprint) {
        List<GuardrailResult.Warning> warnings = new ArrayList<>();
        String lowerContent = content.toLowerCase();

        // Check for required sections
        for (String section : REQUIRED_SECTIONS) {
            if (!lowerContent.contains(section)) {
                warnings.add(GuardrailResult.Warning.builder()
                        .type(GuardrailResult.WarningType.REVIEW_REQUIRED)
                        .message("Document may be missing section: " + section)
                        .severity(GuardrailResult.Severity.WARNING)
                        .build());
            }
        }

        // Check for problematic phrases
        for (String phrase : PROBLEMATIC_PHRASES) {
            if (lowerContent.contains(phrase)) {
                warnings.add(GuardrailResult.Warning.builder()
                        .type(GuardrailResult.WarningType.LEGAL_NOTICE)
                        .message("Document contains phrase that should be reviewed: '" + phrase + "'")
                        .severity(GuardrailResult.Severity.CRITICAL)
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
        return 2;
    }
}
