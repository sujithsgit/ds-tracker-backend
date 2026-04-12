package com.example.drtraker.demo.service;

import com.example.drtraker.demo.dto.DashboardResponse;
import com.example.drtraker.demo.entity.Resolution;
import com.example.drtraker.demo.entity.ResolutionLog;
import com.example.drtraker.demo.entity.User;
import com.example.drtraker.demo.repository.ResolutionLogRepository;
import com.example.drtraker.demo.repository.ResolutionRepository;
import com.example.drtraker.demo.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class DashboardService {

    private final UserRepository userRepository;
    private final ResolutionRepository resolutionRepository;
    private final ResolutionLogRepository logRepository;

    public DashboardService(UserRepository userRepository,
                            ResolutionRepository resolutionRepository,
                            ResolutionLogRepository logRepository) {
        this.userRepository = userRepository;
        this.resolutionRepository = resolutionRepository;
        this.logRepository = logRepository;
    }

    public DashboardResponse getDashboard(String email) {

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        List<Resolution> resolutions = resolutionRepository.findByUserOrderByCreatedAtDesc(user);

        if (resolutions.isEmpty()) {
            DashboardResponse empty = new DashboardResponse();
            empty.setUserName(user.getName());
            empty.setResolutions(new ArrayList<>());
            empty.setCalendarLogs(new ArrayList<>());
            return empty;
        }

        LocalDate today = LocalDate.now();

      
        Map<Long, List<ResolutionLog>> logsCache = new HashMap<>();
        for (Resolution res : resolutions) {
            logsCache.put(res.getId(), logRepository.findByResolutionOrderByDateAsc(res));
        }

        // Latest resolution — main card
        Resolution latest = resolutions.get(0);
        List<ResolutionLog> latestLogs = logsCache.get(latest.getId());

        int daysCompleted = calculateDaysCompleted(latestLogs);
        int daysRemaining = (int) Math.max(0, ChronoUnit.DAYS.between(today, latest.getEndDate()));
        String todayStatus = getTodayStatus(latestLogs, today);
        int streak = calculateStreak(latestLogs, today, todayStatus);
        int bestStreak = calculateBestStreak(latestLogs);

        List<DashboardResponse.LogDto> logDtos = latestLogs.stream()
                .map(l -> new DashboardResponse.LogDto(l.getDate().toString(), l.getStatus()))
                .collect(Collectors.toList());

        // All resolutions — modal
        List<DashboardResponse.ResolutionDto> resolutionDtos = resolutions.stream().map(res -> {
            List<ResolutionLog> resLogs = logsCache.get(res.getId());

            DashboardResponse.ResolutionDto dto = new DashboardResponse.ResolutionDto();
            dto.setId(res.getId());
            dto.setTitle(res.getTitle());
            dto.setDurationDays(res.getDurationDays());
            dto.setDaysCompleted(calculateDaysCompleted(resLogs));
            dto.setDaysRemaining((int) Math.max(0, ChronoUnit.DAYS.between(today, res.getEndDate())));
            dto.setTodayStatus(getTodayStatus(resLogs, today));
            dto.setStreak(calculateStreak(resLogs, today, getTodayStatus(resLogs, today)));

            return dto;
        }).collect(Collectors.toList());

        // ✅ Calendar — date → statuses + resolution breakdown
        Map<String, List<String>> dateStatusMap = new LinkedHashMap<>();
        Map<String, List<DashboardResponse.CalendarResolutionDto>> dateResolutionMap = new LinkedHashMap<>();

        for (Resolution res : resolutions) {
            List<ResolutionLog> resLogs = logsCache.get(res.getId());
            for (ResolutionLog log : resLogs) {
                String date = log.getDate().toString();
                dateStatusMap.computeIfAbsent(date, k -> new ArrayList<>()).add(log.getStatus());
                dateResolutionMap.computeIfAbsent(date, k -> new ArrayList<>())
                    .add(new DashboardResponse.CalendarResolutionDto(res.getTitle(), log.getStatus()));
            }
        }

        List<DashboardResponse.CalendarDto> calendarDtos = dateStatusMap.entrySet().stream()
            .map(entry -> {
                String date = entry.getKey();
                List<String> statuses = entry.getValue();

                boolean anyPending = statuses.stream().anyMatch(s -> "PENDING".equals(s));
                boolean allDone = statuses.stream().allMatch(s -> "DONE".equals(s));
                boolean allSkipped = statuses.stream().allMatch(s -> "SKIPPED".equals(s));

                String combinedStatus;
                if (anyPending) combinedStatus = "PENDING";
                else if (allDone) combinedStatus = "DONE";
                else if (allSkipped) combinedStatus = "SKIPPED";
                else combinedStatus = "PARTIAL";

                List<DashboardResponse.CalendarResolutionDto> dayResolutions =
                    dateResolutionMap.getOrDefault(date, new ArrayList<>());

                return new DashboardResponse.CalendarDto(date, combinedStatus, dayResolutions);
            })
            .sorted(Comparator.comparing(DashboardResponse.CalendarDto::getDate))
            .collect(Collectors.toList());

        // Build response
        DashboardResponse response = new DashboardResponse();
        response.setUserName(user.getName());
        response.setResolutionId(latest.getId());
        response.setResolutionTitle(latest.getTitle());
        response.setDurationDays(latest.getDurationDays());
        response.setStartDate(latest.getStartDate().toString());
        response.setEndDate(latest.getEndDate().toString());
        response.setDaysCompleted(daysCompleted);
        response.setDaysRemaining(daysRemaining);
        response.setTodayStatus(todayStatus);
        response.setStreak(streak);
        response.setBestStreak(bestStreak);
        response.setLogs(logDtos);
        response.setResolutions(resolutionDtos);
        response.setCalendarLogs(calendarDtos); // ✅

        return response;
    }

    private int calculateDaysCompleted(List<ResolutionLog> logs) {
        return (int) logs.stream().filter(l -> "DONE".equals(l.getStatus())).count();
    }

    private String getTodayStatus(List<ResolutionLog> logs, LocalDate today) {
        return logs.stream()
                .filter(l -> l.getDate().equals(today))
                .map(ResolutionLog::getStatus)
                .findFirst()
                .orElse("PENDING");
    }

    private int calculateStreak(List<ResolutionLog> logs, LocalDate today, String todayStatus) {
        int streak = 0;
        LocalDate checkDate = today.minusDays(1);
        for (int i = logs.size() - 1; i >= 0; i--) {
            ResolutionLog log = logs.get(i);
            if (log.getDate().equals(checkDate) && "DONE".equals(log.getStatus())) {
                streak++;
                checkDate = checkDate.minusDays(1);
            } else if (log.getDate().equals(checkDate)) {
                break;
            }
        }
        if ("DONE".equals(todayStatus)) streak++;
        return streak;
    }

    private int calculateBestStreak(List<ResolutionLog> logs) {
        int best = 0, current = 0;
        for (ResolutionLog log : logs) {
            if ("DONE".equals(log.getStatus())) {
                current++;
                best = Math.max(best, current);
            } else {
                current = 0;
            }
        }
        return best;
    }
}