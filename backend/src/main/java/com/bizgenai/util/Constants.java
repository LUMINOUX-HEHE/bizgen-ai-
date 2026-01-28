package com.bizgenai.util;

public final class Constants {

    private Constants() {
        // Utility class
    }

    // API
    public static final String API_BASE_PATH = "/api/v1";
    public static final String API_VERSION = "1.0.0";

    // Defaults
    public static final int DEFAULT_VARIATION_COUNT = 3;
    public static final int MAX_VARIATION_COUNT = 5;
    public static final int DEFAULT_PAGE_SIZE = 20;
    public static final int MAX_PAGE_SIZE = 100;
    public static final int DEFAULT_TIMEOUT_SECONDS = 30;

    // Content limits
    public static final int MAX_CONTENT_LENGTH = 10000;
    public static final int MAX_INPUT_LENGTH = 5000;
    public static final int MAX_PROMPT_LENGTH = 50000;

    // Categories
    public static final String CATEGORY_MARKETING = "marketing";
    public static final String CATEGORY_LEGAL = "legal";

    // Disclaimer keys
    public static final String DISCLAIMER_DRAFT_ONLY = "DRAFT_ONLY";
    public static final String DISCLAIMER_LEGAL_REVIEW_REQUIRED = "LEGAL_REVIEW_REQUIRED";
    public static final String DISCLAIMER_NOT_LEGAL_ADVICE = "NOT_LEGAL_ADVICE";

    // Cache TTL
    public static final long BLUEPRINT_CACHE_TTL_MINUTES = 60;
    public static final long DOMAIN_KNOWLEDGE_CACHE_TTL_MINUTES = 60;

    // Placeholder pattern
    public static final String PLACEHOLDER_PATTERN = "\\[([A-Z][A-Z_0-9]*)\\]";
}
