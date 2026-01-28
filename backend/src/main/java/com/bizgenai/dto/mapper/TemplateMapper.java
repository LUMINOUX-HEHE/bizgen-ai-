package com.bizgenai.dto.mapper;

import com.bizgenai.dto.response.TemplateResponse;
import com.bizgenai.dto.response.TemplateSchemaResponse;
import com.bizgenai.entity.Blueprint;
import com.bizgenai.entity.Template;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface TemplateMapper {

    @Mapping(target = "categoryId", source = "category.id")
    @Mapping(target = "categoryName", source = "category.displayName")
    TemplateResponse toResponse(Template template);

    List<TemplateResponse> toResponseList(List<Template> templates);

    default TemplateSchemaResponse toSchemaResponse(Template template, Blueprint blueprint) {
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
