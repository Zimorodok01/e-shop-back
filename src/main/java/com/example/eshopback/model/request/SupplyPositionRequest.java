package com.example.eshopback.model.request;

import lombok.Getter;
import lombok.Setter;

@Getter@Setter
public class SupplyPositionRequest {
    private Long product;
    private int amount;
    private double price;
}
