package com.example.eshopback.controller;

import com.example.eshopback.service.RemainService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.http.ResponseEntity.ok;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/remains")
public class RemainController {
    private final RemainService remainService;

    @GetMapping
    @PreAuthorize("hasAuthority('ROLE_MANAGER')")
    public ResponseEntity<?> getRemains(@RequestParam Long salesPoint) {
        return ok(remainService.getRemains(salesPoint));
    }

    @GetMapping("/find")
    @PreAuthorize("hasAuthority('ROLE_MANAGER')")
    public ResponseEntity<?> getRemainByProduct(@RequestParam String productName,
                                                @RequestParam Long salesPoint) {
        return ok(remainService.getRemainsByProductName(productName, salesPoint));
    }
}
