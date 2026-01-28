package com.bizgenai.service;

import com.bizgenai.entity.DomainKnowledge;
import com.bizgenai.repository.DomainKnowledgeRepository;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

@Service
public class DomainKnowledgeService {

    private static final Logger log = LoggerFactory.getLogger(DomainKnowledgeService.class);
    private final DomainKnowledgeRepository repository;
    private final ResourceLoader resourceLoader;
    private final ObjectMapper objectMapper;

    @Value("${bizgen.domain-knowledge.path:classpath:domain-knowledge/}")
    private String domainKnowledgePath;

    public DomainKnowledgeService(DomainKnowledgeRepository repository,
                                   ResourceLoader resourceLoader,
                                   ObjectMapper objectMapper) {
        this.repository = repository;
        this.resourceLoader = resourceLoader;
        this.objectMapper = objectMapper;
    }

    @PostConstruct
    public void loadDomainKnowledgeFiles() {
        log.info("Loading domain knowledge files from: {}", domainKnowledgePath);
        try {
            PathMatchingResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
            Resource[] resources = resolver.getResources(domainKnowledgePath + "*.json");

            for (Resource resource : resources) {
                loadDomainKnowledgeFromResource(resource);
            }

            log.info("Loaded {} domain knowledge files", resources.length);
        } catch (IOException e) {
            log.warn("Failed to load domain knowledge files: {}", e.getMessage());
        }
    }

    private void loadDomainKnowledgeFromResource(Resource resource) {
        try (InputStream is = resource.getInputStream()) {
            JsonNode root = objectMapper.readTree(is);

            String id = root.has("id") ? root.get("id").asText() : resource.getFilename();

            if (repository.existsByReferenceId(id)) {
                log.debug("Domain knowledge already exists: {}", id);
                return;
            }

            DomainKnowledge dk = DomainKnowledge.builder()
                    .referenceId(id)
                    .name(id)
                    .version(root.has("version") ? root.get("version").asText() : "1.0.0")
                    .content(objectMapper.writeValueAsString(root))
                    .active(true)
                    .build();

            repository.save(dk);
            log.debug("Loaded domain knowledge: {}", id);
        } catch (IOException e) {
            log.error("Failed to load domain knowledge from: {}", resource.getFilename(), e);
        }
    }

    public List<DomainKnowledge> loadByReferences(List<String> references) {
        if (references == null || references.isEmpty()) {
            return new ArrayList<>();
        }
        return repository.findByReferenceIdIn(references);
    }

    public DomainKnowledge getByReferenceId(String referenceId) {
        return repository.findByReferenceId(referenceId).orElse(null);
    }
}
