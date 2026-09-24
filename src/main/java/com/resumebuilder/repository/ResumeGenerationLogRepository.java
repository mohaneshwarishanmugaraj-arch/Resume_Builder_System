package com.resumebuilder.repository;

import com.resumebuilder.entity.ResumeGenerationLog;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ResumeGenerationLogRepository extends JpaRepository<ResumeGenerationLog, Long> {
    long countByUserId(Long userId);
}
