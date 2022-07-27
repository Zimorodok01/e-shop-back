package com.example.eshopback.service.impl;

import com.example.eshopback.model.entity.Product;
import com.example.eshopback.model.entity.Remain;
import com.example.eshopback.model.entity.SalesPoint;
import com.example.eshopback.model.entity.Supply;
import com.example.eshopback.model.request.SupplyPositionRequest;
import com.example.eshopback.model.request.SupplyRequest;
import com.example.eshopback.repository.SupplyRepository;
import com.example.eshopback.service.ProductService;
import com.example.eshopback.service.RemainService;
import com.example.eshopback.service.SalesPointService;
import com.example.eshopback.service.SupplyService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SupplyServiceImpl implements SupplyService {
    private final SupplyRepository supplyRepository;
    private final ProductService productService;
    private final SalesPointService salesPointService;
    private final RemainService remainService;

    @Override
    public void addSupply(SupplyRequest supplyRequest) {
        SalesPoint salesPoint = salesPointService.getSalesPoint(supplyRequest.getSalesPoint());
        for (SupplyPositionRequest position : supplyRequest.getPositions()) {
            Product product = productService.getProduct(position.getProduct());
            Supply supply = Supply.builder()
                    .price(position.getPrice())
                    .product(product)
                    .amount(position.getAmount())
                    .salesPoint(salesPoint)
                    .build();
            supplyRepository.save(supply);

            remainService.getRemainBySalesPointAndProduct(supply);
        }
    }
}
