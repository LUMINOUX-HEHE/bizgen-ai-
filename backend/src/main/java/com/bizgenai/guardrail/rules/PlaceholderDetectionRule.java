package com.bizgenai.guardrail.rules;

import com.bizgenai.entity.Blueprint;
import com.bizgenai.entity.Category;
import com.bizgenai.guardrail.GuardrailResult;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class PlaceholderDetectionRule implements GuardrailRule {

    private static final Pattern PLACEHOLDER_PATTERN = Pattern.compile("\\[([A-Z][A-Z_0-9]*)\\]");

    @Override
    public boolean appliesTo(Category category, Blueprint blueprint) {
        return true; // Applies to all content
    }

    @Override
    public RuleResult apply(String content, Blueprint blueprint) {
        List<GuardrailResult.Warning> warnings = new ArrayList<>();
        List<GuardrailResult.Placeholder> placeholders = new ArrayList<>();

        Matcher matcher = PLACEHOLDER_PATTERN.matcher(content);
        while (matcher.find()) {
            String placeholderName = matcher.group(1);

            placeholders.add(GuardrailResult.Placeholder.builder()
                    .name(placeholderName)
                    .description("This field needs to be filled in: " + formatPlaceholderName(placeholderName))
                    .startIndex(matcher.start())
                    .endIndex(matcher.end())
                    .build());

            warnings.add(GuardrailResult.Warning.builder()
                    .type(GuardrailResult.WarningType.MISSING_FIELD)
                    .message("Please fill in: " + formatPlaceholderName(placeholderName))
                    .severity(GuardrailResult.Severity.WARNING)
                    .build());
        }

        return RuleResult.builder()
                .modifiedContent(content)
                .warnings(warnings)
                .placeholders(placeholders)
                .build();
    }

    private String formatPlaceholderName(String name) {
        return name.replace("_", " ").toLowerCase();
    }

    @Override
    public int getOrder() {
        return 1;
    }
}
