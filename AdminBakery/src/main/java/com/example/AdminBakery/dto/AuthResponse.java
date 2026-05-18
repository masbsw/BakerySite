package com.example.AdminBakery.dto;

import com.example.AdminBakery.models.Role;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AuthResponse {
    private String token;
    private String tokenType;
    private String username;
    private Role role;
}
