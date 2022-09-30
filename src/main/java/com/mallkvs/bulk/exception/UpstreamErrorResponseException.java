package com.mallkvs.bulk.exception;

/**
 * Exception that represents any error from upstream
 * this itself doesn't cause error response. pls deal it and return 207.
 */
public class UpstreamErrorResponseException extends RuntimeException {

    private final int statusCode;

    public UpstreamErrorResponseException(int statusCode, String message) {
        super(message);
        this.statusCode = statusCode;
    }

    public int getStatusCode() {
        return statusCode;
    }
}
