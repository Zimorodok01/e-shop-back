package com.example.eshopback.service.impl;

import com.example.eshopback.model.entity.WriteOff;
import com.example.eshopback.repository.WriteOffRepository;
import com.example.eshopback.service.WriteOffService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class WriteOffServiceImpl implements WriteOffService {
    private final WriteOffRepository writeOffRepository;

    @Override
    public void save(WriteOff writeOff) {
        writeOffRepository.save(writeOff);
    }
}
