package com.example.springbootapp.exception;

import org.springframework.http.HttpStatus;

public class ValidationException extends BaseException {
    public ValidationException(String message, String errorCode) {
        super(message, errorCode, HttpStatus.UNPROCESSABLE_ENTITY);
    }

    public ValidationException(String message, String errorCode, Object... args) {
        super(message, errorCode, HttpStatus.UNPROCESSABLE_ENTITY, args);
    }
} 