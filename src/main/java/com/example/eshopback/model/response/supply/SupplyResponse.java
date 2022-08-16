package com.example.eshopback.model.response.supply;

import com.example.eshopback.model.enums.DocumentType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter@Setter
@AllArgsConstructor
@Builder
public class SupplyResponse {
    private Long id;
    private String supplier;
    private DocumentType documentType;
    private List<SupplyPositionResponse> positions;
}
