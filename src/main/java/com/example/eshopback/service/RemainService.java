package com.example.eshopback.service;

import com.example.eshopback.model.entity.Product;
import com.example.eshopback.model.entity.SalesPoint;
import com.example.eshopback.model.entity.Supply;
import com.example.eshopback.model.request.PositionRequest;
import com.example.eshopback.model.response.RemainResponse;

import java.util.List;

public interface RemainService {
    void checkOrder(List<PositionRequest> positions, SalesPoint salesPoint);

    void writeOffOrder(List<PositionRequest> products, SalesPoint salesPoint);

    void addProduct(Product product);

    void getRemainBySalesPointAndProduct(Supply supply);

    List<RemainResponse> getRemains(Long salesPointId);

    List<RemainResponse> getRemainsByProductName(String productName, Long salesPoint);

    void writeOff(Product product, SalesPoint salesPoint, int amount);
}
