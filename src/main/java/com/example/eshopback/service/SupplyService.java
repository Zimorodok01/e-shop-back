package com.example.eshopback.service;

import com.example.eshopback.model.entity.Supplier;
import com.example.eshopback.model.request.SupplierRequest;
import com.example.eshopback.model.request.SupplyRequest;
import com.example.eshopback.model.response.SupplyResponse;

import java.util.List;

public interface SupplyService {
    void addSupply(SupplyRequest supplyRequest);

    void addSupplier(SupplierRequest supplierRequest);

    List<Supplier> getSuppliers();

    List<SupplyResponse> getSupplies(Long salesPoint);
}
