package com.example.web.controller;

import com.example.service.ExampleService;
import com.example.service.model.request.ExampleRequestDTO;
import com.example.helper.ObjectMapper;
import com.example.helper.ResponseHelper;
import com.example.service.model.request.PagingRequestDTO;
import com.example.web.model.PagingResponse;
import com.example.web.model.Response;
import com.example.web.model.example.ExampleRequest;
import com.example.web.model.example.ExampleResponse;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping(ExampleController.PATH)
public class ExampleController {

    public static final String PATH = "/api/examples";

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private ExampleService exampleService;

    @GetMapping("/{id}")
    @ApiOperation("Get example by id")
    public Mono<Response<ExampleResponse>> getById(@PathVariable String id) {
        return exampleService.getById(id)
            .map(o -> objectMapper.map(o, ExampleResponse.class))
            .map(ResponseHelper::ok);
    }

    @GetMapping
    @ApiOperation("Get all examples")
    public Mono<Response<PagingResponse<ExampleResponse>>> getAll(
        @RequestParam(defaultValue = "1") int page, @RequestParam(defaultValue = "20") int size) {
        PagingRequestDTO pagingRequest = PagingRequestDTO.builder()
            .page(page - 1)
            .size(size)
            .build();
        return exampleService.getAll(pagingRequest)
            .map(o -> ResponseHelper.okWithPaging(
                o.getPage() + 1,
                o.getSize(),
                o.getTotalPage(),
                o.getTotalSize(),
                objectMapper.mapAsList(o.getEntries(), ExampleResponse.class))
            );
    }

    @PostMapping
    @ApiOperation("Insert example")
    public Mono<Response<ExampleResponse>> insert(@RequestBody ExampleRequest request) {
        return exampleService.insert(objectMapper.map(request, ExampleRequestDTO.class))
            .map(o -> objectMapper.map(o, ExampleResponse.class))
            .map(ResponseHelper::ok);
    }

    @PutMapping("/{id}")
    @ApiOperation("Update example by id")
    public Mono<Response<ExampleResponse>> update(@PathVariable String id,
                                                  @RequestBody ExampleRequest request) {
        return exampleService.update(id, objectMapper.map(request, ExampleRequestDTO.class))
            .map(o -> objectMapper.map(o, ExampleResponse.class))
            .map(ResponseHelper::ok);
    }

    @DeleteMapping("/{id}")
    @ApiOperation("Delete example by id")
    public Mono<Response<Boolean>> delete(@PathVariable String id) {
        return exampleService.delete(id).map(ResponseHelper::ok);
    }

}
