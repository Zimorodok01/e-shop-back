package com.example.eshopback.controller;

import com.example.eshopback.model.response.SuccessResponse;
import com.example.eshopback.service.ShiftService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.ResponseEntity.ok;

@RestController
@RequestMapping("/api/v1/shifts")
@RequiredArgsConstructor
public class ShiftController {
    private final ShiftService shiftService;

    @GetMapping("/status")
    @PreAuthorize("hasAuthority('ROLE_MANAGER')")
    public ResponseEntity<?> shiftStatus(@RequestParam Long salesPoint) {
        return ok(shiftService.getStatus(salesPoint));
    }


    @PostMapping("/{salesPoint}")
    @PreAuthorize("hasAuthority('ROLE_MANAGER')")
    public ResponseEntity<?> openShift(@PathVariable Long salesPoint) {
        shiftService.openShift(salesPoint);
        return ok(new SuccessResponse("Смена закрыта"));
    }

    @PutMapping("/{salesPoint}")
    @PreAuthorize("hasAuthority('ROLE_MANAGER')")
    public ResponseEntity<?> closeShift(@PathVariable Long salesPoint) {
        shiftService.closeShift(salesPoint);
        return ok(new SuccessResponse("Смена закрыта"));
    }
}
