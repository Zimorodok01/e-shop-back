package com.example.eshopback.model.request;

import com.example.eshopback.model.enums.SupplierType;
import lombok.*;

@Getter@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SupplierRequest {
    private String name;
    private String phone;
    private String email;
    private String contact;
    private SupplierType type;
    private String comments;
}
