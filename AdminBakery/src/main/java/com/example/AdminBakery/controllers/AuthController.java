package com.example.AdminBakery.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.example.AdminBakery.dto.AuthRequest;
import com.example.AdminBakery.dto.UserProfileResponse;
import com.example.AdminBakery.security.AuthUserDetails;
import com.example.AdminBakery.services.AuthService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;
    private final ObjectMapper objectMapper;

    public AuthController(AuthService authService, ObjectMapper objectMapper) {
        this.authService = authService;
        this.objectMapper = objectMapper;
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody Map<String, Object> payload) {
        try {
            AuthRequest request = extractAuthRequest(payload);
            return ResponseEntity.status(HttpStatus.CREATED).body(authService.register(request));
        } catch (IllegalArgumentException exception) {
            return ResponseEntity.badRequest().body(exception.getMessage());
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, Object> payload) {
        try {
            AuthRequest request = extractAuthRequest(payload);
            return ResponseEntity.ok(authService.login(request));
        } catch (BadCredentialsException | IllegalArgumentException exception) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(exception.getMessage());
        }
    }

    @GetMapping("/me")
    public ResponseEntity<UserProfileResponse> me(@AuthenticationPrincipal AuthUserDetails userDetails) {
        return ResponseEntity.ok(authService.getCurrentUser(userDetails));
    }

    private AuthRequest extractAuthRequest(Map<String, Object> payload) {
        Object nestedAuth = payload.get("auth");
        if (nestedAuth instanceof Map<?, ?> nestedPayload) {
            return objectMapper.convertValue(nestedPayload, AuthRequest.class);
        }
        return objectMapper.convertValue(payload, AuthRequest.class);
    }
}
