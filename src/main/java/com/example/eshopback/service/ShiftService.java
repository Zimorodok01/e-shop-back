package com.example.eshopback.service;

import com.example.eshopback.model.entity.SalesPoint;

public interface ShiftService {

    boolean getStatus(Long salesPointId);

    void openShift(Long salesPointId);

    void closeShift(Long salesPointId);
}
