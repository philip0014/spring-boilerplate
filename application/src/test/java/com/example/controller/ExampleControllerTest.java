package com.example.controller;

import com.example.application.ExampleApplication;
import com.example.helper.JsonHelper;
import com.example.helper.ObjectMapper;
import com.example.service.ExampleService;
import com.example.service.model.request.ExampleRequestDTO;
import com.example.service.model.request.PagingRequestDTO;
import com.example.service.model.response.ExampleResponseDTO;
import com.example.service.model.response.PagingResponseDTO;
import com.example.web.controller.ExampleController;
import com.example.web.model.PagingResponse;
import com.example.web.model.Response;
import com.example.web.model.example.ExampleRequest;
import com.example.web.model.example.ExampleResponse;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.BodyInserters;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
@SpringBootTest(
    webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
    classes = ExampleApplication.class
)
@ActiveProfiles("test")
public class ExampleControllerTest {

    @Autowired
    private WebTestClient webTestClient;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private ExampleService exampleService;

    private static final String ID = "id-001";
    private static final String VALUE = "value-001";

    private static ExampleResponseDTO responseDTO1;
    private static ExampleResponseDTO responseDTO2;
    private static List<ExampleResponseDTO> responseDTOS;

    @Before
    public void setUp() {
        generateExampleResponse();

        responseDTOS = new ArrayList<>();
        responseDTOS.add(responseDTO1);
        responseDTOS.add(responseDTO2);
    }

    @Test
    public void getById_thenStatus200() throws Exception {
        when(exampleService.getById(ID)).thenReturn(Mono.create(monoSink -> {
            monoSink.success(responseDTO1);
        }));

        String jsonResponse = webTestClient
            .get()
            .uri(ExampleController.PATH + "/" + ID)
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus().isOk()
            .returnResult(String.class)
            .getResponseBody()
            .blockFirst();

        Response response = JsonHelper.readFromString(jsonResponse, Response.class);
        ExampleResponse responseData = objectMapper.map(response.getData(), ExampleResponse.class);

        verify(exampleService).getById(ID);

        assertNotNull(responseData);
        assertEquals(VALUE, responseData.getValue());
    }

    @Test
    public void getAll_thenStatus200() throws Exception {
        when(exampleService.getAll(any(PagingRequestDTO.class)))
            .thenReturn(Mono.create(monoSink -> {
                monoSink.success(
                    PagingResponseDTO.<ExampleResponseDTO>builder()
                        .entries(responseDTOS)
                        .page(0)
                        .size(responseDTOS.size())
                        .totalSize(responseDTOS.size())
                        .build()
                );
            }));

        String jsonResponse = webTestClient
            .get()
            .uri(ExampleController.PATH)
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus().isOk()
            .returnResult(String.class)
            .getResponseBody()
            .blockFirst();

        Response response = JsonHelper.readFromString(jsonResponse, Response.class);
        PagingResponse responseData = objectMapper.map(response.getData(), PagingResponse.class);

        verify(exampleService).getAll(any(PagingRequestDTO.class));

        assertNotNull(responseData);
        assertEquals(responseDTOS.size(), responseData.getEntries().size());
    }

    @Test
    public void insert_thenStatus200() throws Exception {
        ExampleRequest request = ExampleRequest.builder()
            .value(VALUE)
            .build();

        when(exampleService.insert(any(ExampleRequestDTO.class)))
            .thenReturn(Mono.create(monoSink -> {
                monoSink.success(responseDTO1);
            }));

        String jsonResponse = webTestClient
            .post()
            .uri(ExampleController.PATH)
            .contentType(MediaType.APPLICATION_JSON)
            .body(BodyInserters.fromValue(request))
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus().isOk()
            .returnResult(String.class)
            .getResponseBody()
            .blockFirst();

        Response response = JsonHelper.readFromString(jsonResponse, Response.class);
        ExampleResponse responseData = objectMapper.map(response.getData(), ExampleResponse.class);

        verify(exampleService).insert(any(ExampleRequestDTO.class));

        assertNotNull(responseData);
        assertEquals(VALUE, responseData.getValue());
    }

    @Test
    public void update_thenStatus200() throws Exception {
        ExampleRequest request = ExampleRequest.builder()
            .value(VALUE)
            .build();

        when(exampleService.update(eq(ID), any(ExampleRequestDTO.class)))
            .thenReturn(Mono.create(monoSink -> {
                monoSink.success(responseDTO1);
            }));

        String jsonResponse = webTestClient
            .put()
            .uri(ExampleController.PATH + "/" + ID)
            .contentType(MediaType.APPLICATION_JSON)
            .body(BodyInserters.fromValue(request))
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus().isOk()
            .returnResult(String.class)
            .getResponseBody()
            .blockFirst();

        Response response = JsonHelper.readFromString(jsonResponse, Response.class);
        ExampleResponse responseData = objectMapper.map(response.getData(), ExampleResponse.class);

        verify(exampleService).update(eq(ID), any(ExampleRequestDTO.class));

        assertNotNull(responseData);
        assertEquals(VALUE, responseData.getValue());
    }

    @Test
    public void delete_thenStatus200() throws Exception {
        when(exampleService.delete(ID))
            .thenReturn(Mono.create(monoSink -> {
                monoSink.success(true);
            }));

        String jsonResponse = webTestClient
            .delete()
            .uri(ExampleController.PATH + "/" + ID)
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus().isOk()
            .returnResult(String.class)
            .getResponseBody()
            .blockFirst();

        Response response = JsonHelper.readFromString(jsonResponse, Response.class);
        Boolean responseData = objectMapper.map(response.getData(), Boolean.class);

        verify(exampleService).delete(ID);

        assertNotNull(responseData);
        assertTrue(responseData);
    }

    private void generateExampleResponse() {
        responseDTO1 = ExampleResponseDTO.builder()
            .id(ID)
            .value(VALUE)
            .build();

        responseDTO2 = ExampleResponseDTO.builder()
            .id(ID)
            .value(VALUE)
            .build();
    }

}
