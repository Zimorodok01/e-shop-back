package com.example.eshopback.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.example.eshopback.model.entity.User;
import com.example.eshopback.model.exception.ErrorBody;
import com.example.eshopback.model.exception.ErrorException;
import com.example.eshopback.model.request.AuthenticatedUser;
import com.example.eshopback.service.TokenService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Strings;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.server.ResponseStatusException;

import javax.crypto.SecretKey;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.stream.Collectors;

import static org.springframework.http.HttpStatus.*;

@RequiredArgsConstructor
public class JwtTokenVerifier extends OncePerRequestFilter {
    private final SecretKey secretKey;
    private final String authorizationHeader;
    private final String tokenPrefix;
    private final ObjectMapper objectMapper;
    private final AuthenticationManager authenticationManager;
    private final TokenService tokenService;
    private final UserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String authorizationHeader = request
                .getHeader(this.authorizationHeader);

        if (authorizationHeader == null) {
            ErrorBody exception = ErrorBody.builder()
                    .status(UNAUTHORIZED)
                    .message("Вы не авторизовались. Для выполнение этого действие нужно авторизация")
                    .timeline(new Date())
                    .build();

            response.setContentType("application/json");
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getOutputStream().write(objectMapper.writeValueAsString(exception).getBytes(StandardCharsets.UTF_8));
            response.getOutputStream().flush();

            filterChain.doFilter(request, response);
        }

        if (Strings.isNullOrEmpty(authorizationHeader) ||
                !authorizationHeader.startsWith(tokenPrefix)) {
            filterChain.doFilter(request,response);
            return;
        }

        String token = authorizationHeader.replace(tokenPrefix, "");

        try {
            DecodedJWT decoderToken = JWT.decode(token);
            if (decoderToken.getExpiresAt().before(new Date())) {
                ErrorBody exception = ErrorBody.builder()
                        .status(FORBIDDEN)
                        .message("Время токена истек. Заново авторизуйтесь")
                        .timeline(new Date())
                        .build();

                response.setContentType("application/json");
                response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                response.getOutputStream().write(objectMapper.writeValueAsString(exception).getBytes(StandardCharsets.UTF_8));
                response.getOutputStream().flush();
                return;
            }

            Jws<Claims> claimsJws = Jwts.parser()
                    .setSigningKey(secretKey)
                    .parseClaimsJws(token);

            Claims body = claimsJws.getBody();

            String username = body.getSubject();

            List<Map<String, String>> authorities =
                    (List<Map<String,String>> ) body.get("authorities");

            Set<SimpleGrantedAuthority> simpleGrantedAuthorities = authorities.stream()
                    .map(m -> new SimpleGrantedAuthority(m.get("authority")))
                    .collect(Collectors.toSet());

            Authentication authentication = new UsernamePasswordAuthenticationToken(
                    username,
                    null,
                    simpleGrantedAuthorities
            );

            SecurityContextHolder.getContext().setAuthentication(authentication);
        } catch (JwtException e) {
            ErrorBody exception = ErrorBody.builder()
                    .status(INTERNAL_SERVER_ERROR)
                    .message("Что-то полшо не так во время получение токена")
                    .timeline(new Date())
                    .build();

            response.setContentType("application/json");
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getOutputStream().write(objectMapper.writeValueAsString(exception).getBytes(StandardCharsets.UTF_8));
            response.getOutputStream().flush();
        } catch (AccessDeniedException e) {
            ErrorBody exception = ErrorBody.builder()
                    .status(FORBIDDEN)
                    .message("Access denied")
                    .timeline(new Date())
                    .build();

            response.setContentType("application/json");
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getOutputStream().write(objectMapper.writeValueAsString(exception).getBytes(StandardCharsets.UTF_8));
            response.getOutputStream().flush();
        }

        try {
            filterChain.doFilter(request,response);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
