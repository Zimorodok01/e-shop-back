package com.example.eshopback.service;

import com.example.eshopback.model.entity.SalesPoint;
import com.example.eshopback.model.request.SalesPointRequest;

import java.util.List;

public interface SalesPointService {
    void createSalePoint(SalesPointRequest salesPointRequest);

    List<SalesPoint> getSalesPoints();

    SalesPoint getSalesPoint(Long salesPoint);
}
