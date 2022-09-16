package com.mallkvs.bulk.exception;

import com.mallkvs.bulk.exception.errors.ErrorCode;
import org.springframework.http.HttpStatus;

public abstract class AggregationServiceException extends RuntimeException {
    AggregationServiceException() {}

    AggregationServiceException(Throwable cause) {
        super(cause);
    }

    public abstract ErrorCode getErrorCode();

    public abstract String getErrorMessage();

    public abstract HttpStatus getStatus();
}
