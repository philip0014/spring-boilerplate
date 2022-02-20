package com.example.service.impl;

import com.example.entity.Example;
import com.example.enumeration.ErrorMessage;
import com.example.exception.BadRequestException;
import com.example.helper.PagingHelper;
import com.example.repository.ExampleRepository;
import com.example.service.ExampleService;
import com.example.service.model.request.ExampleRequestDTO;
import com.example.service.model.request.PagingRequestDTO;
import com.example.service.model.response.ExampleResponseDTO;
import com.example.helper.ObjectMapper;
import com.example.service.model.response.PagingResponseDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Slf4j
@Service
public class ExampleServiceImpl implements ExampleService {

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private ExampleRepository exampleRepository;

    @Override
    public Mono<ExampleResponseDTO> getById(String id) {
        return Mono.<ExampleResponseDTO>create(monoSink -> {
            Example example = getExampleIfExists(id);
            monoSink.success(objectMapper.map(example, ExampleResponseDTO.class));
        }).doOnError(error -> log.error("Error on getById with id: {}", id, error));
    }

    @Override
    public Mono<PagingResponseDTO<ExampleResponseDTO>> getAll(PagingRequestDTO pagingRequest) {
        return Mono.<PagingResponseDTO<ExampleResponseDTO>>create(monoSink -> {
            PageRequest pageRequest =
                PageRequest.of(pagingRequest.getPage(), pagingRequest.getSize());
            Page<Example> pageOfExamples = exampleRepository.findAllByDeletedFalse(pageRequest);
            List<ExampleResponseDTO> entries = pageOfExamples.getContent().stream()
                .map(o -> objectMapper.map(o, ExampleResponseDTO.class))
                .collect(Collectors.toList());
            monoSink.success(PagingHelper
                .create(entries, pagingRequest, pageOfExamples.getTotalPages(),
                    pageOfExamples.getTotalElements()));
        }).doOnError(error -> log.error("Error on getAll with request: {}", pagingRequest, error));
    }

    @Override
    public Mono<ExampleResponseDTO> insert(ExampleRequestDTO request) {
        return Mono.<ExampleResponseDTO>create(monoSink -> {
            Example result = exampleRepository.save(objectMapper.map(request, Example.class));
            monoSink.success(objectMapper.map(result, ExampleResponseDTO.class));
        }).doOnError(error -> log.error("Error on insert with request: {}", request, error));
    }

    @Override
    public Mono<ExampleResponseDTO> update(String id, ExampleRequestDTO request) {
        return Mono.<ExampleResponseDTO>create(monoSink -> {
            Example example = getExampleIfExists(id);
            example.setValue(request.getValue());
            exampleRepository.save(example);
            monoSink.success(objectMapper.map(example, ExampleResponseDTO.class));
        }).doOnError(
            error -> log.error("Error on update with id: {}, request: {}", id, request, error));
    }

    @Override
    public Mono<Boolean> delete(String id) {
        return Mono.<Boolean>create(monoSink -> {
            Example example = getExampleIfExists(id);
            example.setDeleted(true);
            exampleRepository.save(example);
            monoSink.success(true);
        }).doOnError(error -> log.error("Error on delete with id: {}", id, error));
    }

    private Example getExampleIfExists(String id) {
        Example example = exampleRepository.findByIdAndDeletedFalse(id);
        if (Objects.isNull(example)) {
            throw new BadRequestException(
                Collections.singletonMap(id, ErrorMessage.ID_NOT_FOUND.writeMessage(id)));
        }
        return example;
    }

}
