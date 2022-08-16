package com.example.eshopback.controller;

import com.example.eshopback.model.enums.PaymentType;
import com.example.eshopback.model.request.OrderRequest;
import com.example.eshopback.model.response.SuccessResponse;
import com.example.eshopback.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

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
        return new ResponseEntity<>(new SuccessResponse("Заказ принят"), HttpStatus.OK);
    }

    @GetMapping("/last")
    @PreAuthorize("hasAuthority('ORDER_READ')")
    public ResponseEntity<?> getLastOrders(@RequestParam Long salesPoint) {
        return ok(orderService.getLast(salesPoint));
    }

    @GetMapping("/revenue")
    @PreAuthorize("hasAuthority('ROLE_MANAGER')")
    public ResponseEntity<?> getRevenues(@RequestParam Long salesPoint) {
        return ok(orderService.getRevenues(salesPoint));
    }

    @GetMapping("/report")
    @PreAuthorize("hasAuthority('ROLE_MANAGER')")
    public ResponseEntity<?> getReports(@RequestParam Optional<String> date,
                                        @RequestParam Long salesPoint,
                                        @RequestParam Optional<PaymentType> paymentType) {
        return ok(orderService.getReports(date, salesPoint, paymentType));
    }
}
