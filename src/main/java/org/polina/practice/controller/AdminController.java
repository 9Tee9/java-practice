package org.polina.practice.controller;

import lombok.RequiredArgsConstructor;
import org.polina.practice.service.impl.OurUserDetailedService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
public class AdminController {
    private final OurUserDetailedService ourUserDetailedService;

    @PatchMapping("/unlock-user/{userId}")
    @PreAuthorize("hasRole('SUPER_ADMIN')")
    public ResponseEntity<String> unlockUser(@PathVariable Long userId) {
        ourUserDetailedService.unlockUser(userId);
        return ResponseEntity.ok("User account unlocked successfully.");
    }
}

