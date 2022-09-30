package com.mallkvs.bulk.exception;


/**
 * Exception that represents timeout error on upstream
 * this itself doesn't cause error response. pls deal it and return 207.
 */
public class UpstreamTimeoutException extends RuntimeException {
    public UpstreamTimeoutException(String message) {
        super(message);
    }
}