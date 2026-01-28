package com.bizgenai.service;

import com.bizgenai.entity.Blueprint;
import com.bizgenai.repository.BlueprintRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class BlueprintService {

    private static final Logger log = LoggerFactory.getLogger(BlueprintService.class);
    private final BlueprintRepository blueprintRepository;

    public BlueprintService(BlueprintRepository blueprintRepository) {
        this.blueprintRepository = blueprintRepository;
    }

    public Blueprint loadBlueprint(String blueprintPath) {
        log.debug("Loading blueprint: {}", blueprintPath);
        return blueprintRepository.loadBlueprint(blueprintPath);
    }

    public void clearCache() {
        log.info("Clearing blueprint cache");
        blueprintRepository.clearCache();
    }

    public void evictFromCache(String blueprintPath) {
        log.debug("Evicting blueprint from cache: {}", blueprintPath);
        blueprintRepository.evictFromCache(blueprintPath);
    }
}
