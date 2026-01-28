package com.bizgenai.ai;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

/**
 * Fallback AI Service that wraps the real AI service and falls back to
 * MockAiService
 * when the real API fails due to rate limits, payment issues, or other errors.
 */
@Service
@Primary
public class FallbackAiService implements AiService {

    private static final Logger log = LoggerFactory.getLogger(FallbackAiService.class);

    private final AiService primaryAiService;
    private final MockAiService mockAiService;
    private final boolean enableFallback;
    private final int maxRetries;

    public FallbackAiService(
            @Qualifier("aiServiceAdapter") AiService primaryAiService,
            MockAiService mockAiService,
            @Value("${bizgen.ai.enable-fallback:true}") boolean enableFallback,
            @Value("${bizgen.ai.max-retries:3}") int maxRetries) {
        this.primaryAiService = primaryAiService;
        this.mockAiService = mockAiService;
        this.enableFallback = enableFallback;
        this.maxRetries = maxRetries;
    }

    @Override
    public AiResponse generate(AiRequest request) {
        log.info("FallbackAiService - attempting generation with primary service");

        // Try primary service with retries
        for (int attempt = 1; attempt <= maxRetries; attempt++) {
            try {
                AiResponse response = primaryAiService.generate(request);

                if (response.isSuccess()) {
                    log.info("Primary AI service succeeded on attempt {}", attempt);
                    return response;
                }

                String errorMsg = response.getErrorMessage();
                log.warn("Primary AI service failed on attempt {}: {}", attempt, errorMsg);

                // Check if this is a non-retryable error
                if (isNonRetryableError(errorMsg)) {
                    log.warn("Non-retryable error detected, skipping retries");
                    break;
                }

                // Wait before retry (exponential backoff)
                if (attempt < maxRetries) {
                    long waitTime = calculateBackoff(attempt);
                    log.info("Waiting {}ms before retry...", waitTime);
                    try {
                        TimeUnit.MILLISECONDS.sleep(waitTime);
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                        break;
                    }
                }

            } catch (Exception e) {
                log.error("Exception during AI generation attempt {}: {}", attempt, e.getMessage());
                if (attempt < maxRetries) {
                    long waitTime = calculateBackoff(attempt);
                    try {
                        TimeUnit.MILLISECONDS.sleep(waitTime);
                    } catch (InterruptedException ie) {
                        Thread.currentThread().interrupt();
                        break;
                    }
                }
            }
        }

        // Primary service failed, try fallback
        if (enableFallback) {
            log.warn("Primary AI service failed after {} attempts, falling back to MockAiService", maxRetries);
            return mockAiService.generate(request);
        } else {
            log.error("Primary AI service failed and fallback is disabled");
            return AiResponse.error("AI generation failed after " + maxRetries + " attempts. Fallback is disabled.");
        }
    }

    @Override
    public boolean isAvailable() {
        return primaryAiService.isAvailable() || mockAiService.isAvailable();
    }

    /**
     * Check if the error is non-retryable (e.g., payment required, invalid API key)
     */
    private boolean isNonRetryableError(String errorMsg) {
        if (errorMsg == null) {
            return false;
        }

        // Payment/credit issues
        if (errorMsg.contains("402") ||
                errorMsg.contains("spending limit") ||
                errorMsg.contains("insufficient credits") ||
                errorMsg.contains("payment required")) {
            return true;
        }

        // Authentication issues
        if (errorMsg.contains("401") ||
                errorMsg.contains("invalid api key") ||
                errorMsg.contains("unauthorized")) {
            return true;
        }

        // Bad request (won't be fixed by retrying)
        if (errorMsg.contains("400") &&
                (errorMsg.contains("invalid request") || errorMsg.contains("bad request"))) {
            return true;
        }

        return false;
    }

    /**
     * Calculate exponential backoff time in milliseconds
     */
    private long calculateBackoff(int attempt) {
        // Base delay of 1 second, doubling each time
        return 1000L * (1L << (attempt - 1));
    }
}
