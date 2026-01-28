package com.bizgenai.guardrail.rules;

import com.bizgenai.guardrail.GuardrailResult;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RuleResult {

    private String modifiedContent;

    @Builder.Default
    private List<GuardrailResult.Warning> warnings = new ArrayList<>();

    @Builder.Default
    private List<GuardrailResult.Placeholder> placeholders = new ArrayList<>();

    public static RuleResult unchanged(String content) {
        return RuleResult.builder()
                .modifiedContent(content)
                .build();
    }

    public static RuleResult modified(String content, List<GuardrailResult.Warning> warnings) {
        return RuleResult.builder()
                .modifiedContent(content)
                .warnings(warnings)
                .build();
    }
}
