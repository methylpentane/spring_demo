package com.mallkvs.bulk.controller;

import io.github.resilience4j.circuitbreaker.CallNotPermittedException;
import io.netty.handler.timeout.ReadTimeoutException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ReadTimeoutHandler {
    static final Logger logger = LogManager.getLogger(ReadTimeoutHandler.class.toString());
    static final String errorMessage = "Read timout";
    @ExceptionHandler({ReadTimeoutException.class})
    public ResponseEntity<String> handle(ReadTimeoutException e) {
        logger.error(errorMessage);
        return new ResponseEntity<String>(errorMessage, HttpStatus.SERVICE_UNAVAILABLE);
    }
}

