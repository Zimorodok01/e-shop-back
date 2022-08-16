package com.example.eshopback.model.response;

import com.example.eshopback.model.enums.PaymentType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter@Setter
@AllArgsConstructor
@Builder
public class OrderReportResponse {
    private Long orderId;
    private PaymentType paymentType;
    private double orderSum;
}
