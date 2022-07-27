package com.example.eshopback.controller;

import com.example.eshopback.model.request.SalesPointRequest;
import com.example.eshopback.service.SalesPointService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/points")
public class SalesPointController {
    private final SalesPointService salesPointService;


    @PreAuthorize("hasAuthority('POINT_WRITE')")
    @PostMapping
    public ResponseEntity<?> createSalesPoint(@RequestBody SalesPointRequest salesPointRequest) {
        salesPointService.createSalePoint(salesPointRequest);
        return ResponseEntity.ok("Торговая точка создана");
    }
}
