package com.bizgenai.service;

import com.bizgenai.entity.FormField;
import com.bizgenai.entity.FormSection;
import com.bizgenai.exception.ValidationException;
import com.bizgenai.validation.ValidationResult;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

@Service
public class ValidationService {

    public ValidationResult validateInputs(Map<String, Object> inputs, List<FormSection> sections) {
        List<ValidationException.FieldError> errors = new ArrayList<>();

        for (FormSection section : sections) {
            if (section.getFields() == null) continue;

            for (FormField field : section.getFields()) {
                Object value = inputs.get(field.getName());

                // Check required fields
                if (Boolean.TRUE.equals(field.getRequired())) {
                    if (value == null || (value instanceof String && ((String) value).isBlank())) {
                        errors.add(new ValidationException.FieldError(
                                field.getName(),
                                field.getLabel() + " is required"
                        ));
                        continue;
                    }
                }

                // Skip validation if value is null or empty
                if (value == null) continue;

                // Validate based on field type and validation rules
                if (field.getValidation() != null) {
                    validateField(field, value, errors);
                }
            }
        }

        return new ValidationResult(errors.isEmpty(), errors);
    }

    private void validateField(FormField field, Object value, List<ValidationException.FieldError> errors) {
        FormField.FieldValidation validation = field.getValidation();
        String stringValue = value.toString();

        // Min length validation
        if (validation.getMinLength() != null && stringValue.length() < validation.getMinLength()) {
            errors.add(new ValidationException.FieldError(
                    field.getName(),
                    field.getLabel() + " must be at least " + validation.getMinLength() + " characters",
                    value
            ));
        }

        // Max length validation
        if (validation.getMaxLength() != null && stringValue.length() > validation.getMaxLength()) {
            errors.add(new ValidationException.FieldError(
                    field.getName(),
                    field.getLabel() + " must not exceed " + validation.getMaxLength() + " characters",
                    value
            ));
        }

        // Pattern validation
        if (validation.getPattern() != null) {
            try {
                Pattern pattern = Pattern.compile(validation.getPattern());
                if (!pattern.matcher(stringValue).matches()) {
                    errors.add(new ValidationException.FieldError(
                            field.getName(),
                            field.getLabel() + " has an invalid format",
                            value
                    ));
                }
            } catch (Exception e) {
                // Skip invalid regex patterns
            }
        }

        // Numeric validations
        if (value instanceof Number) {
            double numValue = ((Number) value).doubleValue();
            if (validation.getMin() != null && numValue < validation.getMin()) {
                errors.add(new ValidationException.FieldError(
                        field.getName(),
                        field.getLabel() + " must be at least " + validation.getMin(),
                        value
                ));
            }
            if (validation.getMax() != null && numValue > validation.getMax()) {
                errors.add(new ValidationException.FieldError(
                        field.getName(),
                        field.getLabel() + " must not exceed " + validation.getMax(),
                        value
                ));
            }
        }
    }
}
