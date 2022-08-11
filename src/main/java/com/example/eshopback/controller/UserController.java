package com.example.eshopback.controller;

import com.example.eshopback.model.entity.User;
import com.example.eshopback.model.request.UserRequest;
import com.example.eshopback.model.response.UserResponse;
import com.example.eshopback.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.ResponseEntity.ok;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/users")
@Slf4j
public class UserController {
    private final UserService userService;
    private final ModelMapper modelMapper;

    @GetMapping("/{userId}")
    public ResponseEntity<UserResponse> getUser(@PathVariable Long userId) {
        User user = userService.getUserById(userId);
        UserResponse map = modelMapper.map(user, UserResponse.class);
        return ok(map);
    }

    @PreAuthorize("hasAuthority('SELLER_WRITE')")
    @PostMapping
    public ResponseEntity<?> createUser(@RequestBody UserRequest userRequest) {
        userService.createUser(userRequest);
        return ok("Пользователь создан");
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/userInfo")
    public ResponseEntity<UserResponse> userInfo(@RequestHeader("Authorization") String authorization) {
        System.out.println("/userInfo");
        UserResponse userInfo = userService.getUserInfo(authorization);
        log.info("userInfo" + userInfo);
        return ok(userInfo);
    }
}
