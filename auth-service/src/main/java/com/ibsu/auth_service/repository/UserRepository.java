package com.ibsu.auth_service.repository;

import com.ibsu.auth_service.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);

    boolean existsByUsername(String workId);

    boolean existsByEmail(String email);
}
