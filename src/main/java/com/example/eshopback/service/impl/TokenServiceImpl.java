package com.example.eshopback.service.impl;

import com.example.eshopback.model.exception.ErrorBody;
import com.example.eshopback.model.exception.ErrorException;
import com.example.eshopback.model.response.TokenResponse;
import com.example.eshopback.service.TokenService;
import com.google.common.base.Strings;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import javax.servlet.http.HttpServletResponse;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.stream.Collectors;

import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import static org.springframework.http.HttpStatus.UNAUTHORIZED;

@Service
@RequiredArgsConstructor
public class TokenServiceImpl implements TokenService {
    private final SecretKey secretKey;
    @Value("${application.jwt.tokenPrefix}")
    private String tokenPrefix;

    @Override
    public TokenResponse getAccessToken(String name, Collection<? extends GrantedAuthority> authorities) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.add(Calendar.DATE,1);
        String token = Jwts.builder()
                .setSubject(name)
                .claim("authorities", authorities)
                .claim("token","ACCESS")
                .setIssuedAt(new Date())
                .setExpiration(calendar.getTime())
                .signWith(secretKey)
                .compact();

        return TokenResponse.builder()
                .token(token)
                .issuedDate(new Date())
                .expirationDate(calendar.getTime())
                .build();
    }

    @Override
    public String getUsername(String authorization) {
        String token = authorization.replace("Bearer ", "");

        try {

            Jws<Claims> claimsJws = Jwts.parser()
                    .setSigningKey(secretKey)
                    .parseClaimsJws(token);

            Claims body = claimsJws.getBody();

            String username = body.getSubject();

            return username;
        } catch (JwtException e) {
            throw ErrorException.builder()
                    .status(INTERNAL_SERVER_ERROR)
                    .message("Токен не валидный")
                    .timeline(new Date())
                    .build();
        }
    }
}
