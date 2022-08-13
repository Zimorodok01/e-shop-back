package com.example.eshopback.repository;

import com.example.eshopback.model.entity.SalesPoint;
import com.example.eshopback.model.entity.WriteOff;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface WriteOffRepository extends JpaRepository<WriteOff, Long> {
    List<WriteOff> findBySalesPointAndDeletedAtNull(SalesPoint salesPoint);

}
