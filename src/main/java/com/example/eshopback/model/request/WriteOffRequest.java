package com.example.eshopback.model.request;

import lombok.Getter;
import lombok.Setter;

@Getter@Setter
public class WriteOffRequest {
    private Long productId;
    private int amount;
    private String cause;
    private String comments;
}
