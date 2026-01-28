package com.bizgenai.validation;

import com.bizgenai.exception.ValidationException;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ValidationResult {

    private boolean valid;
    private List<ValidationException.FieldError> errors = new ArrayList<>();

    public static ValidationResult success() {
        return new ValidationResult(true, new ArrayList<>());
    }

    public static ValidationResult failure(List<ValidationException.FieldError> errors) {
        return new ValidationResult(false, errors);
    }

    public void addError(String field, String message) {
        this.errors.add(new ValidationException.FieldError(field, message));
        this.valid = false;
    }

    public void addError(String field, String message, Object rejectedValue) {
        this.errors.add(new ValidationException.FieldError(field, message, rejectedValue));
        this.valid = false;
    }
}
