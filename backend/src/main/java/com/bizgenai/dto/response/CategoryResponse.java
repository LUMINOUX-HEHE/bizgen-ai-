package com.bizgenai.dto.response;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CategoryResponse {

    private String id;
    private String name;
    private String displayName;
    private String description;
    private String icon;
    private Integer templateCount;
    private Boolean requiresDisclaimer;
}
