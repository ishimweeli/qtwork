package com.assignmentTeam4.serviceImpls;

import com.assignmentTeam4.enums.ERole;
import com.assignmentTeam4.repositories.AssignmentRepo;
import com.assignmentTeam4.repositories.IUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import com.assignmentTeam4.exceptions.BadRequestException;
import com.assignmentTeam4.exceptions.ResourceNotFoundException;
import com.assignmentTeam4.models.User;
import com.assignmentTeam4.services.IUserService;

import java.util.*;

@Service
public class UserServiceImpl implements IUserService {

    private final IUserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final AssignmentRepo assignmentRepo;

    @Autowired
    public UserServiceImpl(IUserRepository userRepository, BCryptPasswordEncoder bCryptPasswordEncoder, AssignmentRepo assignmentRepo) {
        this.userRepository = userRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.assignmentRepo = assignmentRepo;
    }

    @Override
    public User create(User user) {
        validateNewRegistration(user);

        return this.userRepository.save(user);
    }


    @Override
    public boolean isNotUnique(User user) {
        Optional<User> userOptional = this.userRepository.findByEmail(user.getEmail());
        return userOptional.isPresent();
    }

    @Override
    public void validateNewRegistration(User user) {
        if (isNotUnique(user)) {
            throw new BadRequestException(String.format("User with email already exists", user.getEmail()));
        }
    }

    @Override
    public User getLoggedInUser() {
        if (SecurityContextHolder.getContext().getAuthentication().getPrincipal() == "anonymousUser")
            throw new BadRequestException("You are not logged in, try to log in");

        String email;
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if (principal instanceof UserDetails) {
            email = ((UserDetails) principal).getUsername();
        } else {
            email = principal.toString();
        }

        return userRepository.findByEmail(email).orElseThrow(
                () -> new ResourceNotFoundException("User", "id", email));
    }

    public List<User> getAllRegisteredStudentsByEmails(List<String> emails) {
        return userRepository.findByEmailInAndRole(emails, ERole.STUDENT);
    }

    @Override
    public Optional<User> getUserById(Long id) {
        return userRepository.findById(id);
    }

}