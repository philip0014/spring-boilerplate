package com.example.helper;

import com.example.service.model.response.PagingResponseDTO;

import java.util.List;

public class PagingHelper {

    public static <T> PagingResponseDTO<T> create(List<T> entries, int page, int totalPages,
        long totalSize) {
        return PagingResponseDTO.<T>builder()
            .page(page)
            .size(entries.size())
            .totalPage(totalPages)
            .totalSize(totalSize)
            .entries(entries)
            .build();
    }

}
