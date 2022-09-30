package com.mallkvs.bulk.controller;

import io.github.resilience4j.circuitbreaker.CallNotPermittedException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class CircuitBreakerHandler {
    static final Logger logger = LogManager.getLogger(CircuitBreakerHandler.class.toString());
    static final String errorMessage = "Circuit Breaker is Open.";
    /**
     * It returns fallback response (503) when the exception is thrown. <br>
     * exception is come from opened CB.
     * @return 503 response that means opened CB.
     */
    @ExceptionHandler({CallNotPermittedException.class})
    public ResponseEntity<String> handle(CallNotPermittedException e) {
        logger.error(errorMessage);
        return new ResponseEntity<String>(errorMessage, HttpStatus.SERVICE_UNAVAILABLE);
    }
}

