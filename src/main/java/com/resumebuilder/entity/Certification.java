package com.resumebuilder.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "certifications")
@Data
@NoArgsConstructor
public class Certification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String certificationName;
    private String issuingOrganization;
    private String issueDate;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
}
