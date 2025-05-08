package org.polina.practice.controller;

import lombok.RequiredArgsConstructor;
import org.polina.practice.dto.RefreshTokenRequest;
import org.polina.practice.utils.JWTUtils;
import org.polina.practice.service.impl.OurUserDetailedService;
import org.polina.practice.dto.AuthRequest;
import org.polina.practice.dto.AuthResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthenticationManager authenticationManager;
    private final JWTUtils jwtUtils;
    private final OurUserDetailedService ourUserDetailedService;

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody AuthRequest authRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(authRequest.getUsername(), authRequest.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);

        UserDetails userDetails = ourUserDetailedService.loadUserByUsername(authRequest.getUsername());
        String jwt = jwtUtils.generateToken(userDetails);

        HashMap<String, Object> claims = new HashMap<>();
        claims.put("roles", userDetails.getAuthorities());

        String refreshToken = jwtUtils.generateRefreshToken(claims, userDetails);

        return ResponseEntity.ok(new AuthResponse(jwt, refreshToken));
    }

    @PostMapping("/refresh-token")
    public ResponseEntity<AuthResponse> refreshToken(@RequestBody RefreshTokenRequest refreshToken) {
        String username = jwtUtils.extractUsername(refreshToken.getRefreshToken());
        UserDetails userDetails = ourUserDetailedService.loadUserByUsername(username);

        if (jwtUtils.isTokenValid(refreshToken.getRefreshToken(), userDetails)) {
            String newJwt = jwtUtils.generateToken(userDetails);
            return ResponseEntity.ok(new AuthResponse(newJwt, refreshToken.getRefreshToken()));
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new AuthResponse("Invalid refresh token", null));
        }
    }
}
