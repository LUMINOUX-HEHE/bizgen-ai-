package com.bizgenai.dto.response;

import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GenerationResponse {

    private String generationId;
    private String templateId;
    private String templateName;
    private String category;
    private List<VariationResponse> variations;
    private List<String> disclaimers;
    private List<String> warnings;
    private Long generationTimeMs;
    private LocalDateTime createdAt;
}
