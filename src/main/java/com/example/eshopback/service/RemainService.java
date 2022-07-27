package com.example.eshopback.service;

import com.example.eshopback.model.entity.Product;
import com.example.eshopback.model.entity.SalesPoint;
import com.example.eshopback.model.entity.Supply;
import com.example.eshopback.model.request.PositionRequest;

import java.util.List;

public interface RemainService {
    void checkOrder(List<PositionRequest> positions, SalesPoint salesPoint);

    void writeOff(List<PositionRequest> products, SalesPoint salesPoint);

    void addProduct(Product product);

    void getRemainBySalesPointAndProduct(Supply supply);
}