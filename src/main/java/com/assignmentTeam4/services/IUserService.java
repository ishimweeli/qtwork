package com.assignmentTeam4.services;

import com.assignmentTeam4.models.User;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;
import java.util.UUID;


public interface IUserService {

    User create(User user);


    boolean isNotUnique(User user);

    void validateNewRegistration(User user);

    User getLoggedInUser();

    Optional<User> getUserById(Long id);

    List<User> getAllRegisteredStudentsByEmails(List<String> studentEmails);
}