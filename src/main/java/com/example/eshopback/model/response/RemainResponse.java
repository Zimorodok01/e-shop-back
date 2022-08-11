package com.example.eshopback.model.response;

import com.example.eshopback.model.enums.ProductType;
import lombok.*;

@Getter@Setter
@AllArgsConstructor
@Builder
@NoArgsConstructor
public class RemainResponse {
    private String productName;
    private int amount;
    private ProductType productType;
    private double productPrice;
}
