package com.bizgenai.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class AiServiceConfig {

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

    @Value("${bizgen.ai.provider:mock}")
    private String aiProvider;

    @Value("${bizgen.ai.timeout-seconds:30}")
    private int timeoutSeconds;

    @Value("${bizgen.ai.max-retries:3}")
    private int maxRetries;

    @Value("${bizgen.ai.api-key}")
    private String apiKey;

    @Value("${bizgen.ai.model}")
    private String model;

    @Value("${bizgen.ai.base-url}")
    private String baseUrl;

    public String getAiProvider() {
        return aiProvider;
    }

    public String getApiKey() {
        return apiKey;
    }

    public String getModel() {
        return model;
    }

    public String getBaseUrl() {
        return baseUrl;
    }

    public int getTimeoutSeconds() {
        return timeoutSeconds;
    }

    public int getMaxRetries() {
        return maxRetries;
    }
}
