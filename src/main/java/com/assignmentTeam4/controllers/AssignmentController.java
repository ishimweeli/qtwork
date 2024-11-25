package com.assignmentTeam4.controllers;

import com.assignmentTeam4.dtos.AssignmentDTO;
import com.assignmentTeam4.enums.ERole;
import com.assignmentTeam4.models.Assignment;
import com.assignmentTeam4.models.User;
import com.assignmentTeam4.repositories.IUserRepository;
import com.assignmentTeam4.services.AssignmentService;
import com.assignmentTeam4.services.IUserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.web.bind.annotation.*;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;


@RestController
@RequestMapping("/api/v1/tasks")
public class AssignmentController {

    private final AssignmentService assignmentService;
    private final IUserRepository userRepository;
    private final IUserService userService;
    private final JavaMailSender mailSender;

    private static final Logger log = LoggerFactory.getLogger(AssignmentController.class);


    public AssignmentController(AssignmentService assignmentService, IUserRepository userRepository, IUserService userService, JavaMailSender mailSender) {
        this.assignmentService = assignmentService;
        this.userRepository = userRepository;
        this.userService = userService;
        this.mailSender = mailSender;
    }

    @PostMapping
    public ResponseEntity<Object> createATask(@RequestBody AssignmentDTO assignmentDTO) {
        User currentUser = userService.getLoggedInUser();
            Assignment assignment = convertToEntity(assignmentDTO);


            Assignment createdAssignment = assignmentService.createAssignment(assignment);
            return new ResponseEntity<>(createdAssignment, HttpStatus.CREATED);
    }

    private Assignment convertToEntity(AssignmentDTO assignmentDTO) {
        Assignment assignment = new Assignment();
        assignment.setTitle(assignmentDTO.getTitle());
        assignment.setDescription(assignmentDTO.getDescription());
        assignment.setDeadline(assignmentDTO.getDeadline());
        assignment.setUniqueCode(getUniqueCode());
        return assignment;
    }

    private String getUniqueCode() {
        return UUID.randomUUID().toString();
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateATask(@PathVariable Long id, @RequestBody AssignmentDTO assignmentDTO) {
        User currentUser = userService.getLoggedInUser();

        Optional<Assignment> assignmentOpt = assignmentService.getAssignmentById(id);
        if (!assignmentOpt.isPresent()) {
            return new ResponseEntity<>("Assignment not found", HttpStatus.NOT_FOUND);
        }

        Assignment assignment = assignmentOpt.get();
        assignment.setTitle(assignmentDTO.getTitle());
        assignment.setDescription(assignmentDTO.getDescription());
        assignment.setDeadline(assignmentDTO.getDeadline());

        Assignment updatedAssignment = assignmentService.updateAssignment(assignment);
        return new ResponseEntity<>(updatedAssignment, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteATask(@PathVariable Long id) {
        User currentUser = userService.getLoggedInUser();

        Optional<Assignment> assignmentOpt = assignmentService.getAssignmentById(id);
        if (!assignmentOpt.isPresent()) {
            return new ResponseEntity<>("Assignment not found", HttpStatus.NOT_FOUND);
        }

        Assignment assignment = assignmentOpt.get();

        assignmentService.deleteATask(id);
        return new ResponseEntity<>("Assignment deleted successfully", HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<List<Assignment>> statisticsOfAssignments(){
        List<Assignment> assignments = assignmentService.getAllAssignments();

        return new ResponseEntity<>(assignments, HttpStatus.OK);
    }

    @PostMapping("/invite")
    public ResponseEntity<?> assignTask(@RequestParam String uniqueCode, @RequestBody List<String> studentEmails) {
        Optional<Assignment> assignmentOpt = assignmentService.getAssignmentByUniqueCode(uniqueCode);
        if (!assignmentOpt.isPresent()) {
            return new ResponseEntity<>("Assignment not found", HttpStatus.NOT_FOUND);
        }
        Assignment assignment = assignmentOpt.get();

        List<User> registeredStudents = userService.getAllRegisteredStudentsByEmails(studentEmails);
        if (registeredStudents.isEmpty()) {
            return new ResponseEntity<>("No registered user found with the provided emails", HttpStatus.BAD_REQUEST);
        }

        registeredStudents.forEach(student -> sendAssignmentInvitationEmail(student, assignment));

        return new ResponseEntity<>("Invitations sent successfully", HttpStatus.OK);
    }

    private void sendAssignmentInvitationEmail(User student, Assignment assignment) {
        String subject = "Invitation to Assignment";
        String messageText = String.format(
                "Hello, %s %s,\n\n" +
                        "You have been invited to partake in the assignment: '%s' before %s.\n\n" +
                        "Please click on the following link to access the assignment and submit your work:\n%s\n\n" +
                        "Best Regards,\nYour Team",
                student.getFirstName(),
                student.getLastName(),
                assignment.getTitle(),
                assignment.getDeadline().format(DateTimeFormatter.ISO_LOCAL_DATE),
                "http://mydomain.com/assignments/" + assignment.getUniqueCode()
        );

        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(student.getEmail());
        message.setSubject(subject);
        message.setText(messageText);
        mailSender.send(message);
    }
}