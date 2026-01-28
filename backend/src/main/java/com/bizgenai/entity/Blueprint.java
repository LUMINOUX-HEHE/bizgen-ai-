package com.bizgenai.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

import java.util.List;
import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
public class Blueprint {

    private String templateId;
    private String version;
    private MetaPrompt metaPrompt;
    private List<FormSection> sections;
    private List<ValidationRule> validationRules;
    private List<String> domainKnowledgeRefs;
    private OutputFormat outputFormat;
    private List<String> requiredDisclaimers;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class MetaPrompt {
        private String systemInstruction;
        private String roleDefinition;
        private String outputConstraints;
        private String styleGuidelines;
        private Map<String, String> variables;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class ValidationRule {
        private String type;
        private List<String> fields;
        private String message;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class OutputFormat {
        private String structure;
        private Integer variationCount;
        private Integer maxLength;
        private String tone;
        private Boolean includeEmoji;
        private List<String> sections;
    }
}
