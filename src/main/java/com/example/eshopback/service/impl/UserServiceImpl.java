package com.example.eshopback.service.impl;

import com.example.eshopback.model.entity.SalesPoint;
import com.example.eshopback.model.entity.User;
import com.example.eshopback.model.enums.Role;
import com.example.eshopback.model.exception.ErrorException;
import com.example.eshopback.model.request.UserRequest;
import com.example.eshopback.model.response.EmployeeResponse;
import com.example.eshopback.model.response.SalePointResponse;
import com.example.eshopback.model.response.UserResponse;
import com.example.eshopback.repository.SalesPointRepository;
import com.example.eshopback.repository.UserRepository;
import com.example.eshopback.service.TokenService;
import com.example.eshopback.service.UserService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService, UserDetailsService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final TokenService tokenService;
    private final ModelMapper modelMapper;
    private final SalesPointRepository salesPointRepository;

    private final String USERNAME_NOT_FOUND = "Пользователь с именем %s не существует";
    private final String USER_NOT_FOUND = "Пользователь %d не найден";

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        return userRepository.findByUsername(username)
                .orElseThrow(() -> ErrorException.builder()
                        .status(HttpStatus.NOT_FOUND)
                        .message(String.format(USERNAME_NOT_FOUND, username))
                        .build());
    }

    @Override
    public User getUserById(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> ErrorException.builder()
                        .status(HttpStatus.NOT_FOUND)
                        .message(String.format(USER_NOT_FOUND, userId))
                        .build());
    }

    @Override
    public User getUserByUsername(String username) {
        Optional<User> optionalUser = userRepository.findByUsername(username);

        if (!optionalUser.isPresent()) {
            throw ErrorException.builder()
                    .status(HttpStatus.NOT_FOUND)
                    .message(String.format(USERNAME_NOT_FOUND, username))
                    .build();
        }

        return optionalUser.get();
    }

    @Override
    public void createUser(UserRequest userRequest) {

        Optional<User> optionalUser = userRepository.findByUsername(userRequest.getUsername());
        if (optionalUser.isPresent()) {
            throw ErrorException.builder()
                    .status(HttpStatus.BAD_REQUEST)
                    .message(String.format("Пользователь с именем %s существует", userRequest.getUsername()))
                    .build();
        }

        User user = User.builder()
                .firstName(userRequest.getFirstName())
                .lastName(userRequest.getLastName())
                .password(passwordEncoder.encode(userRequest.getPassword()))
                .username(userRequest.getUsername())
                .role(userRequest.getRole())
                .build();

        userRepository.save(user);
    }

    @Override
    public UserResponse getUserInfo(String authorization) {
        String username = tokenService.getUsername(authorization);
        User user = getUserByUsername(username);

        UserResponse userResponse = modelMapper.map(user, UserResponse.class);

        userResponse.setSalePoint(modelMapper.map(user.getSalesPoint(), SalePointResponse.class));

        return userResponse;
    }

    @Override
    public List<EmployeeResponse> getEmployees(Long salesPointId) {
        SalesPoint salesPoint = salesPointRepository.findById(salesPointId)
                .orElseThrow(() -> ErrorException.builder()
                        .status(HttpStatus.BAD_REQUEST)
                        .message("Торговая точка не существует")
                        .build());


        return userRepository.findBySalesPointAndDeletedAtNull(salesPoint).parallelStream()
                .map(user -> EmployeeResponse.builder()
                        .id(user.getId())
                        .name(user.getFirstName() + " " + user.getLastName())
                        .phone(user.getPhone())
                        .username(user.getUsername())
                        .email(user.getEmail())
                        .role(getRole(user.getRole()))
                        .build()).collect(Collectors.toList());
    }

    private String getRole(Role role) {
        switch (role) {
            case SELLER: return "Продавец";
            case MANAGER: return "Менеджер";
            default: throw ErrorException.builder()
                    .status(HttpStatus.BAD_REQUEST)
                    .message("Неправильная роль " + role)
                    .build();
        }
    }
}
