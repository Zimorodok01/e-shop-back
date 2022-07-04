package com.example.eshopback.config;

import io.jsonwebtoken.security.Keys;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.crypto.SecretKey;

@Configuration
@Getter
public class JWTConfig {
    @Value("${application.jwt.secretKey}")
    private String secretKey;

    @Bean
    public SecretKey secretKey() {
        return Keys.hmacShaKeyFor(secretKey.getBytes());
    }
}
