package com.bizgenai.dto.response;

import lombok.*;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class VariationResponse {

    private String id;
    private Integer variationNumber;
    private String content;
    private List<String> placeholders;
}
