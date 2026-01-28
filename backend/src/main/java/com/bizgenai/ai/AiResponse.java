package com.bizgenai.ai;

import lombok.*;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AiResponse {

    private List<String> variations;
    private Integer totalTokens;
    private Long processingTimeMs;
    private boolean success;
    private String errorMessage;

    public static AiResponse error(String errorMessage) {
        return AiResponse.builder()
                .success(false)
                .errorMessage(errorMessage)
                .build();
    }

    public static AiResponse success(List<String> variations, Integer totalTokens, Long processingTimeMs) {
        return AiResponse.builder()
                .variations(variations)
                .totalTokens(totalTokens)
                .processingTimeMs(processingTimeMs)
                .success(true)
                .build();
    }
}
