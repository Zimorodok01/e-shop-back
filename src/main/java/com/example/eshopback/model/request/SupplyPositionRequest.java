package com.example.eshopback.model.request;

import com.example.eshopback.model.enums.NDSType;
import lombok.Getter;
import lombok.Setter;

@Getter@Setter
public class SupplyPositionRequest {
    private Long product;
    private int amount;
    private double price;
    private NDSType ndsType;
}
