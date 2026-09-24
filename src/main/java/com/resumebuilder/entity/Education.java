package com.resumebuilder.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "education")
@Data
@NoArgsConstructor
public class Education {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String degree;
    private String institution;
    private String boardOrUniversity;
    private String yearOfPassing;
    private String percentageOrCgpa;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
}
