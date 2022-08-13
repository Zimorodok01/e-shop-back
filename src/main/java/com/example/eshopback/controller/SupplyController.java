package com.example.eshopback.controller;

import com.example.eshopback.model.request.SupplierRequest;
import com.example.eshopback.model.request.SupplyRequest;
import com.example.eshopback.model.response.SuccessResponse;
import com.example.eshopback.service.SupplyService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.ResponseEntity.ok;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/supplies")
public class SupplyController {
    private final SupplyService supplyService;

    @PostMapping
    @PreAuthorize("hasAuthority('ROLE_MANAGER')")
    public ResponseEntity<?> supplyProducts(@RequestBody SupplyRequest supplyRequest) {
        supplyService.addSupply(supplyRequest);
        return ok(new SuccessResponse("Приход продукта добавлен"));
    }

    @GetMapping
    @PreAuthorize("hasAuthority('ROLE_MANAGER')")
    public ResponseEntity<?> getSupplies(@RequestParam Long salesPoint) {
        return ok(supplyService.getSupplies(salesPoint));
    }

    @PostMapping("/suppliers")
    @PreAuthorize("hasAuthority('ROLE_MANAGER')")
    public ResponseEntity<?> addSupplier(@RequestBody SupplierRequest supplierRequest) {
        supplyService.addSupplier(supplierRequest);
        return ok(new SuccessResponse("Поставщик добавлен"));
    }

    @GetMapping("/suppliers")
    @PreAuthorize("hasAuthority('ROLE_MANAGER')")
    public ResponseEntity<?> getSuppliers() {
        return ok(supplyService.getSuppliers());
    }
}
