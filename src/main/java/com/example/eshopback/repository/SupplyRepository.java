package com.example.eshopback.repository;

import com.example.eshopback.model.entity.SalesPoint;
import com.example.eshopback.model.entity.Supply;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SupplyRepository extends JpaRepository<Supply, Long> {
    List<Supply> findBySalesPointAndDeletedAtNull(SalesPoint salesPoint);

}
