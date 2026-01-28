package com.bizgenai.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
public class FormField {

    private String id;
    private String name;
    private String label;
    private FieldType type;
    private String placeholder;
    private String helpText;
    private Boolean required;
    private FieldValidation validation;
    private List<SelectOption> options;
    private Object defaultValue;
    private Integer maxLength;
    private Integer rows;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class FieldValidation {
        private Integer minLength;
        private Integer maxLength;
        private String pattern;
        private Double min;
        private Double max;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class SelectOption {
        private String value;
        private String label;
    }

    public enum FieldType {
        text,
        textarea,
        select,
        multiselect,
        checkbox,
        radio,
        number,
        email,
        url
    }
}
