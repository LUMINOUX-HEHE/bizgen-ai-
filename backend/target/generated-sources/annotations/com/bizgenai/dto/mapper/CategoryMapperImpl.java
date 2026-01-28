package com.bizgenai.dto.mapper;

import com.bizgenai.dto.response.CategoryResponse;
import com.bizgenai.entity.Category;
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
public class CategoryMapperImpl implements CategoryMapper {

    @Override
    public CategoryResponse toResponse(Category category) {
        if ( category == null ) {
            return null;
        }

        CategoryResponse.CategoryResponseBuilder categoryResponse = CategoryResponse.builder();

        categoryResponse.id( category.getId() );
        categoryResponse.name( category.getName() );
        categoryResponse.displayName( category.getDisplayName() );
        categoryResponse.description( category.getDescription() );
        categoryResponse.icon( category.getIcon() );
        categoryResponse.requiresDisclaimer( category.getRequiresDisclaimer() );

        categoryResponse.templateCount( category.getTemplates() != null ? (int) category.getTemplates().stream().filter(t -> t.getActive()).count() : 0 );

        return categoryResponse.build();
    }

    @Override
    public List<CategoryResponse> toResponseList(List<Category> categories) {
        if ( categories == null ) {
            return null;
        }

        List<CategoryResponse> list = new ArrayList<CategoryResponse>( categories.size() );
        for ( Category category : categories ) {
            list.add( toResponse( category ) );
        }

        return list;
    }
}
