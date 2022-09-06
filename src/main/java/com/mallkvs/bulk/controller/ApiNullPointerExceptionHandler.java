package com.mallkvs.bulk.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class ApiNullPointerExceptionHandler {
    @ExceptionHandler({NullPointerException.class})
    public ResponseEntity<String> handleNullPointerExceptionHandler(NullPointerException e) {
        String errorMessage = e.toString();
        System.out.println(errorMessage);
        return new ResponseEntity<String>(errorMessage, HttpStatus.BAD_REQUEST);
    }
}
