package com.example.eshopback.model.request;

import com.example.eshopback.model.enums.Role;
import lombok.Getter;
import lombok.Setter;

@Getter@Setter
public class UserRequest {
    private String firstName;
    private String lastName;
    private String username;
    private String password;
    private Role role;
}
