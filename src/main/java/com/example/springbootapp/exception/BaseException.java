package com.example.springbootapp.exception;

import org.springframework.http.HttpStatus;

public abstract class BaseException extends RuntimeException {
    private final String errorCode;
    private final HttpStatus httpStatus;
    private final Object[] args;

    protected BaseException(String message, String errorCode, HttpStatus httpStatus) {
        super(message);
        this.errorCode = errorCode;
        this.httpStatus = httpStatus;
        this.args = new Object[0];
    }

    protected BaseException(String message, String errorCode, HttpStatus httpStatus, Object... args) {
        super(message);
        this.errorCode = errorCode;
        this.httpStatus = httpStatus;
        this.args = args;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }

    public Object[] getArgs() {
        return args;
    }
} 