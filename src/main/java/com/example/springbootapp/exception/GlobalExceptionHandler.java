package com.example.springbootapp.exception;

import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private final MessageSource messageSource;

    public GlobalExceptionHandler(MessageSource messageSource) {
        this.messageSource = messageSource;
    }

    @ExceptionHandler(BaseException.class)
    public ResponseEntity<ErrorResponse> handleBaseException(BaseException ex) {
        String message = messageSource.getMessage(ex.getErrorCode(), ex.getArgs(), ex.getMessage(), LocaleContextHolder.getLocale());
        
        ErrorResponse errorResponse = ErrorResponse.builder()
                .timestamp(LocalDateTime.now())
                .status(ex.getHttpStatus().value())
                .error(ex.getHttpStatus().getReasonPhrase())
                .message(message)
                .errorCode(ex.getErrorCode())
                .build();

        return new ResponseEntity<>(errorResponse, ex.getHttpStatus());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ValidationErrorResponse> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .collect(Collectors.toMap(
                        FieldError::getField,
                        error -> messageSource.getMessage(error.getDefaultMessage(), null, error.getDefaultMessage(), LocaleContextHolder.getLocale())
                ));

        ValidationErrorResponse errorResponse = ValidationErrorResponse.validationBuilder()
                .timestamp(LocalDateTime.now())
                .status(HttpStatus.BAD_REQUEST.value())
                .error(HttpStatus.BAD_REQUEST.getReasonPhrase())
                .message("Validation failed")
                .errors(errors)
                .build();

        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGenericException(Exception ex) {
        ErrorResponse errorResponse = ErrorResponse.builder()
                .timestamp(LocalDateTime.now())
                .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                .error(HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase())
                .message("An unexpected error occurred")
                .errorCode("INTERNAL_ERROR")
                .build();

        return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    public static class ErrorResponse {
        private LocalDateTime timestamp;
        private int status;
        private String error;
        private String message;
        private String errorCode;

        public ErrorResponse() {}

        public static ErrorResponseBuilder builder() {
            return new ErrorResponseBuilder();
        }

        // Getters and setters
        public LocalDateTime getTimestamp() { return timestamp; }
        public void setTimestamp(LocalDateTime timestamp) { this.timestamp = timestamp; }
        public int getStatus() { return status; }
        public void setStatus(int status) { this.status = status; }
        public String getError() { return error; }
        public void setError(String error) { this.error = error; }
        public String getMessage() { return message; }
        public void setMessage(String message) { this.message = message; }
        public String getErrorCode() { return errorCode; }
        public void setErrorCode(String errorCode) { this.errorCode = errorCode; }

        public static class ErrorResponseBuilder {
            private ErrorResponse errorResponse = new ErrorResponse();

            public ErrorResponseBuilder timestamp(LocalDateTime timestamp) {
                errorResponse.timestamp = timestamp;
                return this;
            }

            public ErrorResponseBuilder status(int status) {
                errorResponse.status = status;
                return this;
            }

            public ErrorResponseBuilder error(String error) {
                errorResponse.error = error;
                return this;
            }

            public ErrorResponseBuilder message(String message) {
                errorResponse.message = message;
                return this;
            }

            public ErrorResponseBuilder errorCode(String errorCode) {
                errorResponse.errorCode = errorCode;
                return this;
            }

            public ErrorResponse build() {
                return errorResponse;
            }
        }
    }

    public static class ValidationErrorResponse extends ErrorResponse {
        private Map<String, String> errors = new HashMap<>();

        public ValidationErrorResponse() {}

        public static ValidationErrorResponseBuilder validationBuilder() {
            return new ValidationErrorResponseBuilder();
        }

        public Map<String, String> getErrors() { return errors; }
        public void setErrors(Map<String, String> errors) { this.errors = errors; }

        public static class ValidationErrorResponseBuilder {
            private ValidationErrorResponse errorResponse = new ValidationErrorResponse();

            public ValidationErrorResponseBuilder timestamp(LocalDateTime timestamp) {
                errorResponse.setTimestamp(timestamp);
                return this;
            }

            public ValidationErrorResponseBuilder status(int status) {
                errorResponse.setStatus(status);
                return this;
            }

            public ValidationErrorResponseBuilder error(String error) {
                errorResponse.setError(error);
                return this;
            }

            public ValidationErrorResponseBuilder message(String message) {
                errorResponse.setMessage(message);
                return this;
            }

            public ValidationErrorResponseBuilder errors(Map<String, String> errors) {
                errorResponse.errors = errors;
                return this;
            }

            public ValidationErrorResponse build() {
                return errorResponse;
            }
        }
    }
} 