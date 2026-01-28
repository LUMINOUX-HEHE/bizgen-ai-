package com.bizgenai.dto.response;

import lombok.*;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class HistoryItemResponse {

    private String id;
    private String templateId;
    private String templateName;
    private String categoryId;
    private String categoryName;
    private String status;
    private Long generationTimeMs;
    private Integer variationCount;
    private LocalDateTime createdAt;
}
