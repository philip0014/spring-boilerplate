package com.example.service.model.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PagingResponseDTO<T> {

    private int page;
    private int totalPage;
    private int size;
    private long totalSize;
    private List<T> entries;

}
