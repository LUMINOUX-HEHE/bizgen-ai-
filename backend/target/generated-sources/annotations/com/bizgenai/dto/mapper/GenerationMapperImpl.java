package com.bizgenai.dto.mapper;

import com.bizgenai.dto.response.GenerationResponse;
import com.bizgenai.dto.response.HistoryItemResponse;
import com.bizgenai.dto.response.VariationResponse;
import com.bizgenai.entity.Category;
import com.bizgenai.entity.Generation;
import com.bizgenai.entity.GenerationVariation;
import com.bizgenai.entity.Template;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-01-28T20:15:13+0530",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 17.0.12 (Oracle Corporation)"
)
@Component
public class GenerationMapperImpl implements GenerationMapper {

    @Override
    public GenerationResponse toResponse(Generation generation) {
        if ( generation == null ) {
            return null;
        }

        GenerationResponse.GenerationResponseBuilder generationResponse = GenerationResponse.builder();

        generationResponse.generationId( generation.getId() );
        generationResponse.templateId( generationTemplateId( generation ) );
        generationResponse.templateName( generationTemplateName( generation ) );
        generationResponse.category( generationTemplateCategoryDisplayName( generation ) );
        generationResponse.variations( toVariationResponseList( generation.getVariations() ) );
        generationResponse.disclaimers( jsonToStringList( generation.getDisclaimers() ) );
        generationResponse.warnings( jsonToStringList( generation.getWarnings() ) );
        generationResponse.generationTimeMs( generation.getGenerationTimeMs() );
        generationResponse.createdAt( generation.getCreatedAt() );

        return generationResponse.build();
    }

    @Override
    public VariationResponse toVariationResponse(GenerationVariation variation) {
        if ( variation == null ) {
            return null;
        }

        VariationResponse.VariationResponseBuilder variationResponse = VariationResponse.builder();

        variationResponse.placeholders( jsonToStringList( variation.getPlaceholders() ) );
        variationResponse.id( variation.getId() );
        variationResponse.variationNumber( variation.getVariationNumber() );
        variationResponse.content( variation.getContent() );

        return variationResponse.build();
    }

    @Override
    public List<VariationResponse> toVariationResponseList(List<GenerationVariation> variations) {
        if ( variations == null ) {
            return null;
        }

        List<VariationResponse> list = new ArrayList<VariationResponse>( variations.size() );
        for ( GenerationVariation generationVariation : variations ) {
            list.add( toVariationResponse( generationVariation ) );
        }

        return list;
    }

    @Override
    public HistoryItemResponse toHistoryItemResponse(Generation generation) {
        if ( generation == null ) {
            return null;
        }

        HistoryItemResponse.HistoryItemResponseBuilder historyItemResponse = HistoryItemResponse.builder();

        historyItemResponse.id( generation.getId() );
        historyItemResponse.templateId( generationTemplateId( generation ) );
        historyItemResponse.templateName( generationTemplateName( generation ) );
        historyItemResponse.categoryId( generationTemplateCategoryId( generation ) );
        historyItemResponse.categoryName( generationTemplateCategoryDisplayName( generation ) );
        if ( generation.getStatus() != null ) {
            historyItemResponse.status( generation.getStatus().name() );
        }
        historyItemResponse.generationTimeMs( generation.getGenerationTimeMs() );
        historyItemResponse.createdAt( generation.getCreatedAt() );

        historyItemResponse.variationCount( generation.getVariations() != null ? generation.getVariations().size() : 0 );

        return historyItemResponse.build();
    }

    @Override
    public List<HistoryItemResponse> toHistoryItemResponseList(List<Generation> generations) {
        if ( generations == null ) {
            return null;
        }

        List<HistoryItemResponse> list = new ArrayList<HistoryItemResponse>( generations.size() );
        for ( Generation generation : generations ) {
            list.add( toHistoryItemResponse( generation ) );
        }

        return list;
    }

    private String generationTemplateId(Generation generation) {
        if ( generation == null ) {
            return null;
        }
        Template template = generation.getTemplate();
        if ( template == null ) {
            return null;
        }
        String id = template.getId();
        if ( id == null ) {
            return null;
        }
        return id;
    }

    private String generationTemplateName(Generation generation) {
        if ( generation == null ) {
            return null;
        }
        Template template = generation.getTemplate();
        if ( template == null ) {
            return null;
        }
        String name = template.getName();
        if ( name == null ) {
            return null;
        }
        return name;
    }

    private String generationTemplateCategoryDisplayName(Generation generation) {
        if ( generation == null ) {
            return null;
        }
        Template template = generation.getTemplate();
        if ( template == null ) {
            return null;
        }
        Category category = template.getCategory();
        if ( category == null ) {
            return null;
        }
        String displayName = category.getDisplayName();
        if ( displayName == null ) {
            return null;
        }
        return displayName;
    }

    private String generationTemplateCategoryId(Generation generation) {
        if ( generation == null ) {
            return null;
        }
        Template template = generation.getTemplate();
        if ( template == null ) {
            return null;
        }
        Category category = template.getCategory();
        if ( category == null ) {
            return null;
        }
        String id = category.getId();
        if ( id == null ) {
            return null;
        }
        return id;
    }
}
