package com.bizgenai.dto.request;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GenerationOptionsRequest {

    private String tone;
    private String length;
    private Integer variationCount;
    private Boolean includeHashtags;
    private Boolean includeEmoji;
}
