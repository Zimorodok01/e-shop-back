package com.example.eshopback.security;

import com.example.eshopback.model.exception.ErrorBody;
import com.example.eshopback.model.request.AuthenticatedUser;
import com.example.eshopback.model.response.TokenResponse;
import com.example.eshopback.service.TokenService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.server.ResponseStatusException;

import javax.servlet.FilterChain;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@RequiredArgsConstructor
public class JwtUsernameAndPasswordAuthenticationFilter extends UsernamePasswordAuthenticationFilter {
    private final AuthenticationManager authenticationManager;
    private final ObjectMapper objectMapper;
    private final TokenService tokenService;


    @Override
    public Authentication attemptAuthentication(HttpServletRequest request,
                                                HttpServletResponse response) throws AuthenticationException {
        try {
            AuthenticatedUser authenticationRequest =
                    new ObjectMapper().readValue(request.getInputStream(),
                            AuthenticatedUser.class);
            Authentication authentication = new UsernamePasswordAuthenticationToken(
                    authenticationRequest.getUsername(),
                    authenticationRequest.getPassword()
            );
            Authentication authenticate;
                authenticate = authenticationManager.authenticate(authentication);

            return authenticate;
        } catch (BadCredentialsException badCredentialsException) {
            response.setContentType("application/json");
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            try {
                response.getOutputStream()
                        .write(objectMapper.writeValueAsString(ErrorBody.builder()
                                .message("Неправильный логин или пароль")
                                .status(HttpStatus.UNAUTHORIZED)
                                .timeline(new Date())
                                .build()).getBytes(StandardCharsets.UTF_8));
                response.getOutputStream().flush();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            return null;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request,
                                            HttpServletResponse response,
                                            FilterChain chain, Authentication authResult) throws IOException {
        TokenResponse accessToken = tokenService.getAccessToken(authResult.getName(),
                authResult.getAuthorities());
        response.setStatus(HttpServletResponse.SC_OK);
        response.setContentType("application/json");
        response.getWriter().write(objectMapper.writeValueAsString(accessToken));
        response.getWriter().flush();
    }
}
