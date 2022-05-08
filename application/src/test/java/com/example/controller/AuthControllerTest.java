package com.example.controller;

import com.example.application.ExampleApplication;
import com.example.helper.JsonHelper;
import com.example.helper.ObjectMapper;
import com.example.service.AuthService;
import com.example.service.model.request.SignInRequestDTO;
import com.example.service.model.response.SignInResponseDTO;
import com.example.web.controller.AuthController;
import com.example.web.model.Response;
import com.example.web.model.auth.SignInRequest;
import com.example.web.model.auth.SignInResponse;
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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
@SpringBootTest(
    webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
    classes = ExampleApplication.class
)
@ActiveProfiles("test")
public class AuthControllerTest {

    @Autowired
    private WebTestClient webTestClient;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private AuthService authService;

    private static final String ID = "id";
    private static final String ACCESS_TOKEN = "accessToken";
    private static final String USERNAME = "username";
    private static final String PASSWORD = "password";

    private static SignInResponseDTO responseDTO;

    @Before
    public void setUp() {
        generateSignInhResponse();
    }

    @Test
    public void signIn_thenStatus200() throws Exception {
        SignInRequest request = SignInRequest.builder()
            .username(USERNAME)
            .password(PASSWORD)
            .build();

        when(authService.signIn(any(SignInRequestDTO.class)))
            .thenReturn(Mono.create(monoSink -> {
                monoSink.success(responseDTO);
            }));

        String jsonResponse = webTestClient
            .post()
            .uri(AuthController.PATH)
            .contentType(MediaType.APPLICATION_JSON)
            .body(BodyInserters.fromValue(request))
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus().isOk()
            .returnResult(String.class)
            .getResponseBody()
            .blockFirst();

        Response response = JsonHelper.readFromString(jsonResponse, Response.class);
        SignInResponse responseData = objectMapper.map(response.getData(), SignInResponse.class);

        verify(authService).signIn(any(SignInRequestDTO.class));

        assertNotNull(responseData);
        assertEquals(ACCESS_TOKEN, responseData.getAccessToken());
    }

    private void generateSignInhResponse() {
        responseDTO = SignInResponseDTO.builder()
            .id(ID)
            .accessToken(ACCESS_TOKEN)
            .username(USERNAME)
            .build();
    }

}
