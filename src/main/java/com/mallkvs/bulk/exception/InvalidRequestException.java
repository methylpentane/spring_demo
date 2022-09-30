package com.mallkvs.bulk.exception;

/**
 * Exception that represents BAD_REQUEST
 */
public class InvalidRequestException extends RuntimeException {

    public InvalidRequestException(String msg) {
        super(msg);
    }
}
