package com.ibsu.auth_service.kafka;

import com.ibsu.common.event.UserCreatedEvent;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class UserCreatedEventProducer {

    private final KafkaTemplate<String, UserCreatedEvent> kafkaTemplate;

    @Value("${kafka.topic.user-created}")
    private String userCreatedTopic;

    public UserCreatedEventProducer(KafkaTemplate<String, UserCreatedEvent> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void sendUserCreatedEvent(UserCreatedEvent event) {
        kafkaTemplate.send(userCreatedTopic, event);
    }
}

