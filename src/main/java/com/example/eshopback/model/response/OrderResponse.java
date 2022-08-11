package com.example.eshopback.model.response;

import com.example.eshopback.model.enums.PaymentType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter@Setter
@AllArgsConstructor
@Builder
public class OrderResponse {
    private Long id;
    private Long salesPoint;
    private String clientName;
    private String cashier;
    private double sum = 0.0;
    private double paymentSum;
    private PaymentType paymentType;
    private List<PositionResponse> positions;
}
