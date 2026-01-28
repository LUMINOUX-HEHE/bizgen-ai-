package com.bizgenai.ai;

import com.bizgenai.config.AiServiceConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Profile;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Production AI Service Adapter.
 * This is a placeholder for the real AI integration.
 * Implement this class when the AI provider (OpenAI, Anthropic, etc.) is
 * chosen.
 */
@Service
@Profile("openrouter")
public class AiServiceAdapter implements AiService {

    private static final Logger log = LoggerFactory.getLogger(AiServiceAdapter.class);
    private final AiServiceConfig config;
    private final RestTemplate restTemplate;

    public AiServiceAdapter(AiServiceConfig config, RestTemplate restTemplate) {
        this.config = config;
        this.restTemplate = restTemplate;
    }

    @Override
    public AiResponse generate(AiRequest request) {
        log.info("OpenRouter AI Service - generating content using model: {}", config.getModel());

        // Check if API key is configured
        if (config.getApiKey() == null || config.getApiKey().isBlank()) {
            String errorMsg = "OpenRouter API key is not configured. Please set BIZGEN_AI_API_KEY environment variable or configure bizgen.ai.api-key in application.yml";
            log.error(errorMsg);
            return AiResponse.error(errorMsg);
        }

        try {
            long startTime = System.currentTimeMillis();

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.setBearerAuth(config.getApiKey());
            headers.set("HTTP-Referer", "http://localhost:8080"); // Required by some OpenRouter models
            headers.set("X-Title", "BizGen AI");

            Map<String, Object> body = new HashMap<>();
            body.put("model", config.getModel());

            List<Map<String, String>> messages = new ArrayList<>();
            if (request.getSystemPrompt() != null && !request.getSystemPrompt().isBlank()) {
                messages.add(Map.of("role", "system", "content", request.getSystemPrompt()));
            }
            messages.add(Map.of("role", "user", "content", request.getUserPrompt()));
            body.put("messages", messages);

            body.put("temperature", request.getTemperature() != null ? request.getTemperature() : 0.7);
            if (request.getMaxTokens() != null) {
                body.put("max_tokens", request.getMaxTokens());
            }
            // OpenRouter 'n' parameter might not be supported by all free models or might
            // return only 1 completion
            // For simplicity and reliability with free models, we'll request 1 and loop if
            // more are needed,
            // but usually 'n' works. Let's try 'n' first.
            body.put("n", request.getVariationCount() != null ? request.getVariationCount() : 1);

            HttpEntity<Map<String, Object>> entity = new HttpEntity<>(body, headers);
            String url = config.getBaseUrl() + "/chat/completions";

            ResponseEntity<Map> responseEntity = restTemplate.postForEntity(url, entity, Map.class);
            Map<String, Object> responseBody = responseEntity.getBody();

            if (responseBody == null || !responseEntity.getStatusCode().is2xxSuccessful()) {
                String errorMsg = buildErrorMessage(responseEntity.getStatusCode(), responseBody);
                log.error("OpenRouter API error: {}", responseBody);
                return AiResponse.error(errorMsg);
            }

            List<Map<String, Object>> choices = (List<Map<String, Object>>) responseBody.get("choices");
            if (choices == null || choices.isEmpty()) {
                String errorMsg = buildNoChoicesError(responseBody);
                log.error("OpenRouter returned no choices: {}", responseBody);
                return AiResponse.error(errorMsg);
            }

            List<String> variations = choices.stream()
                    .map(choice -> {
                        Map<String, Object> message = (Map<String, Object>) choice.get("message");
                        return message != null ? (String) message.get("content") : null;
                    })
                    .filter(content -> content != null)
                    .toList();

            if (variations.isEmpty()) {
                return AiResponse.error("OpenRouter returned choices but no content.");
            }

            Map<String, Object> usage = (Map<String, Object>) responseBody.get("usage");
            Integer totalTokens = usage != null ? (Integer) usage.get("total_tokens") : 0;
            long processingTime = System.currentTimeMillis() - startTime;

            return AiResponse.success(variations, totalTokens, processingTime);

        } catch (Exception e) {
            log.error("Error calling OpenRouter API", e);
            return AiResponse.error("AI Generation Error: " + e.getMessage());
        }
    }

    /**
     * Build a detailed error message from the HTTP response
     */
    private String buildErrorMessage(HttpStatusCode statusCode, Map<String, Object> responseBody) {
        StringBuilder errorMsg = new StringBuilder();
        errorMsg.append("OpenRouter API returned status ").append(statusCode.value()).append(" (")
                .append(statusCode.toString()).append(")");

        if (responseBody != null) {
            if (responseBody.containsKey("error")) {
                Object errorObj = responseBody.get("error");
                if (errorObj instanceof Map) {
                    Map<?, ?> errorMap = (Map<?, ?>) errorObj;
                    if (errorMap.containsKey("message")) {
                        errorMsg.append(": ").append(errorMap.get("message"));
                    }
                    if (errorMap.containsKey("code")) {
                        errorMsg.append(" [code: ").append(errorMap.get("code")).append("]");
                    }
                    if (errorMap.containsKey("type")) {
                        errorMsg.append(" [type: ").append(errorMap.get("type")).append("]");
                    }
                } else {
                    errorMsg.append(": ").append(errorObj);
                }
            }
            if (responseBody.containsKey("message")) {
                errorMsg.append(" - ").append(responseBody.get("message"));
            }
        }

        return errorMsg.toString();
    }

    /**
     * Build an error message when no choices are returned
     */
    private String buildNoChoicesError(Map<String, Object> responseBody) {
        StringBuilder errorMsg = new StringBuilder("OpenRouter returned no choices");

        if (responseBody.containsKey("error")) {
            Object errorObj = responseBody.get("error");
            if (errorObj instanceof Map) {
                Map<?, ?> errorMap = (Map<?, ?>) errorObj;
                errorMsg.append(". Error: ");
                if (errorMap.containsKey("message")) {
                    errorMsg.append(errorMap.get("message"));
                }
                if (errorMap.containsKey("code")) {
                    errorMsg.append(" [code: ").append(errorMap.get("code")).append("]");
                }
            } else {
                errorMsg.append(". Error: ").append(errorObj);
            }
        } else {
            errorMsg.append(". Full response: ").append(responseBody);
        }

        return errorMsg.toString();
    }

    @Override
    public boolean isAvailable() {
        return config.getApiKey() != null && !config.getApiKey().isBlank();
    }
}
