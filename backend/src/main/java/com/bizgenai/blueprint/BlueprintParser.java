package com.bizgenai.blueprint;

import com.bizgenai.entity.Blueprint;
import com.bizgenai.exception.BlueprintParsingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;

@Component
public class BlueprintParser {

    private static final Logger log = LoggerFactory.getLogger(BlueprintParser.class);
    private final ObjectMapper objectMapper;
    private final BlueprintValidator validator;

    public BlueprintParser(ObjectMapper objectMapper, BlueprintValidator validator) {
        this.objectMapper = objectMapper;
        this.validator = validator;
    }

    public Blueprint parse(InputStream inputStream, String blueprintPath) {
        try {
            Blueprint blueprint = objectMapper.readValue(inputStream, Blueprint.class);
            log.debug("Parsed blueprint: {}", blueprint.getTemplateId());

            // Validate the parsed blueprint
            validator.validate(blueprint);

            return blueprint;
        } catch (IOException e) {
            log.error("Failed to parse blueprint: {}", blueprintPath, e);
            throw new BlueprintParsingException("Failed to parse blueprint: " + blueprintPath, e);
        }
    }

    public Blueprint parseFromString(String json) {
        try {
            Blueprint blueprint = objectMapper.readValue(json, Blueprint.class);
            validator.validate(blueprint);
            return blueprint;
        } catch (IOException e) {
            throw new BlueprintParsingException("Failed to parse blueprint from string", e);
        }
    }
}
