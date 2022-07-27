package com.example.eshopback.service;

import com.example.eshopback.model.request.OrderRequest;

public interface OrderService {
    void createOrder(OrderRequest orderRequest);
}
