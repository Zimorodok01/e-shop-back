package com.example.eshopback.controller;

import com.example.eshopback.model.request.OrderRequest;
import com.example.eshopback.service.OrderService;
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
@RequestMapping("/api/v1/orders")
public class OrderController {
    private final OrderService orderService;

    @PostMapping
    @PreAuthorize("hasAuthority('ORDER_CREATE')")
    public ResponseEntity<?> createOrder(@RequestBody OrderRequest orderRequest) {

        orderService.createOrder(orderRequest);
        return ok("Заказ принят");
    }
}
