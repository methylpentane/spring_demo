package com.mallkvs.bulk.exception;

import com.mallkvs.bulk.exception.errors.ErrorCode;
import com.mallkvs.bulk.exception.errors.ErrorMessage;
import org.springframework.http.HttpStatus;

public class UpstreamTimeoutException extends AggregationServiceException {
    private final ErrorCode errorCode;
    private final String errorMessage;

    public UpstreamTimeoutException(String apiName) {
        errorCode = ErrorCode.UPSTREAM_TIMEOUT_ERROR;
        errorMessage = String.format(ErrorMessage.UPSTREAM_TIMEOUT_ERROR, apiName);
    }

    public UpstreamTimeoutException(String apiName, Throwable cause) {
        super(cause);
        errorCode = ErrorCode.UPSTREAM_TIMEOUT_ERROR;
        errorMessage = String.format(ErrorMessage.UPSTREAM_TIMEOUT_ERROR, apiName);
    }

    @Override
    public ErrorCode getErrorCode() {
        return errorCode;
    }

    @Override
    public String getErrorMessage() {
        return errorMessage;
    }

    @Override
    public HttpStatus getStatus() {
        return HttpStatus.GATEWAY_TIMEOUT;
    }
}