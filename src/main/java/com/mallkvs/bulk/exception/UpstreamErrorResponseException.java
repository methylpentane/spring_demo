package com.mallkvs.bulk.exception;

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
