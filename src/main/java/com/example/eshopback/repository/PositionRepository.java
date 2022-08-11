package com.example.eshopback.repository;

import com.example.eshopback.model.entity.Order;
import com.example.eshopback.model.entity.Position;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PositionRepository extends JpaRepository<Position, Long> {
    List<Position> findByOrder(Order order);

}
