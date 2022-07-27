package com.example.eshopback.service.impl;

import com.example.eshopback.model.entity.User;
import com.example.eshopback.model.exception.ErrorException;
import com.example.eshopback.model.request.UserRequest;
import com.example.eshopback.model.response.SalePointResponse;
import com.example.eshopback.model.response.UserResponse;
import com.example.eshopback.repository.UserRepository;
import com.example.eshopback.service.ShiftService;
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

import java.util.Optional;

import static com.example.eshopback.model.enums.Role.SELLER;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService, UserDetailsService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final TokenService tokenService;
    private final ModelMapper modelMapper;
    private final ShiftService shiftService;

    private final String USERNAME_NOT_FOUND = "Пользователь с именем %s не существует";
    private final String USER_NOT_FOUND = "Пользователь %d не найден";
    private final String USERNAME_EXIST = "Пользователь с именем %s существует";

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
                    .message(String.format(USERNAME_EXIST, userRequest.getUsername()))
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

        boolean isOpened = shiftService.isOpened(user.getSalesPoint());

        if (user.getRole() == SELLER && !isOpened) {
            throw ErrorException.builder()
                    .message("Ваша смена не открыта")
                    .status(HttpStatus.BAD_REQUEST)
                    .build();
        }

        UserResponse userResponse = modelMapper.map(user, UserResponse.class);

        userResponse.setSalePoint(modelMapper.map(user.getSalesPoint(), SalePointResponse.class));

        return userResponse;
    }
}
