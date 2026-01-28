package com.bizgenai.validation;

import org.springframework.stereotype.Component;

import java.util.regex.Pattern;

@Component
public class InputValidator {

    private static final Pattern EMAIL_PATTERN = Pattern.compile("^[^@]+@[^@]+\\.[^@]+$");
    private static final Pattern URL_PATTERN = Pattern.compile("^https?://.*");

    public boolean isValidEmail(String email) {
        return email != null && EMAIL_PATTERN.matcher(email).matches();
    }

    public boolean isValidUrl(String url) {
        return url != null && URL_PATTERN.matcher(url).matches();
    }

    public boolean isNotBlank(String value) {
        return value != null && !value.trim().isEmpty();
    }

    public boolean isWithinLength(String value, int minLength, int maxLength) {
        if (value == null) return minLength == 0;
        int length = value.length();
        return length >= minLength && length <= maxLength;
    }

    public boolean matchesPattern(String value, String pattern) {
        if (value == null || pattern == null) return false;
        try {
            return Pattern.compile(pattern).matcher(value).matches();
        } catch (Exception e) {
            return false;
        }
    }
}
