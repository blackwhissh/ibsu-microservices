package com.ibsu.auth_service.kafka;

import com.ibsu.common.event.UserEditedEvent;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class UserEditedEventProducer {
    private final KafkaTemplate<String, UserEditedEvent> kafkaTemplate;

    public UserEditedEventProducer(KafkaTemplate<String, UserEditedEvent> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }
    public void sendUserEditedEvent(UserEditedEvent event) {
        kafkaTemplate.send("user-edited", event);
    }
}
