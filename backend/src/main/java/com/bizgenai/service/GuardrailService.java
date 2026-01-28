package com.bizgenai.service;

import com.bizgenai.entity.Blueprint;
import com.bizgenai.entity.Category;
import com.bizgenai.guardrail.GuardrailEngine;
import com.bizgenai.guardrail.GuardrailResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GuardrailService {

    private static final Logger log = LoggerFactory.getLogger(GuardrailService.class);
    private final GuardrailEngine guardrailEngine;

    public GuardrailService(GuardrailEngine guardrailEngine) {
        this.guardrailEngine = guardrailEngine;
    }

    public GuardrailResult apply(List<String> variations, Blueprint blueprint, Category category) {
        log.debug("Applying guardrails to {} variations", variations.size());
        return guardrailEngine.apply(variations, blueprint, category);
    }
}
