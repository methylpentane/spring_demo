package com.mallkvs.bulk.exception;

public class InvalidRequestException extends RuntimeException {
    public InvalidRequestException() {
    }

    public InvalidRequestException(Throwable msg) {
        super(msg);
    }
}
