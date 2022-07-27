package com.example.eshopback.model.request;

import com.example.eshopback.model.enums.PaymentType;
import lombok.Getter;
import lombok.Setter;
import java.util.List;

@Getter@Setter
public class OrderRequest {
    private Long salesPoint;
    private String clientName;
    private Long cashier;
    private double sum;
    private double paymentSum;
    private PaymentType paymentType;
    private List<PositionRequest> products;
}
