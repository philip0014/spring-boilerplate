package com.example.web.controller;

import com.example.helper.ObjectMapper;
import com.example.helper.ResponseHelper;
import com.example.service.AuthService;
import com.example.service.model.request.SignInRequestDTO;
import com.example.web.model.Response;
import com.example.web.model.auth.SignInRequest;
import com.example.web.model.auth.SignInResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping(AuthController.PATH)
public class AuthController {

    public static final String PATH = "/api/auth";

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private AuthService authService;

    @PostMapping
    public Mono<Response<SignInResponse>> signIn(@RequestBody SignInRequest signInRequest) {
        return authService.signIn(objectMapper.map(signInRequest, SignInRequestDTO.class))
            .map(o -> objectMapper.map(o, SignInResponse.class))
            .map(ResponseHelper::ok);
    }

}
