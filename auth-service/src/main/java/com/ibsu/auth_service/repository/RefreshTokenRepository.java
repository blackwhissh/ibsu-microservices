package com.ibsu.auth_service.repository;


import com.ibsu.auth_service.model.RefreshToken;
import com.ibsu.auth_service.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {
    Optional<RefreshToken> findRefreshTokenByToken(String token);

    int deleteByUser(User user);
    void deleteByUser_Id(Long userId);

    User user(User user);
}
