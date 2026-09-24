package com.resumebuilder.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "skills")
@Data
@NoArgsConstructor
public class Skill {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String skillName;
    private String proficiencyLevel; // Beginner / Intermediate / Advanced / Expert

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
}
