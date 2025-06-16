package com.ibsu.profile_service.repository;

import com.ibsu.profile_service.model.Profile;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProfileRepository extends JpaRepository<Profile, Long> {
}
