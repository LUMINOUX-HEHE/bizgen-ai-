package com.bizgenai.validation;

import com.bizgenai.entity.Blueprint;
import com.bizgenai.entity.FormField;
import com.bizgenai.entity.FormSection;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class SchemaValidator {

    private final InputValidator inputValidator;

    public SchemaValidator(InputValidator inputValidator) {
        this.inputValidator = inputValidator;
    }

    public ValidationResult validate(Map<String, Object> inputs, Blueprint blueprint) {
        ValidationResult result = ValidationResult.success();

        if (blueprint.getSections() == null) {
            return result;
        }

        for (FormSection section : blueprint.getSections()) {
            if (section.getFields() == null) continue;

            for (FormField field : section.getFields()) {
                Object value = inputs.get(field.getName());
                validateField(field, value, result);
            }
        }

        return result;
    }

    private void validateField(FormField field, Object value, ValidationResult result) {
        String fieldName = field.getName();
        String label = field.getLabel() != null ? field.getLabel() : fieldName;

        // Required validation
        if (Boolean.TRUE.equals(field.getRequired())) {
            if (value == null || (value instanceof String && ((String) value).isBlank())) {
                result.addError(fieldName, label + " is required");
                return;
            }
        }

        if (value == null) return;

        // Type-specific validation
        switch (field.getType()) {
            case email:
                if (!inputValidator.isValidEmail(value.toString())) {
                    result.addError(fieldName, label + " must be a valid email address", value);
                }
                break;
            case url:
                if (!inputValidator.isValidUrl(value.toString())) {
                    result.addError(fieldName, label + " must be a valid URL", value);
                }
                break;
            default:
                // Apply field validation rules if present
                if (field.getValidation() != null) {
                    validateFieldRules(field, value, result);
                }
        }
    }

    private void validateFieldRules(FormField field, Object value, ValidationResult result) {
        FormField.FieldValidation validation = field.getValidation();
        String fieldName = field.getName();
        String label = field.getLabel() != null ? field.getLabel() : fieldName;
        String stringValue = value.toString();

        if (validation.getMinLength() != null && stringValue.length() < validation.getMinLength()) {
            result.addError(fieldName,
                    label + " must be at least " + validation.getMinLength() + " characters", value);
        }

        if (validation.getMaxLength() != null && stringValue.length() > validation.getMaxLength()) {
            result.addError(fieldName,
                    label + " must not exceed " + validation.getMaxLength() + " characters", value);
        }

        if (validation.getPattern() != null && !inputValidator.matchesPattern(stringValue, validation.getPattern())) {
            result.addError(fieldName, label + " has an invalid format", value);
        }
    }
}
