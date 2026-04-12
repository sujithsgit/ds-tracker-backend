package com.example.drtraker.demo.service;


import com.example.drtraker.demo.dto.CheckinRequest;
import com.example.drtraker.demo.entity.Resolution;
import com.example.drtraker.demo.entity.ResolutionLog;
import com.example.drtraker.demo.repository.ResolutionLogRepository;
import com.example.drtraker.demo.repository.ResolutionRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
public class CheckInService {

    private final ResolutionRepository resolutionRepository;
    private final ResolutionLogRepository logRepository;

    public CheckInService(ResolutionRepository resolutionRepository,
                          ResolutionLogRepository logRepository) {
        this.resolutionRepository = resolutionRepository;
        this.logRepository = logRepository;
    }

    public String checkIn(Long resolutionId, CheckinRequest request) {

     
        Resolution resolution = resolutionRepository.findById(resolutionId)
                .orElseThrow(() -> new RuntimeException("Resolution not found"));

        LocalDate today = LocalDate.now();

   
        ResolutionLog log = logRepository
                .findByResolutionAndDate(resolution, today)
                .orElseThrow(() -> new RuntimeException("Log not found for today"));

      
        if ("DONE".equals(log.getStatus()) || "SKIPPED".equals(log.getStatus())) {
            return "Already checked in for today";
        }


        log.setStatus(request.getStatus()); // DONE or SKIPPED
        logRepository.save(log);

        return "Check-in successful";
    }
}