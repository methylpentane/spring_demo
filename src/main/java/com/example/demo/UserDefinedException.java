package com.example.demo;

public class UserDefinedException extends RuntimeException {
    UserDefinedException(Throwable msg) {
        super(msg);
    }
}
