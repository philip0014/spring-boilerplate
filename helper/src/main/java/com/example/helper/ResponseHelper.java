package com.example.helper;

import com.example.service.model.response.PagingResponseDTO;
import com.example.web.model.PagingResponse;
import com.example.web.model.Response;
import org.springframework.http.HttpStatus;

import java.util.Collections;
import java.util.List;
import java.util.Map;

public class ResponseHelper {

    private static final String MESSAGE_KEY = "message";

    public static <T> Response<T> ok() {
        return ok(null);
    }

    public static <T> Response<T> ok(T data) {
        return Response.<T>builder()
            .code(HttpStatus.OK.value())
            .status(HttpStatus.OK)
            .data(data)
            .build();
    }

    public static <T> Response<PagingResponse<T>> okWithPaging(int page, int size, int totalPage,
        long totalSize, List<T> entries) {
        return Response.<PagingResponse<T>>builder()
            .code(HttpStatus.OK.value())
            .status(HttpStatus.OK)
            .data(PagingResponse.<T>builder()
                .page(page)
                .size(size)
                .totalPage(totalPage)
                .totalSize(totalSize)
                .entries(entries)
                .build())
            .build();
    }

    public static Response error(HttpStatus httpStatus, String message) {
        return error(httpStatus, message, Collections.singletonMap(MESSAGE_KEY, message));
    }

    public static Response error(HttpStatus httpStatus, String message,
        Map<String, Object> errors) {
        return Response.builder()
            .code(httpStatus.value())
            .status(httpStatus)
            .data(message)
            .errors(errors)
            .build();
    }

}
