package com.example.eshopback.repository;

import com.example.eshopback.model.entity.SalesPoint;
import com.example.eshopback.model.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);

    List<User> findBySalesPointAndDeletedAtNull(SalesPoint salesPoint);
}
