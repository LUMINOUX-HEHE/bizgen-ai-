package com.bizgenai.dto.mapper;

import com.bizgenai.dto.response.CategoryResponse;
import com.bizgenai.entity.Category;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface CategoryMapper {

    @Mapping(target = "templateCount", expression = "java(category.getTemplates() != null ? (int) category.getTemplates().stream().filter(t -> t.getActive()).count() : 0)")
    CategoryResponse toResponse(Category category);

    List<CategoryResponse> toResponseList(List<Category> categories);

    default CategoryResponse toResponseWithCount(Category category, int templateCount) {
        return CategoryResponse.builder()
                .id(category.getId())
                .name(category.getName())
                .displayName(category.getDisplayName())
                .description(category.getDescription())
                .icon(category.getIcon())
                .templateCount(templateCount)
                .requiresDisclaimer(category.getRequiresDisclaimer())
                .build();
    }
}
