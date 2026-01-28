package com.bizgenai.service;

import com.bizgenai.dto.mapper.TemplateMapper;
import com.bizgenai.dto.response.TemplateResponse;
import com.bizgenai.dto.response.TemplateSchemaResponse;
import com.bizgenai.entity.Blueprint;
import com.bizgenai.entity.Template;
import com.bizgenai.exception.ResourceNotFoundException;
import com.bizgenai.repository.BlueprintRepository;
import com.bizgenai.repository.TemplateRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class TemplateService {

    private static final Logger log = LoggerFactory.getLogger(TemplateService.class);
    private final TemplateRepository templateRepository;
    private final BlueprintRepository blueprintRepository;
    private final TemplateMapper templateMapper;

    public TemplateService(TemplateRepository templateRepository,
                           BlueprintRepository blueprintRepository,
                           TemplateMapper templateMapper) {
        this.templateRepository = templateRepository;
        this.blueprintRepository = blueprintRepository;
        this.templateMapper = templateMapper;
    }

    public List<TemplateResponse> getAllTemplates(String categoryId, Boolean active) {
        log.debug("Fetching templates - categoryId: {}, active: {}", categoryId, active);

        List<Template> templates;
        if (categoryId != null) {
            templates = templateRepository.findByCategoryIdWithCategory(categoryId);
        } else {
            templates = templateRepository.findAllActiveWithCategory();
        }

        return templateMapper.toResponseList(templates);
    }

    public TemplateResponse getTemplateById(String id) {
        log.debug("Fetching template by id: {}", id);
        Template template = templateRepository.findByIdWithCategory(id)
                .orElseThrow(() -> new ResourceNotFoundException("Template", "id", id));

        return templateMapper.toResponse(template);
    }

    public TemplateSchemaResponse getTemplateSchema(String id) {
        log.debug("Fetching template schema for id: {}", id);
        Template template = templateRepository.findByIdWithCategory(id)
                .orElseThrow(() -> new ResourceNotFoundException("Template", "id", id));

        Blueprint blueprint = blueprintRepository.loadBlueprint(template.getBlueprintPath());

        return templateMapper.toSchemaResponse(template, blueprint);
    }

    public Template getActiveTemplate(String id) {
        return templateRepository.findByIdAndActiveTrue(id)
                .orElseThrow(() -> new ResourceNotFoundException("Template", "id", id));
    }

    public Template getTemplateWithCategory(String id) {
        return templateRepository.findByIdWithCategory(id)
                .orElseThrow(() -> new ResourceNotFoundException("Template", "id", id));
    }

    public List<TemplateResponse> getPopularTemplates() {
        List<Template> templates = templateRepository.findByPopularTrueAndActiveTrueOrderByDisplayOrderAsc();
        return templateMapper.toResponseList(templates);
    }
}
