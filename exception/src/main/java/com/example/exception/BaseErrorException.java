package com.example.exception;

import org.springframework.http.HttpStatus;

import java.util.HashMap;
import java.util.Map;

public class BaseErrorException extends RuntimeException {

    private HttpStatus httpStatus;
    private Map<String, Object> errors;

    BaseErrorException(HttpStatus httpStatus, String message) {
        super(message);
        this.httpStatus = httpStatus;
        this.errors = new HashMap<>();
    }

    BaseErrorException(HttpStatus httpStatus, String message, Map<String, Object> errors) {
        super(message);
        this.httpStatus = httpStatus;
        this.errors = errors;
    }

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }

    public Map<String, Object> getErrors() {
        return errors;
    }

}
