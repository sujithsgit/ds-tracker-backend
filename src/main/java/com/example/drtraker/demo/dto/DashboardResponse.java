package com.example.drtraker.demo.dto;

import java.util.List;

public class DashboardResponse {
    private String userName;
    private Long resolutionId;
    private String resolutionTitle;
    private int durationDays;
    private String startDate;
    private String endDate;
    private int daysCompleted;
    private int daysRemaining;
    private String todayStatus;
    private int streak;
    private int bestStreak;
    private List<LogDto> logs;
    private List<ResolutionDto> resolutions;
    private List<CalendarDto> calendarLogs;

    // Log DTO
    public static class LogDto {
        private String date;
        private String status;

        public LogDto(String date, String status) {
            this.date = date;
            this.status = status;
        }
        public String getDate() { return date; }
        public String getStatus() { return status; }
    }

    // ✅ CalendarDto — resolution breakdown add பண்ணு
    public static class CalendarDto {
        private String date;
        private String status;
        private List<CalendarResolutionDto> resolutions; // ✅ add

        public CalendarDto(String date, String status, List<CalendarResolutionDto> resolutions) {
            this.date = date;
            this.status = status;
            this.resolutions = resolutions; // ✅ add
        }
        public String getDate() { return date; }
        public String getStatus() { return status; }
        public List<CalendarResolutionDto> getResolutions() { return resolutions; } // ✅ add
    }

    // ✅ Each resolution-ஓட status for that day
    public static class CalendarResolutionDto {
        private String title;
        private String status;

        public CalendarResolutionDto(String title, String status) {
            this.title = title;
            this.status = status;
        }
        public String getTitle() { return title; }
        public String getStatus() { return status; }
    }

    // Resolution DTO
    public static class ResolutionDto {
        private Long id;
        private String title;
        private int durationDays;
        private int daysCompleted;
        private int daysRemaining;
        private String todayStatus;
        private int streak;

        public Long getId() { return id; }
        public void setId(Long id) { this.id = id; }
        public String getTitle() { return title; }
        public void setTitle(String title) { this.title = title; }
        public int getDurationDays() { return durationDays; }
        public void setDurationDays(int durationDays) { this.durationDays = durationDays; }
        public int getDaysCompleted() { return daysCompleted; }
        public void setDaysCompleted(int daysCompleted) { this.daysCompleted = daysCompleted; }
        public int getDaysRemaining() { return daysRemaining; }
        public void setDaysRemaining(int daysRemaining) { this.daysRemaining = daysRemaining; }
        public String getTodayStatus() { return todayStatus; }
        public void setTodayStatus(String todayStatus) { this.todayStatus = todayStatus; }
        public int getStreak() { return streak; }
        public void setStreak(int streak) { this.streak = streak; }
    }

    // Getters & Setters
    public String getUserName() { return userName; }
    public void setUserName(String userName) { this.userName = userName; }
    public Long getResolutionId() { return resolutionId; }
    public void setResolutionId(Long resolutionId) { this.resolutionId = resolutionId; }
    public String getResolutionTitle() { return resolutionTitle; }
    public void setResolutionTitle(String resolutionTitle) { this.resolutionTitle = resolutionTitle; }
    public int getDurationDays() { return durationDays; }
    public void setDurationDays(int durationDays) { this.durationDays = durationDays; }
    public String getStartDate() { return startDate; }
    public void setStartDate(String startDate) { this.startDate = startDate; }
    public String getEndDate() { return endDate; }
    public void setEndDate(String endDate) { this.endDate = endDate; }
    public int getDaysCompleted() { return daysCompleted; }
    public void setDaysCompleted(int daysCompleted) { this.daysCompleted = daysCompleted; }
    public int getDaysRemaining() { return daysRemaining; }
    public void setDaysRemaining(int daysRemaining) { this.daysRemaining = daysRemaining; }
    public String getTodayStatus() { return todayStatus; }
    public void setTodayStatus(String todayStatus) { this.todayStatus = todayStatus; }
    public int getStreak() { return streak; }
    public void setStreak(int streak) { this.streak = streak; }
    public int getBestStreak() { return bestStreak; }
    public void setBestStreak(int bestStreak) { this.bestStreak = bestStreak; }
    public List<LogDto> getLogs() { return logs; }
    public void setLogs(List<LogDto> logs) { this.logs = logs; }
    public List<ResolutionDto> getResolutions() { return resolutions; }
    public void setResolutions(List<ResolutionDto> resolutions) { this.resolutions = resolutions; }
    public List<CalendarDto> getCalendarLogs() { return calendarLogs; }
    public void setCalendarLogs(List<CalendarDto> calendarLogs) { this.calendarLogs = calendarLogs; }
}