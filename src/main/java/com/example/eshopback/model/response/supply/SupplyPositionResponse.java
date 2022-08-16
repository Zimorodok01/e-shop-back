package com.example.eshopback.model.response.supply;

import com.example.eshopback.model.enums.NDSType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter@Setter
@AllArgsConstructor
@Builder
public class SupplyPositionResponse {
    private String product;
    private NDSType ndsType;
    private int amount;
    @Builder.Default
    private double price = 0;
}
