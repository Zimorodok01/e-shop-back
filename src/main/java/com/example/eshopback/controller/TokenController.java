package com.example.eshopback.controller;

import com.example.eshopback.model.entity.User;
import com.example.eshopback.model.request.AuthenticatedUser;
import com.example.eshopback.service.TokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/token")
public class TokenController {
    private final TokenService tokenService;
    private final UserDetailsService userDetailsService;

    @PostMapping
    public ResponseEntity<?> getToken(@RequestBody AuthenticatedUser authenticatedUser) {
        User user = (User) userDetailsService.loadUserByUsername(authenticatedUser.getUsername());
        return ResponseEntity.ok(tokenService.getToken(user));
    }
}
