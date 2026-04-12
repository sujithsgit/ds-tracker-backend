package com.example.drtraker.demo.controller;

import com.example.drtraker.demo.dto.DashboardResponse;
import com.example.drtraker.demo.security.JwtUtil;
import com.example.drtraker.demo.service.DashboardService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/dashboard")
public class DashboardController {

    private final DashboardService dashboardService;
    private final JwtUtil jwtUtil;

    public DashboardController(DashboardService dashboardService, JwtUtil jwtUtil) {
        this.dashboardService = dashboardService;
        this.jwtUtil = jwtUtil;
    }

    @GetMapping
    public DashboardResponse getDashboard(
            @RequestHeader("Authorization") String token) {
        String email = jwtUtil.extractUsername(token.substring(7));
        return dashboardService.getDashboard(email);
    }
}