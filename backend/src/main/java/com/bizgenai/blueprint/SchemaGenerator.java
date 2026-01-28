package com.bizgenai.blueprint;

import com.bizgenai.dto.response.TemplateSchemaResponse;
import com.bizgenai.entity.Blueprint;
import com.bizgenai.entity.Template;
import org.springframework.stereotype.Component;

@Component
public class SchemaGenerator {

    public TemplateSchemaResponse generateSchema(Template template, Blueprint blueprint) {
        return TemplateSchemaResponse.builder()
                .templateId(template.getId())
                .templateName(template.getName())
                .description(template.getDescription())
                .category(template.getCategory().getDisplayName())
                .estimatedTime(template.getEstimatedTime())
                .sections(blueprint.getSections())
                .build();
    }
}
