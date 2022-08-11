package com.example.eshopback.service;

import com.example.eshopback.model.request.OrderRequest;
import com.example.eshopback.model.response.OrderResponse;
import com.example.eshopback.model.response.RevenueResponse;

import java.util.List;

//Интерфейсы используется для того чтобы снизить зависимости меж классов
public interface OrderService {
    void createOrder(OrderRequest orderRequest);

    List<OrderResponse> getLast(Long salesPointId);

    RevenueResponse getRevenues(Long salesPoint);
}
