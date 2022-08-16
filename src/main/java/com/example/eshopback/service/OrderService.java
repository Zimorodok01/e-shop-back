package com.example.eshopback.service;

import com.example.eshopback.model.enums.PaymentType;
import com.example.eshopback.model.request.OrderRequest;
import com.example.eshopback.model.response.OrderReportResponse;
import com.example.eshopback.model.response.OrderResponse;
import com.example.eshopback.model.response.RevenueResponse;

import java.util.List;
import java.util.Optional;

public interface OrderService {
    void createOrder(OrderRequest orderRequest);

    List<OrderResponse> getLast(Long salesPointId);

    RevenueResponse getRevenues(Long salesPoint);

    List<OrderReportResponse> getReports(Optional<String> dateOptional, Long salesPointId, Optional<PaymentType> paymentTypeOptional);

    OrderResponse getOrderView(Long orderId);
}
