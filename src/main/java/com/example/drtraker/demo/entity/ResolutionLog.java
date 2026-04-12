package com.example.drtraker.demo.entity;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
public class ResolutionLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDate date;

    private String status; // PENDING / DONE / SKIPPED

    // 🔗 Many logs → One resolution
    @ManyToOne
    @JoinColumn(name = "resolution_id")
    private Resolution resolution;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public LocalDate getDate() {
		return date;
	}

	public void setDate(LocalDate date) {
		this.date = date;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Resolution getResolution() {
		return resolution;
	}

	public void setResolution(Resolution resolution) {
		this.resolution = resolution;
	}

    // getters & setters
    
}
