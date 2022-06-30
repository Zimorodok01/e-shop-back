package com.example.eshopback.service.impl;

import com.example.eshopback.model.exception.ErrorException;
import com.example.eshopback.repository.UserRepository;
import com.example.eshopback.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService, UserDetailsService {
    private final UserRepository userRepository;

    private final String USERNAME_NOT_FOUND = "Пользователь с именем %s не существует";

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        return userRepository.findByUsername(username)
                .orElseThrow(() -> ErrorException.builder()
                        .status(HttpStatus.NOT_FOUND)
                        .message(String.format(USERNAME_NOT_FOUND, username))
                        .build());
    }
}
