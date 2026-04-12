package com.example.drtraker.demo.dto;

public class ResolutionRequest {

    private String resolution;
    private String durationType;
    private String customDuration;  // ✅ added
    private String customUnit;      // ✅ added
    private String customValue;
    private boolean reminderEnabled;
    private String reminderTime;
    private String startDate;
    private String motivation;

    // getters & setters

    public String getResolution() { return resolution; }
    public void setResolution(String resolution) { this.resolution = resolution; }

    public String getDurationType() { return durationType; }
    public void setDurationType(String durationType) { this.durationType = durationType; }

    public String getCustomDuration() { return customDuration; }
    public void setCustomDuration(String customDuration) { this.customDuration = customDuration; }

    public String getCustomUnit() { return customUnit; }
    public void setCustomUnit(String customUnit) { this.customUnit = customUnit; }

    public String getCustomValue() { return customValue; }
    public void setCustomValue(String customValue) { this.customValue = customValue; }

    public boolean isReminderEnabled() { return reminderEnabled; }
    public void setReminderEnabled(boolean reminderEnabled) { this.reminderEnabled = reminderEnabled; }

    public String getReminderTime() { return reminderTime; }
    public void setReminderTime(String reminderTime) { this.reminderTime = reminderTime; }

    public String getStartDate() { return startDate; }
    public void setStartDate(String startDate) { this.startDate = startDate; }

    public String getMotivation() { return motivation; }
    public void setMotivation(String motivation) { this.motivation = motivation; }
}