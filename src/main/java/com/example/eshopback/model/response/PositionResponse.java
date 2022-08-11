package com.example.eshopback.model.response;

import com.example.eshopback.model.enums.ProductType;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter@Setter
@Builder
public class PositionResponse {
    private String productName;
    private double price;
    private int amount;
    private ProductType productType;
}
