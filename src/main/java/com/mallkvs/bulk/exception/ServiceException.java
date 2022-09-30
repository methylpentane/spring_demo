package com.mallkvs.bulk.exception;

/**
 * Exception that represents any 503 SERVICE_UNAVAILABLE except CircuitBreaker
 */
public class ServiceException extends RuntimeException {

    private final int statusCode;

    public ServiceException (int statusCode, String message) {
        super(message);
        this.statusCode = statusCode;
    }

    public int getStatusCode() {
        return statusCode;
    }
}
