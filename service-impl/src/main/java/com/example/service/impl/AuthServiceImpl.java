package com.example.service.impl;

import com.example.entity.User;
import com.example.enumeration.ErrorMessage;
import com.example.exception.BadRequestException;
import com.example.repository.UserRepository;
import com.example.security.JwtTokenProvider;
import com.example.service.AuthService;
import com.example.service.model.request.SignInRequestDTO;
import com.example.service.model.response.SignInResponseDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.Collections;

@Slf4j
@Service
public class AuthServiceImpl implements AuthService {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @Autowired
    private UserRepository userRepository;

    @Override
    public Mono<SignInResponseDTO> signIn(SignInRequestDTO signInRequest) {
        return Mono.create(monoSink -> {
            authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(signInRequest.getUsername(),
                    String.valueOf(signInRequest.getPassword())));

            User user = userRepository.findByUsername(signInRequest.getUsername())
                .orElseThrow(() -> new BadRequestException(
                    Collections.singletonMap(
                        signInRequest.getUsername(),
                        ErrorMessage.AUTHENTICATION_FAILED.name()
                    )
                ));

            String token = jwtTokenProvider.createToken(user.getUsername(),
                Collections.singletonList(user.getUserRole().toString()));

            SignInResponseDTO response = SignInResponseDTO.builder()
                .id(user.getId())
                .username(user.getUsername())
                .accessToken(token)
                .build();
            monoSink.success(response);
        });
    }
}
