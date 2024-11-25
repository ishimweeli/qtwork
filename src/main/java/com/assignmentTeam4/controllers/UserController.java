package com.assignmentTeam4.controllers;

import com.assignmentTeam4.dtos.ChangeNamesDTO;
import com.assignmentTeam4.dtos.ChangePasswordDTO;
import com.assignmentTeam4.models.Assignment;
import com.assignmentTeam4.models.User;
import com.assignmentTeam4.repositories.IUserRepository;
import com.assignmentTeam4.services.AssignmentService;
import com.assignmentTeam4.services.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;
import com.assignmentTeam4.dtos.SignUpDTO;
import com.assignmentTeam4.enums.ERole;
import com.assignmentTeam4.payload.ApiResponse;


import javax.validation.Valid;
import java.security.SecureRandom;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;


@RestController
@RequestMapping(path = "/api/v1/users")
public class UserController {

    private final IUserService userService;
    private final IUserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final AssignmentService assignmentService;

    private static final SecureRandom SECURE_RANDOM = new SecureRandom();


    @Autowired
    public UserController(IUserService userService, IUserRepository userRepository, BCryptPasswordEncoder bCryptPasswordEncoder, JavaMailSender javaMailSender, AssignmentService assignmentService) {

        this.userService = userService;
        this.userRepository = userRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.assignmentService = assignmentService;
    }

    @GetMapping(path = "/ViewCurrent-user")
    public ResponseEntity<ApiResponse> viewProfiles() {
        return ResponseEntity.ok(new ApiResponse(true, userService.getLoggedInUser()));
    }

    @PostMapping(path = "/register/")
    public ResponseEntity<ApiResponse> register(@Valid @RequestBody SignUpDTO dto) {

        com.assignmentTeam4.models.User user = new com.assignmentTeam4.models.User();

        String encodedPassword = bCryptPasswordEncoder.encode(dto.getPassword());

        user.setEmail(dto.getEmail());
        user.setFirstName(dto.getFirstName());
        user.setLastName(dto.getLastName());
        user.setPassword(encodedPassword);
        user.setRole(ERole.STUDENT);
        com.assignmentTeam4.models.User entity = this.userService.create(user);
        return ResponseEntity.status(HttpStatus.CREATED).body(new ApiResponse(true, entity));
    }


    @PostMapping("/update-names")
    public ResponseEntity<ApiResponse> updateNames(@Valid @RequestBody ChangeNamesDTO dto){
        User user = userService.getLoggedInUser();

        user.setFirstName(dto.getNewFirstName());
        user.setLastName(dto.getNewSecondName());

        userRepository.save(user);

        return ResponseEntity.ok(new ApiResponse(true, "FirstName and LastName has successfully been changed"));
    }

    @PostMapping(path = "/change-password")
    public ResponseEntity<ApiResponse> changePassword(@Valid @RequestBody ChangePasswordDTO dto) {
        User user = userService.getLoggedInUser();

        if (!bCryptPasswordEncoder.matches(dto.getOldPassword(), user.getPassword())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ApiResponse(false, "Old password is incorrect"));
        }

        user.setPassword(bCryptPasswordEncoder.encode(dto.getNewPassword()));
        userRepository.save(user);

        return ResponseEntity.ok(new ApiResponse(true, "Password successfully updated"));
    }

    @GetMapping("/statistics")
    public ResponseEntity<ApiResponse> getAllRegisteredStudents() {
        User currentUser = userService.getLoggedInUser();

        long totalStudents = userRepository.countByRole(ERole.STUDENT);

        List<String> studentNames = userRepository.findByRole(ERole.STUDENT)
                .stream()
                .map(User::getFullName)
                .collect(Collectors.toList());

        Map<String, Object> stats = new HashMap<>();
        stats.put("TotalStudents", totalStudents);
        stats.put("StudentNames", studentNames);

        return ResponseEntity.of(Optional.of(new ApiResponse(true, stats)));
    }
}