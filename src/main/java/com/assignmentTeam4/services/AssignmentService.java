package com.assignmentTeam4.services;

import com.assignmentTeam4.models.Assignment;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

public interface AssignmentService {

    Assignment createAssignment(Assignment assignment);

    List<Assignment> getAllAssignments();

    Optional<Assignment> getAssignmentById(Long id);

    Optional<Assignment> getAssignmentByUniqueCode(String uniqueCode);

    long countAllAssignments();

    Assignment updateAssignment(Assignment assignment);

    void deleteAssignment(Long id);
}