package com.example.eshopback.controller;

import com.example.eshopback.model.request.SupplyRequest;
import com.example.eshopback.service.SupplyService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.http.ResponseEntity.ok;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/supplies")
public class SupplyController {
    private final SupplyService supplyService;

    @PostMapping
    @PreAuthorize("hasAuthority('SUPPLY_WRITE')")
    public ResponseEntity<?> supplyProducts(@RequestBody SupplyRequest supplyRequest) {
        supplyService.addSupply(supplyRequest);
        return ok("Приход продукта добавлен");
    }
}
