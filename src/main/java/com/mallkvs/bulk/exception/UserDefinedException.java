package com.mallkvs.bulk.exception;

public class UserDefinedException extends RuntimeException {
    public UserDefinedException(Throwable msg) {
        super(msg);
    }
}
