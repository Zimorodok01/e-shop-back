package com.example.eshopback.model.request;

import com.example.eshopback.model.enums.ProductType;
import lombok.Getter;
import lombok.Setter;

@Getter@Setter
public class ProductRequest {
    private String productName;
    private ProductType productType;
    private double price;
}