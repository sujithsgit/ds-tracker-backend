package com.example.drtraker.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.drtraker.demo.entity.Resolution;
import com.example.drtraker.demo.entity.ResolutionLog;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface ResolutionLogRepository extends JpaRepository<ResolutionLog, Long> {
    List<ResolutionLog> findByResolutionId(Long resolutionId);
    List<ResolutionLog> findByResolutionOrderByDateAsc(Resolution resolution);
    ResolutionLog findByResolutionIdAndDate(Long resolutionId, LocalDate date);
    Optional<ResolutionLog> findByResolutionAndDate(Resolution resolution, LocalDate date); // ✅ add
}