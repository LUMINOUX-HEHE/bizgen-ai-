package com.bizgenai.blueprint;

import com.bizgenai.ai.AiRequest;
import com.bizgenai.dto.request.GenerationOptionsRequest;
import com.bizgenai.entity.Blueprint;
import com.bizgenai.entity.DomainKnowledge;
import com.bizgenai.entity.FormField;
import com.bizgenai.entity.FormSection;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
public class PromptBuilder {

    public AiRequest buildRequest(
            Blueprint blueprint,
            Map<String, Object> userInputs,
            List<DomainKnowledge> domainKnowledge,
            GenerationOptionsRequest options
    ) {
        String systemPrompt = buildSystemPrompt(blueprint, domainKnowledge);
        String userPrompt = buildUserPrompt(blueprint, userInputs, options);

        int variationCount = blueprint.getOutputFormat().getVariationCount();
        if (options != null && options.getVariationCount() != null) {
            variationCount = options.getVariationCount();
        }

        return AiRequest.builder()
                .systemPrompt(systemPrompt)
                .userPrompt(userPrompt)
                .variationCount(variationCount)
                .maxTokens(calculateMaxTokens(blueprint))
                .temperature(0.7)
                .build();
    }

    private String buildSystemPrompt(Blueprint blueprint, List<DomainKnowledge> domainKnowledge) {
        StringBuilder sb = new StringBuilder();
        Blueprint.MetaPrompt meta = blueprint.getMetaPrompt();

        // System instruction
        sb.append(meta.getSystemInstruction());
        sb.append("\n\n");

        // Role definition
        if (meta.getRoleDefinition() != null) {
            sb.append("Role: ").append(meta.getRoleDefinition());
            sb.append("\n\n");
        }

        // Output constraints
        if (meta.getOutputConstraints() != null) {
            sb.append("Constraints: ").append(meta.getOutputConstraints());
            sb.append("\n\n");
        }

        // Style guidelines
        if (meta.getStyleGuidelines() != null) {
            sb.append("Style: ").append(meta.getStyleGuidelines());
            sb.append("\n\n");
        }

        // Inject domain knowledge
        if (domainKnowledge != null && !domainKnowledge.isEmpty()) {
            sb.append("Reference Knowledge:\n");
            for (DomainKnowledge dk : domainKnowledge) {
                sb.append("\n--- ").append(dk.getName()).append(" ---\n");
                sb.append(dk.getContent());
                sb.append("\n");
            }
        }

        return sb.toString();
    }

    private String buildUserPrompt(Blueprint blueprint, Map<String, Object> userInputs, GenerationOptionsRequest options) {
        StringBuilder sb = new StringBuilder();

        sb.append("Generate content with the following details:\n\n");

        // Add user inputs with labels
        for (Map.Entry<String, Object> entry : userInputs.entrySet()) {
            String label = getLabelForField(blueprint, entry.getKey());
            sb.append("- ").append(label).append(": ");
            sb.append(formatValue(entry.getValue()));
            sb.append("\n");
        }

        // Add output format instructions
        sb.append("\nOutput Requirements:\n");
        Blueprint.OutputFormat outputFormat = blueprint.getOutputFormat();

        int variationCount = outputFormat.getVariationCount();
        if (options != null && options.getVariationCount() != null) {
            variationCount = options.getVariationCount();
        }

        sb.append("- Generate exactly ").append(variationCount).append(" variations\n");
        sb.append("- Each variation should be clearly separated with '--- Variation X ---'\n");

        if (outputFormat.getStructure() != null) {
            sb.append("- Follow the structure: ").append(outputFormat.getStructure()).append("\n");
        }

        if (outputFormat.getMaxLength() != null) {
            sb.append("- Maximum length per variation: ").append(outputFormat.getMaxLength()).append(" characters\n");
        }

        if (outputFormat.getIncludeEmoji() != null && outputFormat.getIncludeEmoji()) {
            sb.append("- Include relevant emoji where appropriate\n");
        }

        // Apply options overrides
        if (options != null) {
            if (options.getTone() != null) {
                sb.append("- Tone: ").append(options.getTone()).append("\n");
            }
            if (options.getLength() != null) {
                sb.append("- Length preference: ").append(options.getLength()).append("\n");
            }
            if (options.getIncludeHashtags() != null && options.getIncludeHashtags()) {
                sb.append("- Include relevant hashtags\n");
            }
        }

        return sb.toString();
    }

    private String getLabelForField(Blueprint blueprint, String fieldName) {
        for (FormSection section : blueprint.getSections()) {
            if (section.getFields() != null) {
                for (FormField field : section.getFields()) {
                    if (fieldName.equals(field.getName())) {
                        return field.getLabel() != null ? field.getLabel() : fieldName;
                    }
                }
            }
        }
        return fieldName;
    }

    private String formatValue(Object value) {
        if (value == null) {
            return "";
        }
        if (value instanceof List) {
            List<?> list = (List<?>) value;
            return String.join(", ", list.stream().map(Object::toString).toList());
        }
        return value.toString();
    }

    private Integer calculateMaxTokens(Blueprint blueprint) {
        Integer maxLength = blueprint.getOutputFormat().getMaxLength();
        int variationCount = blueprint.getOutputFormat().getVariationCount();

        if (maxLength != null) {
            // Rough estimate: 4 characters per token
            return (maxLength * variationCount) / 4 + 500;
        }
        return 2000;
    }
}
