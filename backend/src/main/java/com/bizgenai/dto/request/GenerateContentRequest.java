package com.bizgenai.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GenerateContentRequest {

    @NotBlank(message = "Template ID is required")
    private String templateId;

    @NotNull(message = "Inputs are required")
    private Map<String, Object> inputs;

    private GenerationOptionsRequest options;
}
