package com.ibsu.auction_service.kafka;

import com.ibsu.auction_service.dto.AuctionCreatedEvent;
import com.ibsu.auction_service.dto.CreateAuctionRequest;
import com.ibsu.auction_service.model.Auction;
import com.ibsu.auction_service.service.AuctionService;
import com.ibsu.common.dto.AuctionCreateApprovedEvent;
import com.ibsu.common.dto.AuctionCreateRejectedEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class AuctionEventsListener {
    private final static Logger LOGGER = LoggerFactory.getLogger(AuctionEventsListener.class);
    private final RedisTemplate<String, Object> redisTemplate;
    private final AuctionService auctionService;
    private final AuctionEventsProducer auctionEventsProducer;

    public AuctionEventsListener(RedisTemplate<String, Object> redisTemplate, AuctionService auctionService, AuctionEventsProducer auctionEventsProducer) {
        this.redisTemplate = redisTemplate;
        this.auctionService = auctionService;
        this.auctionEventsProducer = auctionEventsProducer;
    }

    @KafkaListener(topics = "auction.create.approved", groupId = "auction-service-group")
    public void handleApproved(AuctionCreateApprovedEvent event) {
        CreateAuctionRequest request = (CreateAuctionRequest) redisTemplate.opsForValue().get(event.requestId());
        if (request == null) {
            LOGGER.error("No pending auction creation request found for requestId: {}", event.requestId());
            return;
        }

        Auction auction = auctionService.createAuction(request, event.requestId());

        redisTemplate.delete(event.requestId());

        AuctionCreatedEvent createdEvent = new AuctionCreatedEvent(
                auction.getAuctionId(), auction.getItemId(), auction.getStartTime(), auction.getEndTime()
        );
        auctionEventsProducer.sendAuctionCreatedEvent(createdEvent);
    }

    @KafkaListener(topics = "auction.create.rejected", groupId = "auction-service-group")
    public void handleRejected(AuctionCreateRejectedEvent event) {
        redisTemplate.delete(event.requestId());
        LOGGER.error("Auction creation rejected: {}", event.reason());
    }
}
