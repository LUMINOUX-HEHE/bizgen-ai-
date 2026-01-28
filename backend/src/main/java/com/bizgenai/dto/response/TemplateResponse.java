package com.bizgenai.dto.response;

import com.bizgenai.entity.DifficultyLevel;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TemplateResponse {

    private String id;
    private String name;
    private String description;
    private String categoryId;
    private String categoryName;
    private String estimatedTime;
    private DifficultyLevel difficulty;
    private Boolean popular;
}
