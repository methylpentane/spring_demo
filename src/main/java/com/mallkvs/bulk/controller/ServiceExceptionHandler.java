package com.mallkvs.bulk.controller;

import com.mallkvs.bulk.exception.InvalidRequestException;
import com.mallkvs.bulk.exception.ServiceException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ServiceExceptionHandler {
    static final Logger logger = LogManager.getLogger(ServiceExceptionHandler.class.toString());
    @ExceptionHandler({ServiceException.class})
    public ResponseEntity<String> handle(ServiceException e) {
        String errorMessage = e.getMessage();
        logger.error(errorMessage);
        return new ResponseEntity<String>(errorMessage, HttpStatus.SERVICE_UNAVAILABLE);
    }
}
