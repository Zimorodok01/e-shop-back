package com.example.eshopback.service;

import com.example.eshopback.model.entity.User;
import com.example.eshopback.model.request.AuthenticatedUser;
import com.example.eshopback.model.response.TokenResponse;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

public interface TokenService {
    TokenResponse getAccessToken(String name, Collection<? extends GrantedAuthority> authorities);

    String getUsername(String authorization);

    TokenResponse getToken(User user);
}
