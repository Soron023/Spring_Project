package com.example.springbootapp.util;

import com.fasterxml.jackson.annotation.JsonInclude;
import java.time.LocalDateTime;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class GenericResponse<T> {
    private boolean success;
    private String message;
    private T data;
    private List<T> dataList;
    private LocalDateTime timestamp;
    private String errorCode;

    public GenericResponse() {
        this.timestamp = LocalDateTime.now();
    }

    public static <T> GenericResponse<T> success(T data) {
        GenericResponse<T> response = new GenericResponse<>();
        response.setSuccess(true);
        response.setData(data);
        return response;
    }

    public static <T> GenericResponse<T> success(List<T> dataList) {
        GenericResponse<T> response = new GenericResponse<>();
        response.setSuccess(true);
        response.setDataList(dataList);
        return response;
    }

    public static <T> GenericResponse<T> success(String message) {
        GenericResponse<T> response = new GenericResponse<>();
        response.setSuccess(true);
        response.setMessage(message);
        return response;
    }

    public static <T> GenericResponse<T> success(T data, String message) {
        GenericResponse<T> response = new GenericResponse<>();
        response.setSuccess(true);
        response.setData(data);
        response.setMessage(message);
        return response;
    }

    public static <T> GenericResponse<T> error(String message, String errorCode) {
        GenericResponse<T> response = new GenericResponse<>();
        response.setSuccess(false);
        response.setMessage(message);
        response.setErrorCode(errorCode);
        return response;
    }

    // Getters and setters
    public boolean isSuccess() { return success; }
    public void setSuccess(boolean success) { this.success = success; }
    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }
    public T getData() { return data; }
    public void setData(T data) { this.data = data; }
    public List<T> getDataList() { return dataList; }
    public void setDataList(List<T> dataList) { this.dataList = dataList; }
    public LocalDateTime getTimestamp() { return timestamp; }
    public void setTimestamp(LocalDateTime timestamp) { this.timestamp = timestamp; }
    public String getErrorCode() { return errorCode; }
    public void setErrorCode(String errorCode) { this.errorCode = errorCode; }
} 