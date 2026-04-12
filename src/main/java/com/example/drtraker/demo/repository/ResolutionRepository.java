package com.example.drtraker.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.drtraker.demo.entity.Resolution;
import com.example.drtraker.demo.entity.User;

import java.util.List;

public interface ResolutionRepository extends JpaRepository<Resolution, Long> {

    List<Resolution> findByUserId(Long userId);
    List<Resolution> findByUserOrderByCreatedAtDesc(User user);
}