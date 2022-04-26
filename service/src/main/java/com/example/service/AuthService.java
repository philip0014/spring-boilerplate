package com.example.service;

import com.example.service.model.request.SignInRequestDTO;
import com.example.service.model.response.SignInResponseDTO;
import reactor.core.publisher.Mono;

public interface AuthService {

    Mono<SignInResponseDTO> signIn(SignInRequestDTO signInRequest);

}
