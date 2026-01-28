package com.bizgenai.ai;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AiRequest {

    private String systemPrompt;
    private String userPrompt;
    private Integer variationCount;
    private Integer maxTokens;
    private Double temperature;

    @Builder.Default
    private Integer timeoutSeconds = 30;
}
