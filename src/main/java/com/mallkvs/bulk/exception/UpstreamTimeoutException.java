package com.mallkvs.bulk.exception;


public class UpstreamTimeoutException extends RuntimeException {
    public UpstreamTimeoutException(String message) {
        super(message);
    }
}