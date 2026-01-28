package com.bizgenai.service;

import com.bizgenai.dto.mapper.CategoryMapper;
import com.bizgenai.dto.response.CategoryResponse;
import com.bizgenai.entity.Category;
import com.bizgenai.exception.ResourceNotFoundException;
import com.bizgenai.repository.CategoryRepository;
import com.bizgenai.repository.TemplateRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class CategoryService {

    private static final Logger log = LoggerFactory.getLogger(CategoryService.class);
    private final CategoryRepository categoryRepository;
    private final TemplateRepository templateRepository;
    private final CategoryMapper categoryMapper;

    public CategoryService(CategoryRepository categoryRepository,
                           TemplateRepository templateRepository,
                           CategoryMapper categoryMapper) {
        this.categoryRepository = categoryRepository;
        this.templateRepository = templateRepository;
        this.categoryMapper = categoryMapper;
    }

    public List<CategoryResponse> getAllActiveCategories() {
        log.debug("Fetching all active categories");
        List<Category> categories = categoryRepository.findByActiveTrueOrderByDisplayOrderAsc();

        return categories.stream()
                .map(category -> {
                    int templateCount = (int) templateRepository.countByCategoryId(category.getId());
                    return categoryMapper.toResponseWithCount(category, templateCount);
                })
                .collect(Collectors.toList());
    }

    public CategoryResponse getCategoryById(String id) {
        log.debug("Fetching category by id: {}", id);
        Category category = categoryRepository.findByIdAndActiveTrue(id)
                .orElseThrow(() -> new ResourceNotFoundException("Category", "id", id));

        int templateCount = (int) templateRepository.countByCategoryId(category.getId());
        return categoryMapper.toResponseWithCount(category, templateCount);
    }

    public Category getCategoryEntity(String id) {
        return categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Category", "id", id));
    }
}
