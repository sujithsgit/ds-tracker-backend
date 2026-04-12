package com.example.drtraker.demo.service;

import org.springframework.stereotype.Service;
import java.time.LocalDate;

import com.example.drtraker.demo.entity.*;
import com.example.drtraker.demo.repository.*;
import com.example.drtraker.demo.dto.*;

@Service
public class ResolutionService {

    private final ResolutionRepository resolutionRepository;
    private final ResolutionLogRepository logRepository;
    private final UserRepository userRepository;

    public ResolutionService(ResolutionRepository resolutionRepository,
                             ResolutionLogRepository logRepository,
                             UserRepository userRepository) {
        this.resolutionRepository = resolutionRepository;
        this.logRepository = logRepository;
        this.userRepository = userRepository;
    }

    public String createResolution(ResolutionRequest req, String email) {

        // 🔹 Get user
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // 🔹 Duration logic
        int duration;

        if ("30days".equals(req.getDurationType())) {
            duration = 30;
        } else {
            // custom flow
            duration = Integer.parseInt(req.getCustomValue());
        }

        // 🔹 Date convert
        LocalDate startDate = LocalDate.parse(req.getStartDate());

        // 🔹 Save resolution
        Resolution resolution = new Resolution();
        resolution.setTitle(req.getResolution());  // ✅ correct mapping
        resolution.setMotivation(req.getMotivation());
        resolution.setDurationDays(duration);
        resolution.setStartDate(startDate);

        LocalDate endDate = startDate.plusDays(duration);
        resolution.setEndDate(endDate);

        resolution.setReminderEnabled(req.isReminderEnabled());
        resolution.setReminderTime(req.getReminderTime());

        resolution.setUser(user);

        resolutionRepository.save(resolution);

        // 🔥 Create daily logs
        for (int i = 0; i < duration; i++) {
            ResolutionLog log = new ResolutionLog();
            log.setResolution(resolution);
            log.setDate(startDate.plusDays(i));
            log.setStatus("PENDING");

            logRepository.save(log);
        }

        return "Resolution created successfully";
    }
}