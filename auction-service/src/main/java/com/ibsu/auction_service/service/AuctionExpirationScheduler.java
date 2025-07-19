package com.ibsu.auction_service.service;

import com.ibsu.auction_service.kafka.AuctionEventsProducer;
import com.ibsu.auction_service.repository.AuctionRepository;
import com.ibsu.common.enums.AuctionStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
public class AuctionExpirationScheduler {
    private final Logger LOGGER = LoggerFactory.getLogger(AuctionExpirationScheduler.class);
    private final RedisTemplate<String, Object> redisTemplate;
    private final AuctionRepository auctionRepository;
    private final AuctionEventsProducer auctionEventsProducer;

    public AuctionExpirationScheduler(RedisTemplate<String, Object> redisTemplate,
                                      AuctionRepository auctionRepository,
                                      AuctionEventsProducer auctionEventsProducer) {
        this.redisTemplate = redisTemplate;
        this.auctionRepository = auctionRepository;
        this.auctionEventsProducer = auctionEventsProducer;
    }

    @Scheduled(fixedRate = 10000) // every 10 seconds
    public void endExpiredAuctions() {
        Set<String> keys = redisTemplate.keys("end_auction:*");
        if (keys == null) return;

        for (String key : keys) {
            if (Boolean.FALSE.equals(redisTemplate.hasKey(key))) {
                continue; // already processed
            }

            String[] parts = key.split(":");
            Long auctionId = Long.parseLong(parts[1]);

            auctionRepository.findById(auctionId).ifPresent(auction -> {
                if (auction.getStatus() == AuctionStatus.ACTIVE) {
                    auction.setStatus(AuctionStatus.COMPLETED);
                    auctionRepository.save(auction);

                    auctionEventsProducer.sendAuctionEndedEvent(auction); // Optional: Kafka event
                    LOGGER.info("Auction {} has ended automatically", auctionId);
                }
            });

            redisTemplate.delete(key);
        }
    }
}

