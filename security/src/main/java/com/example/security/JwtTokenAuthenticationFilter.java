package com.example.security;

import com.example.enumeration.ErrorMessage;
import com.example.exception.InvalidJwtAuthenticationException;
import com.example.helper.JsonHelper;
import com.example.helper.ResponseHelper;
import com.example.web.model.Response;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
@Component
public class JwtTokenAuthenticationFilter extends OncePerRequestFilter {

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        String path = request.getRequestURI();
        boolean shouldNotBeFiltered = false;
        // TODO implement list of permitted endpoint from properties
        shouldNotBeFiltered = path.contains("/auth");
        return shouldNotBeFiltered;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
        FilterChain filterChain) throws ServletException, IOException {
        try {
            String token = jwtTokenProvider.resolveToken(request);
            if (token != null && jwtTokenProvider.validateToken(token)) {
                Authentication auth = jwtTokenProvider.getAuthentication(token);
                if (auth != null) {
                    SecurityContextHolder.getContext().setAuthentication(auth);
                }
            }
            filterChain.doFilter(request, response);
        } catch (InvalidJwtAuthenticationException e) {
            Response responseBody = ResponseHelper
                .error(HttpStatus.UNAUTHORIZED, ErrorMessage.INVALID_TOKEN.getMessage());
            sendError(response, HttpServletResponse.SC_UNAUTHORIZED, responseBody);
        } catch (Exception e) {
            log.error("Error when doing doFilterInternal with req: {}, res: {}", request,
                response, e);

            Response responseBody = ResponseHelper
                .error(HttpStatus.INTERNAL_SERVER_ERROR, ErrorMessage.DEFAULT.getMessage());
            sendError(response, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, responseBody);
        }
    }

    private void sendError(HttpServletResponse response, int status, Response responseBody)
        throws IOException {
        response.resetBuffer();
        response.setStatus(status);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.getOutputStream().write(JsonHelper.writeAsBytes(responseBody));
        response.flushBuffer();
    }

}
