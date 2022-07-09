package com.example.eshopback.model.request;

import lombok.Getter;
import lombok.Setter;

@Getter@Setter
public class AuthenticatedUser {
    private String username;
    private String password;
}
