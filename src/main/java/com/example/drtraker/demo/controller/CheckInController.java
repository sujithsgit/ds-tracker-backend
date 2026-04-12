package com.example.drtraker.demo.controller;

import com.example.drtraker.demo.dto.*;
import com.example.drtraker.demo.security.JwtUtil;
import com.example.drtraker.demo.service.CheckInService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/checkin")
public class CheckInController {

    private final CheckInService checkInService;
    private final JwtUtil jwtUtil;

    public CheckInController(CheckInService checkInService, JwtUtil jwtUtil) {
        this.checkInService = checkInService;
        this.jwtUtil = jwtUtil;
    }

    @PutMapping("/{resolutionId}")
    public String checkIn(@PathVariable Long resolutionId,
                          @RequestBody CheckinRequest request,
                          @RequestHeader("Authorization") String token) {
        return checkInService.checkIn(resolutionId, request);
    }
}