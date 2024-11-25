package com.assignmentTeam4.dtos;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class AssignmentDTO {
    private String title;
    private String description;
    private LocalDateTime deadline;
}