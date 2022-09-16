package com.mallkvs.bulk.controller;

import com.mallkvs.bulk.exception.InvalidRequestException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class InvalidRequestHandler {
    static final Logger logger = LogManager.getLogger(InvalidRequestHandler.class.toString());
    @ExceptionHandler({InvalidRequestException.class})
    public ResponseEntity<String> handle(InvalidRequestException e) {
        String errorMessage = e.getMessage();
        logger.error(errorMessage);
        return new ResponseEntity<String>(errorMessage, HttpStatus.BAD_REQUEST);
    }
}
