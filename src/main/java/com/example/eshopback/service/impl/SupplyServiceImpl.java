package com.example.eshopback.service.impl;

import com.example.eshopback.model.entity.*;
import com.example.eshopback.model.exception.ErrorException;
import com.example.eshopback.model.request.SupplierRequest;
import com.example.eshopback.model.request.SupplyPositionRequest;
import com.example.eshopback.model.request.SupplyRequest;
import com.example.eshopback.model.response.supply.SupplyPositionResponse;
import com.example.eshopback.model.response.supply.SupplyResponse;
import com.example.eshopback.repository.supply.SupplierRepository;
import com.example.eshopback.repository.supply.SupplyPositionRepository;
import com.example.eshopback.repository.supply.SupplyRepository;
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
    private final SupplyPositionRepository supplyPositionRepository;

    @Override
    public void addSupply(SupplyRequest supplyRequest) {
        SalesPoint salesPoint = salesPointService.getSalesPoint(supplyRequest.getSalesPoint());
        Supplier supplier = supplierRepository.findById(supplyRequest.getSupplier()).orElseThrow(
                () -> ErrorException.builder()
                        .status(HttpStatus.BAD_REQUEST)
                        .message("Поставщик не найден")
                        .build()
        );
        Supply supply = Supply.builder()
                .salesPoint(salesPoint)
                .documentType(supplyRequest.getDocumentType())
                .supplier(supplier)
                .build();
        supply = supplyRepository.save(supply);
        for (SupplyPositionRequest position : supplyRequest.getPositions()) {
            Product product = productService.getProduct(position.getProduct());
            SupplyPosition supplyPosition = SupplyPosition.builder()
                    .price(position.getPrice())
                    .product(product)
                    .amount(position.getAmount())
                    .ndsType(position.getNdsType())
                    .supply(supply)
                    .build();
            supplyPositionRepository.save(supplyPosition);

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

        List<Supply> supplies = supplyRepository.findBySalesPointAndDeletedAtNull(salesPoint);


        return supplyRepository.findBySalesPointAndDeletedAtNull(salesPoint).parallelStream()
                .map(supply -> {
                            List<SupplyPositionResponse> positions = getPositions(supply);
                            return SupplyResponse.builder()
                                    .id(supply.getId())
                                    .supplier(supply.getSupplier().getName())
                                    .documentType(supply.getDocumentType())
                                    .positions(positions)
                                    .build();
                        }).collect(Collectors.toList());
    }

    private List<SupplyPositionResponse> getPositions(Supply supply) {
        return supplyPositionRepository.findBySupplyAndDeletedAtNull(supply).parallelStream()
                .map(supplyPosition -> SupplyPositionResponse.builder()
                        .price(supplyPosition.getPrice())
                        .ndsType(supplyPosition.getNdsType())
                        .product(supplyPosition.getProduct().getName())
                        .amount(supplyPosition.getAmount())
                        .build()).collect(Collectors.toList());
    }
}
