package com.bizgenai.blueprint;

import com.bizgenai.entity.Blueprint;
import com.bizgenai.entity.FormField;
import com.bizgenai.entity.FormSection;
import com.bizgenai.exception.BlueprintParsingException;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Component
public class BlueprintValidator {

    public void validate(Blueprint blueprint) {
        List<String> errors = new ArrayList<>();

        // Validate required fields
        if (blueprint.getTemplateId() == null || blueprint.getTemplateId().isBlank()) {
            errors.add("Blueprint must have a templateId");
        }

        if (blueprint.getVersion() == null || blueprint.getVersion().isBlank()) {
            errors.add("Blueprint must have a version");
        }

        if (blueprint.getMetaPrompt() == null) {
            errors.add("Blueprint must have a metaPrompt");
        } else {
            validateMetaPrompt(blueprint.getMetaPrompt(), errors);
        }

        if (blueprint.getSections() == null || blueprint.getSections().isEmpty()) {
            errors.add("Blueprint must have at least one section");
        } else {
            validateSections(blueprint.getSections(), errors);
        }

        if (blueprint.getOutputFormat() == null) {
            errors.add("Blueprint must have an outputFormat");
        }

        if (!errors.isEmpty()) {
            throw new BlueprintParsingException("Blueprint validation failed: " + String.join(", ", errors));
        }
    }

    private void validateMetaPrompt(Blueprint.MetaPrompt metaPrompt, List<String> errors) {
        if (metaPrompt.getSystemInstruction() == null || metaPrompt.getSystemInstruction().isBlank()) {
            errors.add("MetaPrompt must have a systemInstruction");
        }
    }

    private void validateSections(List<FormSection> sections, List<String> errors) {
        Set<String> sectionIds = new HashSet<>();
        Set<String> fieldNames = new HashSet<>();

        for (FormSection section : sections) {
            if (section.getId() == null || section.getId().isBlank()) {
                errors.add("Each section must have an id");
                continue;
            }

            if (sectionIds.contains(section.getId())) {
                errors.add("Duplicate section id: " + section.getId());
            }
            sectionIds.add(section.getId());

            if (section.getFields() != null) {
                for (FormField field : section.getFields()) {
                    if (field.getName() == null || field.getName().isBlank()) {
                        errors.add("Each field must have a name in section: " + section.getId());
                        continue;
                    }

                    if (fieldNames.contains(field.getName())) {
                        errors.add("Duplicate field name: " + field.getName());
                    }
                    fieldNames.add(field.getName());

                    if (field.getType() == null) {
                        errors.add("Field must have a type: " + field.getName());
                    }
                }
            }
        }
    }
}
