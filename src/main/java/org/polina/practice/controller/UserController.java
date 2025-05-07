package org.polina.practice.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class UserController {

        @GetMapping("/profile")
        @PreAuthorize("hasRole('USER')")
        public ResponseEntity<String> getProfile() {
            return ResponseEntity.ok("User profile");
        }

        @GetMapping("/moderate")
        @PreAuthorize("hasRole('MODERATOR')")
        public ResponseEntity<String> moderateContent() {
            return ResponseEntity.ok("Moderation content");
        }

        @GetMapping("/admin")
        @PreAuthorize("hasRole('SUPER_ADMIN')")
        public ResponseEntity<String> adminAccess() {
            return ResponseEntity.ok("Admin access");
        }
    }
