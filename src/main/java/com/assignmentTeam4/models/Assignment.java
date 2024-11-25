package com.assignmentTeam4.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Inheritance(strategy = InheritanceType.JOINED)
@Table(name = "assignment")
public class Assignment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(name = "description", nullable = false, columnDefinition = "TEXT")
    private String description;

//    @Column(nullable = false)
//    private LocalDateTime createdTime;

    @Column(nullable = false)
    private LocalDateTime deadline;

    @Column(nullable = false, unique = true)
    private String uniqueCode;

    public Assignment(String title, String description, String deadline) {
    }
}