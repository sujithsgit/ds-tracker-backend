package com.example.drtraker.demo.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.drtraker.demo.dto.ResolutionRequest;
import com.example.drtraker.demo.security.JwtUtil;
import com.example.drtraker.demo.service.ResolutionService;

@RestController
@RequestMapping("/resolution")
public class ResolutionController {
	private final ResolutionService resolutionService;
    private final JwtUtil jwtUtil;

    public ResolutionController(ResolutionService resolutionService, JwtUtil jwtUtil) {
        this.resolutionService = resolutionService;
        this.jwtUtil = jwtUtil;
    }

    @PostMapping("/create")
    public String createResolution(@RequestBody ResolutionRequest request,
                                   @RequestHeader("Authorization") String token) {

        String email = jwtUtil.extractUsername(token.substring(7));

        return resolutionService.createResolution(request, email);
    }

}
