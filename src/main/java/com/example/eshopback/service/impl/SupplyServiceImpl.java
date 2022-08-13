package com.example.eshopback.service.impl;

import com.example.eshopback.model.entity.*;
import com.example.eshopback.model.exception.ErrorException;
import com.example.eshopback.model.request.SupplierRequest;
import com.example.eshopback.model.request.SupplyPositionRequest;
import com.example.eshopback.model.request.SupplyRequest;
import com.example.eshopback.model.response.SupplyResponse;
import com.example.eshopback.repository.SupplierRepository;
import com.example.eshopback.repository.SupplyRepository;
import com.example.eshopback.service.ProductService;
import com.example.eshopback.service.RemainService;
import com.example.eshopback.service.SalesPointService;
import com.example.eshopback.service.SupplyService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SupplyServiceImpl implements SupplyService {
    private final SupplyRepository supplyRepository;
    private final ProductService productService;
    private final SalesPointService salesPointService;
    private final RemainService remainService;
    private final SupplierRepository supplierRepository;

    @Override
    public void addSupply(SupplyRequest supplyRequest) {
        SalesPoint salesPoint = salesPointService.getSalesPoint(supplyRequest.getSalesPoint());
        Supplier supplier = supplierRepository.findById(supplyRequest.getSupplier()).orElseThrow(
                () -> ErrorException.builder()
                        .status(HttpStatus.BAD_REQUEST)
                        .message("Поставщик не найден")
                        .build()
        );
        for (SupplyPositionRequest position : supplyRequest.getPositions()) {
            Product product = productService.getProduct(position.getProduct());
            Supply supply = Supply.builder()
                    .price(position.getPrice())
                    .product(product)
                    .amount(position.getAmount())
                    .salesPoint(salesPoint)
                    .documentType(supplyRequest.getDocumentType())
                    .ndsType(position.getNdsType())
                    .supplier(supplier)
                    .build();
            supplyRepository.save(supply);

            remainService.getRemainBySalesPointAndProduct(supply);
        }
    }

    @Override
    public void addSupplier(SupplierRequest supplierRequest) {
        Supplier supplier = Supplier.builder()
                .name(supplierRequest.getName())
                .type(supplierRequest.getType())
                .comments(supplierRequest.getComments())
                .contact(supplierRequest.getContact())
                .email(supplierRequest.getEmail())
                .phone(supplierRequest.getPhone())
                .build();

        supplierRepository.save(supplier);
    }

    @Override
    public List<Supplier> getSuppliers() {
        return supplierRepository.findAll();
    }

    @Override
    public List<SupplyResponse> getSupplies(Long salesPointId) {
        SalesPoint salesPoint = salesPointService.getSalesPoint(salesPointId);

        return supplyRepository.findBySalesPointAndDeletedAtNull(salesPoint).parallelStream()
                .map(supply -> SupplyResponse.builder()
                        .id(supply.getId())
                        .ndsType(supply.getNdsType())
                        .supplier(supply.getSupplier().getName())
                        .product(supply.getProduct().getName())
                        .documentType(supply.getDocumentType())
                        .amount(supply.getAmount())
                        .build()).collect(Collectors.toList());
    }
}
