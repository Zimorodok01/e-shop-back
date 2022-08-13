package com.example.eshopback.model.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter@Setter
@AllArgsConstructor
@Builder
public class WriteOffResponse {
    private Date date;
    private String product;
    private String cause;
    private String comments;
    private int amount;
}
