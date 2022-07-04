package com.example.eshopback.service;

import com.example.eshopback.model.response.TokenResponse;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

public interface TokenService {
    TokenResponse getAccessToken(String name, Collection<? extends GrantedAuthority> authorities);
}
