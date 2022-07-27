package com.example.eshopback.model.response;

import lombok.*;

@AllArgsConstructor
@Builder
@NoArgsConstructor
@Getter@Setter
public class SalePointResponse {
    private Long id;
    private String pointName;
}
