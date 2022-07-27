package com.example.eshopback.service.impl;

import com.example.eshopback.model.entity.Order;
import com.example.eshopback.model.entity.Position;
import com.example.eshopback.model.entity.Product;
import com.example.eshopback.model.entity.SalesPoint;
import com.example.eshopback.model.request.OrderRequest;
import com.example.eshopback.model.request.PositionRequest;
import com.example.eshopback.repository.OrderRepository;
import com.example.eshopback.repository.PositionRepository;
import com.example.eshopback.service.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {
    private final OrderRepository orderRepository;
    private final SalesPointService salesPointService;
    private final UserService userService;
    private final RemainService remainService;
    private final ProductService productService;
    private final PositionRepository positionRepository;

    @Override
    public void createOrder(OrderRequest orderRequest) {
        //check product remains
        List<PositionRequest> products = orderRequest.getProducts();
        SalesPoint salesPoint = salesPointService.getSalesPoint(orderRequest.getSalesPoint());
        remainService.checkOrder(products, salesPoint);

        Order order = Order.builder()
                .salesPoint(salesPoint)
                .clientName(orderRequest.getClientName())
                .cashier(userService.getUserById(orderRequest.getCashier()))
                .sum(orderRequest.getSum())
                .paymentSum(orderRequest.getPaymentSum())
                .paymentType(orderRequest.getPaymentType())
                .build();

        orderRepository.save(order);

        List<Product> productList = new ArrayList<>();
        for (PositionRequest positionRequest : products) {
            Product product = productService.getProduct(positionRequest.getProduct());
            Position position = Position.builder()
                    .order(order)
                    .amount(positionRequest.getAmount())
                    .product(product)
                    .build();
            positionRepository.save(position);
            productList.add(product);
        }

        //Write off
        remainService.writeOff(products, salesPoint);
    }
}
