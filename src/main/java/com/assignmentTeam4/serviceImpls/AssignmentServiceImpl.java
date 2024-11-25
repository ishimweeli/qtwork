package com.assignmentTeam4.serviceImpls;

import com.assignmentTeam4.models.Assignment;
import com.assignmentTeam4.repositories.AssignmentRepo;
import com.assignmentTeam4.services.AssignmentService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class AssignmentServiceImpl implements AssignmentService {

    private final AssignmentRepo assignmentRepo;

    public AssignmentServiceImpl(AssignmentRepo assignmentRepo) {
        this.assignmentRepo = assignmentRepo;
    }

    @Override
    public Assignment createAssignment(Assignment assignment) {
        assignment.setUniqueCode(UUID.randomUUID().toString());
        return assignmentRepo.save(assignment);
    }

    @Override
    public List<Assignment> getAllAssignments() {
        return assignmentRepo.findAll();
    }

    @Override
    public Optional<Assignment> getAssignmentById(Long id) {
        return assignmentRepo.findById(id);
    }

    @Override
    public Optional<Assignment> getAssignmentByUniqueCode(String uniqueCode) {
        return assignmentRepo.findByUniqueCode(uniqueCode);
    }

    @Override
    public long countAllAssignments() {
        return assignmentRepo.count();
    }

    @Override
    public Assignment updateAssignment(Assignment assignment) {
        return assignmentRepo.save(assignment);
    }

    @Override
    public void deleteAssignment(Long id) {
        assignmentRepo.deleteById(id);
    }
}