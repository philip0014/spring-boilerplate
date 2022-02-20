package com.example.helper;

import com.example.service.model.request.PagingRequestDTO;
import com.example.service.model.response.PagingResponseDTO;

import java.util.List;

public class PagingHelper {

    public static <T> PagingResponseDTO<T> create(List<T> entries, PagingRequestDTO pagingRequest,
        int totalPages, long totalSize) {
        return PagingResponseDTO.<T>builder()
            .page(pagingRequest.getPage())
            .size(pagingRequest.getSize())
            .totalPage(totalPages)
            .totalSize(totalSize)
            .entries(entries)
            .build();
    }

}
