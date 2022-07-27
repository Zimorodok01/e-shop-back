package com.example.eshopback.repository;

import com.example.eshopback.model.entity.Product;
import com.example.eshopback.model.entity.Remain;
import com.example.eshopback.model.entity.SalesPoint;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RemainRepository extends JpaRepository<Remain, Long> {
    Optional<Remain> findBySalesPointAndProduct(SalesPoint salesPoint, Product product);

}
