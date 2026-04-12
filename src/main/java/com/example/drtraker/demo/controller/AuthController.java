package com.example.drtraker.demo.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.example.drtraker.demo.dto.*;
import com.example.drtraker.demo.entity.User;
import com.example.drtraker.demo.security.*;
import com.example.drtraker.demo.service.*;
import com.example.drtraker.demo.repository.*;
import java.util.*;

@RestController
@RequestMapping("/auth")
@CrossOrigin
public class AuthController {
    private final AuthService authService;
    private final JwtUtil jwtUtil;
    private final UserRepository userRepository; // ✅ add

    public AuthController(AuthService authService, JwtUtil jwtUtil,
                          UserRepository userRepository) { // ✅ add
        this.authService = authService;
        this.jwtUtil = jwtUtil;
        this.userRepository = userRepository; // ✅ add
    }

    @PostMapping("/login")
    public AuthResponse login(@RequestBody LoginRequest request) {
        return authService.login(request);
    }

    // ✅ Refresh token validate பண்ணி new access token தரு
    @PostMapping("/refresh")
    public ResponseEntity<?> refresh(@RequestBody Map<String, String> body) {
        try {
            String refreshToken = body.get("refreshToken");

            // ✅ Token validate பண்ணு
            if (!jwtUtil.validateToken(refreshToken)) {
                return ResponseEntity.status(401).body("Refresh token expired");
            }

            String email = jwtUtil.extractUsername(refreshToken);

            // ✅ DB-ல refresh token match பண்ணு
            User user = userRepository.findByEmail(email)
                    .orElseThrow(() -> new RuntimeException("User not found"));

            if (!refreshToken.equals(user.getRefreshToken())) {
                return ResponseEntity.status(401).body("Invalid refresh token");
            }

            // ✅ New access token generate பண்ணு
            String newAccessToken = jwtUtil.generateAccessToken(email);

            return ResponseEntity.ok(new AuthResponse(newAccessToken, refreshToken));

        } catch (Exception e) {
            return ResponseEntity.status(401).body("Refresh token invalid");
        }
    }

    @PostMapping("/register")
    public String register(@RequestBody RegisterRequest request) {
        return authService.register(request);
    }

    @PostMapping("/verify-otp")
    public String verifyOtp(@RequestBody OtpVerifyRequest request) {
        return authService.verifyOtp(request);
    }
}