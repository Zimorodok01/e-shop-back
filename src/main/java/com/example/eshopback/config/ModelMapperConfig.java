package com.example.eshopback.config;

import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ModelMapperConfig {

    //Dependency Injection
    @Bean
    public ModelMapper modelMapper() {
        return new ModelMapper();
    }
}
