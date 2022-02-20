package com.example.web.controller.handler;

import com.example.enumeration.ErrorMessage;
import com.example.exception.BadRequestException;
import com.example.helper.ResponseHelper;
import com.example.web.model.Response;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class RestExceptionHandler {

    @ExceptionHandler(BadRequestException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Response badRequestException(BadRequestException e) {
        return ResponseHelper
            .error(e.getHttpStatus(), e.getMessage(), e.getErrors());
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public Response exception(Exception e) {
        log.error("[{}] Handling exception", HttpStatus.INTERNAL_SERVER_ERROR, e);
        return ResponseHelper
            .error(HttpStatus.INTERNAL_SERVER_ERROR, ErrorMessage.DEFAULT.getMessage());
    }

}
