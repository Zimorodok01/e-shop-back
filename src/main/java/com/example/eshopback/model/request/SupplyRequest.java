package com.example.eshopback.model.request;

import com.example.eshopback.model.enums.DocumentType;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter@Setter
public class SupplyRequest {
    private Long salesPoint;
    private Long supplier;
    private DocumentType documentType;
    private List<SupplyPositionRequest> positions;
}