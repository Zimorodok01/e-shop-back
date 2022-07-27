package com.example.eshopback.repository;

import com.example.eshopback.model.entity.SalesPoint;
import com.example.eshopback.model.entity.Shift;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ShfitRepository extends JpaRepository<Shift, Long> {
    Optional<Shift> findBySalesPointAndClosedAtNull(SalesPoint salesPoint);

}
