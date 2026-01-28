package com.bizgenai.exception;

public class GenerationException extends RuntimeException {

    private final String errorCode;

    public GenerationException(String message) {
        super(message);
        this.errorCode = "GENERATION_ERROR";
    }

    public GenerationException(String message, Throwable cause) {
        super(message, cause);
        this.errorCode = "GENERATION_ERROR";
    }

    public GenerationException(String message, String errorCode) {
        super(message);
        this.errorCode = errorCode;
    }

    public GenerationException(String message, String errorCode, Throwable cause) {
        super(message, cause);
        this.errorCode = errorCode;
    }

    public String getErrorCode() {
        return errorCode;
    }
}
