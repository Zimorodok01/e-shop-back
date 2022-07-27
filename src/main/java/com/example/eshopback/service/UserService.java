package com.example.eshopback.service;

import com.example.eshopback.model.entity.User;
import com.example.eshopback.model.request.UserRequest;
import com.example.eshopback.model.response.UserResponse;

public interface UserService {
    User getUserById(Long userId);

    void createUser(UserRequest userRequest);

    UserResponse getUserInfo(String authorization);

    User getUserByUsername(String username);
}
