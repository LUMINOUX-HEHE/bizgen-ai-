package com.bizgenai.util;

import java.util.List;
import java.util.stream.Collectors;

public final class StringUtils {

    private StringUtils() {
        // Utility class
    }

    public static boolean isBlank(String str) {
        return str == null || str.trim().isEmpty();
    }

    public static boolean isNotBlank(String str) {
        return !isBlank(str);
    }

    public static String truncate(String str, int maxLength) {
        if (str == null) return null;
        if (str.length() <= maxLength) return str;
        return str.substring(0, maxLength - 3) + "...";
    }

    public static String truncateWithSuffix(String str, int maxLength, String suffix) {
        if (str == null) return null;
        if (str.length() <= maxLength) return str;
        int suffixLength = suffix != null ? suffix.length() : 0;
        return str.substring(0, maxLength - suffixLength) + (suffix != null ? suffix : "");
    }

    public static String capitalize(String str) {
        if (isBlank(str)) return str;
        return str.substring(0, 1).toUpperCase() + str.substring(1).toLowerCase();
    }

    public static String toTitleCase(String str) {
        if (isBlank(str)) return str;
        String[] words = str.split("\\s+");
        StringBuilder result = new StringBuilder();
        for (String word : words) {
            if (!result.isEmpty()) result.append(" ");
            result.append(capitalize(word));
        }
        return result.toString();
    }

    public static String toSlug(String str) {
        if (isBlank(str)) return str;
        return str.toLowerCase()
                .replaceAll("[^a-z0-9\\s-]", "")
                .replaceAll("\\s+", "-")
                .replaceAll("-+", "-")
                .replaceAll("^-|-$", "");
    }

    public static String joinWithComma(List<String> items) {
        if (items == null || items.isEmpty()) return "";
        return items.stream()
                .filter(StringUtils::isNotBlank)
                .collect(Collectors.joining(", "));
    }

    public static String formatPlaceholder(String placeholder) {
        if (isBlank(placeholder)) return placeholder;
        return placeholder.replace("_", " ").toLowerCase();
    }
}
