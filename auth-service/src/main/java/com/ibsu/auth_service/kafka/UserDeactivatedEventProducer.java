package com.ibsu.auth_service.kafka;

import com.ibsu.common.event.UserDeactivatedEvent;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class UserDeactivatedEventProducer {
    @Value("${kafka.topic.user-deactivated}")
    private String topic;
    private final KafkaTemplate<String, UserDeactivatedEvent> kafkaTemplate;
    public UserDeactivatedEventProducer(KafkaTemplate<String, UserDeactivatedEvent> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }
    public void sendUserDeactivatedEvent(UserDeactivatedEvent event) {
        kafkaTemplate.send(topic, event);
    }
}
