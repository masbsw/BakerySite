package com.example.AdminBakery.services;

import com.example.AdminBakery.dto.AuthRequest;
import com.example.AdminBakery.dto.AuthResponse;
import com.example.AdminBakery.dto.UserProfileResponse;
import com.example.AdminBakery.models.AppUser;
import com.example.AdminBakery.models.Role;
import com.example.AdminBakery.repositories.AppUserRepository;
import com.example.AdminBakery.security.AuthUserDetails;
import com.example.AdminBakery.security.JwtService;
import com.example.AdminBakery.security.JpaUserDetailsService;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final AppUserRepository appUserRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final JpaUserDetailsService userDetailsService;

    @Value("${app.auth.admin.username}")
    private String adminUsername;

    @Value("${app.auth.admin.password}")
    private String adminPassword;

    public AuthService(
            AppUserRepository appUserRepository,
            PasswordEncoder passwordEncoder,
            JwtService jwtService,
            JpaUserDetailsService userDetailsService
    ) {
        this.appUserRepository = appUserRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
        this.userDetailsService = userDetailsService;
    }

    @PostConstruct
    public void seedAdminUser() {
        if (!appUserRepository.existsByUsername(adminUsername)) {
            AppUser admin = new AppUser();
            admin.setUsername(adminUsername);
            admin.setPasswordHash(passwordEncoder.encode(adminPassword));
            admin.setRole(Role.ADMIN);
            appUserRepository.save(admin);
        }
    }

    public AuthResponse register(AuthRequest request) {
        String username = normalizeUsername(request.getUsername());
        validateCredentials(username, request.getPassword());

        if (appUserRepository.existsByUsername(username)) {
            throw new IllegalArgumentException("Пользователь уже существует");
        }

        AppUser user = new AppUser();
        user.setUsername(username);
        user.setPasswordHash(passwordEncoder.encode(request.getPassword()));
        user.setRole(Role.USER);
        appUserRepository.save(user);

        AuthUserDetails userDetails = userDetailsService.loadUserByUsername(username);
        return buildAuthResponse(userDetails);
    }

    public AuthResponse login(AuthRequest request) {
        String username = normalizeUsername(request.getUsername());
        validateCredentials(username, request.getPassword());

        AuthUserDetails userDetails;
        try {
            userDetails = userDetailsService.loadUserByUsername(username);
        } catch (UsernameNotFoundException exception) {
            throw new BadCredentialsException("Неверный логин или пароль");
        }

        if (!passwordEncoder.matches(request.getPassword(), userDetails.getPassword())) {
            throw new BadCredentialsException("Неверный логин или пароль");
        }

        return buildAuthResponse(userDetails);
    }

    public UserProfileResponse getCurrentUser(AuthUserDetails userDetails) {
        return new UserProfileResponse(
                userDetails.getUserId(),
                userDetails.getUsername(),
                Role.valueOf(userDetails.getRole())
        );
    }

    private AuthResponse buildAuthResponse(AuthUserDetails userDetails) {
        return new AuthResponse(
                jwtService.generateToken(userDetails),
                "Bearer",
                userDetails.getUsername(),
                Role.valueOf(userDetails.getRole())
        );
    }

    private void validateCredentials(String username, String password) {
        if (username == null || username.isBlank() || password == null || password.isBlank()) {
            throw new IllegalArgumentException("Логин и пароль обязательны");
        }
    }

    private String normalizeUsername(String username) {
        return username == null ? null : username.trim().toLowerCase();
    }
}
