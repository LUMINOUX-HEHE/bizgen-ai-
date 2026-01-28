package com.bizgenai.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
public class FormSection {

    private String id;
    private String title;
    private String description;
    private Integer order;
    private List<FormField> fields;
}
