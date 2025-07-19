package com.ibsu.auction_service.kafka;

import com.ibsu.auction_service.dto.AuctionCreateRequestEvent;
import com.ibsu.auction_service.dto.AuctionCreatedEvent;
import com.ibsu.auction_service.dto.AuctionRejectedEvent;
import com.ibsu.auction_service.dto.CreateAuctionRequest;
import com.ibsu.common.dto.AuctionCreateRejectedEvent;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.Objects;
import java.util.UUID;

@Service
public class AuctionEventsProducer {
    private final KafkaTemplate<String, Object> kafkaTemplate;


    public AuctionEventsProducer(KafkaTemplate<String, Object> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void requestAuctionCreation(AuctionCreateRequestEvent event) {
        kafkaTemplate.send("auction.create.request", event);
    }

    public void sendAuctionCreatedEvent(AuctionCreatedEvent event) {
        kafkaTemplate.send("auction.created", event);
    }

    public void sendAuctionRejectedEvent(AuctionRejectedEvent event) {
        kafkaTemplate.send("auction.rejected", event);
    }

}
