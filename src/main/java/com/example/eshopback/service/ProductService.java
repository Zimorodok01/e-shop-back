package com.example.eshopback.service;

import com.example.eshopback.model.entity.Product;
import com.example.eshopback.model.request.ProductRequest;

import java.util.List;

public interface ProductService {
    Product addProduct(ProductRequest productRequest);

    List<Product> getProducts();

    Product getProduct(Long product);
}
