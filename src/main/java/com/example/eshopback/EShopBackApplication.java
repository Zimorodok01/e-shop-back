package com.example.eshopback;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@RestController
public class EShopBackApplication {

    public static void main(String[] args) {
        SpringApplication.run(EShopBackApplication.class, args);
    }

    @GetMapping("/api/main")
    public String testHeroku() {
        return "Hello World!";
    }

}
