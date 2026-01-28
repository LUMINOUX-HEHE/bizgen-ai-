package com.bizgenai.exception;

public class BlueprintParsingException extends RuntimeException {

    private final String blueprintPath;

    public BlueprintParsingException(String message) {
        super(message);
        this.blueprintPath = null;
    }

    public BlueprintParsingException(String message, Throwable cause) {
        super(message, cause);
        this.blueprintPath = null;
    }

    public BlueprintParsingException(String message, String blueprintPath) {
        super(message);
        this.blueprintPath = blueprintPath;
    }

    public BlueprintParsingException(String message, String blueprintPath, Throwable cause) {
        super(message, cause);
        this.blueprintPath = blueprintPath;
    }

    public String getBlueprintPath() {
        return blueprintPath;
    }
}
