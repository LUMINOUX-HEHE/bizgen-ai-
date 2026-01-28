package com.bizgenai.dto.mapper;

import com.bizgenai.dto.response.TemplateResponse;
import com.bizgenai.entity.Category;
import com.bizgenai.entity.Template;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-01-28T20:15:14+0530",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 17.0.12 (Oracle Corporation)"
)
@Component
public class TemplateMapperImpl implements TemplateMapper {

    @Override
    public TemplateResponse toResponse(Template template) {
        if ( template == null ) {
            return null;
        }

        TemplateResponse.TemplateResponseBuilder templateResponse = TemplateResponse.builder();

        templateResponse.categoryId( templateCategoryId( template ) );
        templateResponse.categoryName( templateCategoryDisplayName( template ) );
        templateResponse.id( template.getId() );
        templateResponse.name( template.getName() );
        templateResponse.description( template.getDescription() );
        templateResponse.estimatedTime( template.getEstimatedTime() );
        templateResponse.difficulty( template.getDifficulty() );
        templateResponse.popular( template.getPopular() );

        return templateResponse.build();
    }

    @Override
    public List<TemplateResponse> toResponseList(List<Template> templates) {
        if ( templates == null ) {
            return null;
        }

        List<TemplateResponse> list = new ArrayList<TemplateResponse>( templates.size() );
        for ( Template template : templates ) {
            list.add( toResponse( template ) );
        }

        return list;
    }

    private String templateCategoryId(Template template) {
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

    private String templateCategoryDisplayName(Template template) {
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
}
