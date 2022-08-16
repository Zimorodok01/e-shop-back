package com.example.eshopback.model.response;

import com.example.eshopback.model.enums.Role;
import lombok.*;

@Getter
@AllArgsConstructor
@Builder
@NoArgsConstructor
@Setter
@ToString
public class EmployeeResponse {
    private Long id;
    private String name;
    private String username;
    private String role;
    private String email;
    private String phone;
}
