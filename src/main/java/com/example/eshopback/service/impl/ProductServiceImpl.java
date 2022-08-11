package com.example.eshopback.service.impl;

import com.example.eshopback.model.entity.Product;
import com.example.eshopback.model.exception.ErrorException;
import com.example.eshopback.model.request.ProductRequest;
import com.example.eshopback.repository.ProductRepository;
import com.example.eshopback.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {
    private final ProductRepository productRepository;

    private static final String PRODUCT_DOES_NOT_EXIST = "Продукт %d не существует";

    @Override
    public Product addProduct(ProductRequest productRequest) {
        Product product = Product.builder()
                .name(productRequest.getProductName())
                .type(productRequest.getProductType())
                .price(productRequest.getPrice())
                .url(productRequest.getUrl())
                .build();

        return productRepository.save(product);
    }

    @Override
    public List<Product> getProducts() {
        return productRepository.findAll();
    }

    @Override
    public Product getProduct(Long productId) {
        return productRepository.findById(productId).orElseThrow(
                () -> ErrorException.builder()
                        .status(HttpStatus.BAD_REQUEST)
                        .message(PRODUCT_DOES_NOT_EXIST)
                        .build()
        );
    }

    @Override
    public List<Product> getProductsByName(String productName) {
        return productRepository.findByNameAndDeletedAtNull(productName);
    }
}
