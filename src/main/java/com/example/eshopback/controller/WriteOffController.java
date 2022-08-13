package com.example.eshopback.controller;

import com.example.eshopback.model.request.WriteOffRequest;
import com.example.eshopback.model.response.SuccessResponse;
import com.example.eshopback.service.WriteOffService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.http.ResponseEntity.ok;

@RestController
@RequestMapping("/api/v1/writeOff")
@RequiredArgsConstructor
public class WriteOffController {
    private final WriteOffService writeOffService;

    @PostMapping
    @PreAuthorize("hasAuthority('ROLE_MANAGER')")
    public ResponseEntity<?> writeOff(@RequestBody List<WriteOffRequest> writeOffRequests, @RequestParam Long salesPoint) {
        writeOffService.writeOff(writeOffRequests, salesPoint);
        return ok(new SuccessResponse("Товары списались"));
    }

    @GetMapping
    @PreAuthorize("hasAuthority('ROLE_MANAGER')")
    public ResponseEntity<?> getWriteOffs(@RequestParam Long salesPoint) {
        return ok(writeOffService.getWriteOffs(salesPoint));
    }
}
