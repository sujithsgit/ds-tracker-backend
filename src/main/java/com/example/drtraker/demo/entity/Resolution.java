package com.example.drtraker.demo.entity;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
public class Resolution {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;
    private String motivation;
    private int durationDays;
    private LocalDate startDate;
    private LocalDate endDate;
    private boolean reminderEnabled;
    private String reminderTime;  // ✅ LocalTime -> String fix
    private LocalDate createdAt = LocalDate.now();

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public String getMotivation() { return motivation; }
    public void setMotivation(String motivation) { this.motivation = motivation; }
    public int getDurationDays() { return durationDays; }
    public void setDurationDays(int durationDays) { this.durationDays = durationDays; }
    public LocalDate getStartDate() { return startDate; }
    public void setStartDate(LocalDate startDate) { this.startDate = startDate; }
    public LocalDate getEndDate() { return endDate; }
    public void setEndDate(LocalDate endDate) { this.endDate = endDate; }
    public boolean isReminderEnabled() { return reminderEnabled; }
    public void setReminderEnabled(boolean reminderEnabled) { this.reminderEnabled = reminderEnabled; }
    public String getReminderTime() { return reminderTime; }  // ✅ return type String
    public void setReminderTime(String reminderTime) { this.reminderTime = reminderTime; }
    public LocalDate getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDate createdAt) { this.createdAt = createdAt; }
    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }
}