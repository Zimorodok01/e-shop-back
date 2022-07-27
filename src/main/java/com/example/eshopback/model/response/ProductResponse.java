package com.example.eshopback.model.response;

import com.example.eshopback.model.enums.ProductType;
import lombok.Getter;
import lombok.Setter;

@Getter@Setter
public class ProductResponse {
    private Long id;
    private String name;
    private double price;
    private ProductType type;
}
