package com.resumebuilder.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "resume_templates")
@Data
@NoArgsConstructor
public class ResumeTemplate {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String templateName;

    @Column(length = 500)
    private String description;

    /** Logical key used by PdfGenerationService to pick fonts/colors/layout */
    private String styleKey;

    private boolean active = true;
}
