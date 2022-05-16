package com.example.controller;

import com.example.application.ExampleApplication;
import com.example.entity.Example;
import com.example.helper.JsonHelper;
import com.example.helper.ObjectMapper;
import com.example.repository.ExampleRepository;
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
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.BodyInserters;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

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

    @Autowired
    private ExampleRepository exampleRepository;

    private static final String VALUE = "value";
    private static final String VALUE_UPDATED = "valueUpdated";

    @Before
    public void setUp() {
        resetDataOnDatabase();
    }

    private void resetDataOnDatabase() {
        exampleRepository.deleteAll();
    }

    private Example saveDataOnDatabase() {
        return exampleRepository.save(Example.builder()
            .value(VALUE)
            .build());
    }

    @Test
    public void getById_thenStatus200() throws Exception {
        Example example = saveDataOnDatabase();

        String jsonResponse = webTestClient
            .get()
            .uri(ExampleController.PATH + "/" + example.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus().isOk()
            .returnResult(String.class)
            .getResponseBody()
            .blockFirst();

        Response response = JsonHelper.readFromString(jsonResponse, Response.class);
        ExampleResponse responseData = objectMapper.map(response.getData(), ExampleResponse.class);

        assertNotNull(responseData);
        assertEquals(example.getValue(), responseData.getValue());
    }

    @Test
    public void getAll_thenStatus200() throws Exception {
        Example example1 = saveDataOnDatabase();
        Example example2 = saveDataOnDatabase();
        List<Example> examples = Arrays.asList(example1, example2);

        Map<String, Example> exampleMap = new HashMap<>();
        exampleMap.put(example1.getId(), example1);
        exampleMap.put(example2.getId(), example2);

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

        assertNotNull(responseData);
        assertEquals(examples.size(), responseData.getEntries().size());

        for (Object o : responseData.getEntries()) {
            ExampleResponse exampleResponse = objectMapper.map(o, ExampleResponse.class);

            Example exampleAssertion = exampleMap.get(exampleResponse.getId());
            assertNotNull(exampleAssertion);
            assertEquals(exampleAssertion.getValue(), exampleResponse.getValue());
        }
    }

    @Test
    public void insert_thenStatus200() throws Exception {
        ExampleRequest request = ExampleRequest.builder()
            .value(VALUE)
            .build();

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

        assertNotNull(responseData);
        assertEquals(request.getValue(), responseData.getValue());

        Optional<Example> optionalExample = exampleRepository.findById(responseData.getId());
        assertTrue(optionalExample.isPresent());
        assertEquals(request.getValue(), optionalExample.get().getValue());
    }

    @Test
    public void update_thenStatus200() throws Exception {
        Example example = saveDataOnDatabase();

        ExampleRequest request = ExampleRequest.builder()
            .value(VALUE_UPDATED)
            .build();

        String jsonResponse = webTestClient
            .put()
            .uri(ExampleController.PATH + "/" + example.getId())
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

        assertNotNull(responseData);
        assertEquals(request.getValue(), responseData.getValue());

        Optional<Example> optionalExample = exampleRepository.findById(responseData.getId());
        assertTrue(optionalExample.isPresent());
        assertEquals(request.getValue(), optionalExample.get().getValue());
    }

    @Test
    public void delete_thenStatus200() throws Exception {
        Example example = saveDataOnDatabase();

        String jsonResponse = webTestClient
            .delete()
            .uri(ExampleController.PATH + "/" + example.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus().isOk()
            .returnResult(String.class)
            .getResponseBody()
            .blockFirst();

        Response response = JsonHelper.readFromString(jsonResponse, Response.class);
        Boolean responseData = objectMapper.map(response.getData(), Boolean.class);

        assertNotNull(responseData);
        assertTrue(responseData);

        Optional<Example> optionalExample = exampleRepository.findById(example.getId());
        assertTrue(optionalExample.isPresent());
        assertTrue(optionalExample.get().isDeleted());
    }

}
