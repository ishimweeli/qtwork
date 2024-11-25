package com.assignmentTeam4.repositories;

import com.assignmentTeam4.models.Assignment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AssignmentRepo extends JpaRepository<Assignment, Long> {
    Optional<Assignment> findByUniqueCode(String uniqueCode);
}
