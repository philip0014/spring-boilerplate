package com.example.service;

import com.example.service.model.request.ExampleRequestDTO;
import com.example.service.model.request.PagingRequestDTO;
import com.example.service.model.response.ExampleResponseDTO;
import com.example.service.model.response.PagingResponseDTO;
import reactor.core.publisher.Mono;

public interface ExampleService {

    Mono<ExampleResponseDTO> getById(String id);

    Mono<PagingResponseDTO<ExampleResponseDTO>> getAll(PagingRequestDTO pagingRequest);

    Mono<ExampleResponseDTO> insert(ExampleRequestDTO request);

    Mono<ExampleResponseDTO> update(String id, ExampleRequestDTO request);

    Mono<Boolean> delete(String id);

}
