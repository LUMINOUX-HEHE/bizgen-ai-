package com.bizgenai.repository;

import com.bizgenai.entity.Blueprint;
import com.bizgenai.exception.BlueprintParsingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Repository;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Repository
public class BlueprintRepository {

    private final ResourceLoader resourceLoader;
    private final ObjectMapper objectMapper;
    private final Map<String, Blueprint> blueprintCache = new ConcurrentHashMap<>();

    @Value("${bizgen.blueprints.path:classpath:blueprints/}")
    private String blueprintsBasePath;

    public BlueprintRepository(ResourceLoader resourceLoader, ObjectMapper objectMapper) {
        this.resourceLoader = resourceLoader;
        this.objectMapper = objectMapper;
    }

    public Blueprint loadBlueprint(String blueprintPath) {
        return blueprintCache.computeIfAbsent(blueprintPath, this::loadFromFile);
    }

    private Blueprint loadFromFile(String blueprintPath) {
        String fullPath = blueprintsBasePath + blueprintPath;
        Resource resource = resourceLoader.getResource(fullPath);

        if (!resource.exists()) {
            throw new BlueprintParsingException("Blueprint not found: " + blueprintPath);
        }

        try (InputStream inputStream = resource.getInputStream()) {
            return objectMapper.readValue(inputStream, Blueprint.class);
        } catch (IOException e) {
            throw new BlueprintParsingException("Failed to parse blueprint: " + blueprintPath, e);
        }
    }

    public void clearCache() {
        blueprintCache.clear();
    }

    public void evictFromCache(String blueprintPath) {
        blueprintCache.remove(blueprintPath);
    }
}
