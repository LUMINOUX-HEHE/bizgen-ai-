package com.bizgenai.guardrail;

import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GuardrailResult {

    @Builder.Default
    private List<String> processedVariations = new ArrayList<>();

    @Builder.Default
    private List<String> disclaimers = new ArrayList<>();

    @Builder.Default
    private List<Warning> warnings = new ArrayList<>();

    @Builder.Default
    private List<Placeholder> detectedPlaceholders = new ArrayList<>();

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class Warning {
        private WarningType type;
        private String message;
        private Severity severity;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class Placeholder {
        private String name;
        private String description;
        private int startIndex;
        private int endIndex;
    }

    public enum WarningType {
        MISSING_FIELD,
        REVIEW_REQUIRED,
        LEGAL_NOTICE,
        CONTENT_LENGTH,
        COMPLIANCE_CHECK
    }

    public enum Severity {
        INFO,
        WARNING,
        CRITICAL
    }
}
