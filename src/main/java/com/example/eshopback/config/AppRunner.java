package com.example.eshopback.config;

import com.example.eshopback.model.entity.Role;
import com.example.eshopback.model.entity.User;
import com.example.eshopback.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

@Configuration
@RequiredArgsConstructor
public class AppRunner implements ApplicationRunner {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;


    @Override
    public void run(ApplicationArguments args) {
        addUser();
    }

    private void addUser() {
        Optional<User> adminOptional = userRepository.findByUsername("admin");
        if (adminOptional.isPresent()) return;

        User user = User.builder()
                .firstName("Admin")
                .lastName("Admin")
                .role(Role.MANAGER)
                .password(passwordEncoder.encode("admin"))
                .username("admin")
                .build();

        userRepository.save(user);
    }
}
