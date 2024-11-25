package com.assignmentTeam4.repositories;

import com.assignmentTeam4.enums.ERole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.assignmentTeam4.models.User;

import java.util.*;

@Repository
public interface IUserRepository extends JpaRepository<User, UUID> {

    Optional<User> findByEmail(String email);

    Optional<User> findById(Long id);

    List<User> findAllByEmailIn(Collection<String> emails);

    boolean existsByEmail(String email);

    long countByRole(ERole role);

    List<User> findByRole(ERole role);

    List<User> findByEmailInAndRole(List<String> emails, ERole eRole);
}
