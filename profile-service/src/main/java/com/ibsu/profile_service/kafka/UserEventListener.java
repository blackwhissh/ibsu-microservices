package com.ibsu.profile_service.kafka;

import com.ibsu.common.event.UserCreatedEvent;
import com.ibsu.common.event.UserEditedEvent;
import com.ibsu.common.exceptions.UserNotFoundException;
import com.ibsu.profile_service.model.Profile;
import com.ibsu.profile_service.repository.ProfileRepository;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class UserEventListener {

    private final ProfileRepository profileRepository;

    public UserEventListener(ProfileRepository profileRepository) {
        this.profileRepository = profileRepository;
    }

    @KafkaListener(topics = "${kafka.topic.user-created}", groupId = "user-service")
    public void handleUserCreated(UserCreatedEvent event) {
        Profile profile = new Profile();
        profile.setUserId(event.getUserId());
        profile.setEmail(event.getEmail());
        profile.setFirstName(event.getFirstName());
        profile.setLastName(event.getLastName());
        profile.setPhone(event.getPhone());
        profileRepository.save(profile);
    }

    @KafkaListener(topics = "${kafka.topic.user-edited}", groupId = "user-service")
    public void handleUserEdited(UserEditedEvent event) {
        Profile profile = profileRepository.findById(event.getUserId()).orElseThrow(UserNotFoundException::new);
        event.getFirstName().ifPresent(profile::setFirstName);
        event.getLastName().ifPresent(profile::setLastName);
        profileRepository.save(profile);
    }
}

