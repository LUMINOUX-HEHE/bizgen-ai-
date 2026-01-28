package com.bizgenai.dto.response;

import com.bizgenai.entity.FormSection;
import lombok.*;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TemplateSchemaResponse {

    private String templateId;
    private String templateName;
    private String description;
    private String category;
    private String estimatedTime;
    private List<FormSection> sections;
}
