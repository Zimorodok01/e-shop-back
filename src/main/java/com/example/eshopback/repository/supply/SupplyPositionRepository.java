package com.example.eshopback.repository.supply;

import com.example.eshopback.model.entity.Supply;
import com.example.eshopback.model.entity.SupplyPosition;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SupplyPositionRepository extends JpaRepository<SupplyPosition, Long> {
    List<SupplyPosition> findBySupplyAndDeletedAtNull(Supply supply);

}
