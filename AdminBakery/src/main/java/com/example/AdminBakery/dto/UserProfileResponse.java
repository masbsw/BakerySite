package com.example.AdminBakery.dto;

import com.example.AdminBakery.models.Role;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UserProfileResponse {
    private Long userId;
    private String username;
    private Role role;
}
