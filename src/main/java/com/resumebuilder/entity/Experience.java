package com.resumebuilder.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "experience")
@Data
@NoArgsConstructor
public class Experience {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String companyName;
    private String jobRole;
    private String startDate;
    private String endDate;

    @Column(length = 1000)
    private String description;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
}
