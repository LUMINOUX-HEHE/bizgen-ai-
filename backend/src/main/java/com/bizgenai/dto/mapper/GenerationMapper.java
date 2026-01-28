package com.bizgenai.dto.mapper;

import com.bizgenai.dto.response.GenerationResponse;
import com.bizgenai.dto.response.HistoryItemResponse;
import com.bizgenai.dto.response.VariationResponse;
import com.bizgenai.entity.Generation;
import com.bizgenai.entity.GenerationVariation;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.ArrayList;
import java.util.List;

@Mapper(componentModel = "spring")
public interface GenerationMapper {

    @Mapping(target = "generationId", source = "id")
    @Mapping(target = "templateId", source = "template.id")
    @Mapping(target = "templateName", source = "template.name")
    @Mapping(target = "category", source = "template.category.displayName")
    @Mapping(target = "variations", source = "variations")
    @Mapping(target = "disclaimers", source = "disclaimers", qualifiedByName = "jsonToStringList")
    @Mapping(target = "warnings", source = "warnings", qualifiedByName = "jsonToStringList")
    GenerationResponse toResponse(Generation generation);

    @Mapping(target = "placeholders", source = "placeholders", qualifiedByName = "jsonToStringList")
    VariationResponse toVariationResponse(GenerationVariation variation);

    List<VariationResponse> toVariationResponseList(List<GenerationVariation> variations);

    @Mapping(target = "id", source = "id")
    @Mapping(target = "templateId", source = "template.id")
    @Mapping(target = "templateName", source = "template.name")
    @Mapping(target = "categoryId", source = "template.category.id")
    @Mapping(target = "categoryName", source = "template.category.displayName")
    @Mapping(target = "status", source = "status")
    @Mapping(target = "variationCount", expression = "java(generation.getVariations() != null ? generation.getVariations().size() : 0)")
    HistoryItemResponse toHistoryItemResponse(Generation generation);

    List<HistoryItemResponse> toHistoryItemResponseList(List<Generation> generations);

    @Named("jsonToStringList")
    default List<String> jsonToStringList(String json) {
        if (json == null || json.isEmpty()) {
            return new ArrayList<>();
        }
        try {
            ObjectMapper mapper = new ObjectMapper();
            return mapper.readValue(json, new TypeReference<List<String>>() {});
        } catch (Exception e) {
            return new ArrayList<>();
        }
    }
}
