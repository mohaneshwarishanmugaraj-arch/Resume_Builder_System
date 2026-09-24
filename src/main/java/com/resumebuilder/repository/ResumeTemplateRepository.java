package com.resumebuilder.repository;

import com.resumebuilder.entity.ResumeTemplate;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ResumeTemplateRepository extends JpaRepository<ResumeTemplate, Long> {
    List<ResumeTemplate> findByActiveTrue();
}
