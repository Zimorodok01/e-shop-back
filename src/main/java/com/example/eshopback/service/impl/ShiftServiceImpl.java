package com.example.eshopback.service.impl;

import com.example.eshopback.model.entity.SalesPoint;
import com.example.eshopback.repository.ShfitRepository;
import com.example.eshopback.service.ShiftService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class ShiftServiceImpl implements ShiftService {
    private final ShfitRepository shfitRepository;

    @Override
    public boolean isOpened(SalesPoint salesPoint) {
        return shfitRepository.findBySalesPointAndClosedAtNull(salesPoint).isPresent();
    }
}
