package com.example.eshopback.model.request;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter@Setter
public class SupplyRequest {
    private Long salesPoint;
    private List<SupplyPositionRequest> positions;
}