package com.example.eshopback.model.response;

import com.example.eshopback.model.entity.Product;
import com.example.eshopback.model.entity.SalesPoint;
import com.example.eshopback.model.entity.Supplier;
import com.example.eshopback.model.enums.DocumentType;
import com.example.eshopback.model.enums.NDSType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter@Setter
@AllArgsConstructor
@Builder
public class SupplyResponse {
    private Long id;
    private String supplier;
    private DocumentType documentType;
    private String product;
    private NDSType ndsType;
    private int amount;
    @Builder.Default
    private double price = 0;
}
