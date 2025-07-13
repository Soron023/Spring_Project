package com.example.springbootapp.exception;

import org.springframework.http.HttpStatus;

public class BusinessException extends BaseException {
    public BusinessException(String message, String errorCode) {
        super(message, errorCode, HttpStatus.BAD_REQUEST);
    }

    public BusinessException(String message, String errorCode, Object... args) {
        super(message, errorCode, HttpStatus.BAD_REQUEST, args);
    }
} 