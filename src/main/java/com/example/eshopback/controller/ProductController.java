package com.example.eshopback.controller;

import com.example.eshopback.model.entity.Product;
import com.example.eshopback.model.request.ProductRequest;
import com.example.eshopback.model.response.ProductResponse;
import com.example.eshopback.model.response.SuccessResponse;
import com.example.eshopback.service.ProductService;
import com.example.eshopback.service.RemainService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/products")
public class ProductController {
    private final ProductService productService;
    private final ModelMapper modelMapper;
    private final RemainService remainService;

    @GetMapping
    @PreAuthorize("hasAuthority('PRODUCT_READ')")
    public ResponseEntity<?> getAllProducts() {
        List<Product> products = productService.getProducts();
        return ResponseEntity.ok(products.stream()
                .map(product -> modelMapper.map(product, ProductResponse.class)).collect(Collectors.toList()));
    }

    @PostMapping
    @PreAuthorize("hasAuthority('PRODUCT_WRITE')")
    public ResponseEntity<?> addProduct(@RequestBody ProductRequest productRequest) {
        Product product = productService.addProduct(productRequest);
        remainService.addProduct(product);
        return ResponseEntity.ok(new SuccessResponse("Продукт создан"));
    }
}
