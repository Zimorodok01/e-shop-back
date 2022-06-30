package com.example.eshopback.service.impl;

import com.example.eshopback.model.response.TokenResponse;
import com.example.eshopback.service.TokenService;
import io.jsonwebtoken.Jwts;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;

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
        String token = tokenPrefix + Jwts.builder()
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
}
