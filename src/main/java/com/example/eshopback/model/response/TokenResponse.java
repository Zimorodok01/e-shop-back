package com.example.eshopback.model.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter@Setter
@Builder
public class TokenResponse {
    private String token;
    private Date issuedDate;
    private Date expirationDate;
}
