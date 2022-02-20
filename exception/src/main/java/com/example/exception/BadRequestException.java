package com.example.exception;

import org.springframework.http.HttpStatus;

import java.util.Map;

public class BadRequestException extends BaseErrorException {

    public BadRequestException() {
        super(HttpStatus.BAD_REQUEST, HttpStatus.BAD_REQUEST.getReasonPhrase());
    }

    public BadRequestException(Map<String, Object> errors) {
        super(HttpStatus.BAD_REQUEST, HttpStatus.BAD_REQUEST.getReasonPhrase(), errors);
    }

}
