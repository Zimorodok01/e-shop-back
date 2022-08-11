package com.example.eshopback.repository;

import com.example.eshopback.model.entity.Order;
import com.example.eshopback.model.entity.SalesPoint;
import com.example.eshopback.model.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Date;
import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {

    List<Order> findByCreatedAtAfterAndSalesPointAndDeletedAtNull(Date createdAt, SalesPoint salesPoint);

    List<Order> findBySalesPointAndCreatedAtBetweenAndDeletedAtNull(SalesPoint salesPoint, Date createdAtStart, Date createdAtEnd);

}
