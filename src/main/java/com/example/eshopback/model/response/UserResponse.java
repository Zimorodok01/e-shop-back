package com.example.eshopback.model.response;

import com.example.eshopback.model.enums.Role;
import lombok.*;

@Getter
@AllArgsConstructor
@Builder
@NoArgsConstructor
@Setter
@ToString
public class UserResponse {
    private Long id;
    private String firstName;
    private String lastName;
    private String username;
    private Role role;
    private SalePointResponse salePoint;
}
