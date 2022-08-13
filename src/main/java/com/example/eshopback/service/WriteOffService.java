package com.example.eshopback.service;

import com.example.eshopback.model.entity.WriteOffOrder;
import com.example.eshopback.model.request.WriteOffRequest;
import com.example.eshopback.model.response.WriteOffResponse;

import java.util.List;

public interface WriteOffService {
    void writeOff(List<WriteOffRequest> writeOffRequests, Long salesPoint);

    List<WriteOffResponse> getWriteOffs(Long salesPoint);
}
