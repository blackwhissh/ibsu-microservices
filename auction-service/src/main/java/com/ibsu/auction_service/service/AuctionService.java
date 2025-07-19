package com.ibsu.auction_service.service;

import com.ibsu.auction_service.dto.AuctionCreateRequestEvent;
import com.ibsu.auction_service.dto.CreateAuctionRequest;
import com.ibsu.auction_service.exceptions.AuctionAlreadyExistsException;
import com.ibsu.auction_service.exceptions.AuctionNotFoundException;
import com.ibsu.auction_service.exceptions.WrongTimesException;
import com.ibsu.auction_service.kafka.AuctionEventsProducer;
import com.ibsu.auction_service.model.Auction;
import com.ibsu.auction_service.repository.AuctionRepository;
import com.ibsu.common.enums.AuctionStatus;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.Instant;
import java.util.UUID;

@Service
public class AuctionService {
    private final static Logger LOGGER = LoggerFactory.getLogger(AuctionService.class);
    private final RedisTemplate<String, Object> redisTemplate;
    private final AuctionEventsProducer auctionEventsProducer;
    private final AuctionRepository auctionRepository;

    public AuctionService(RedisTemplate<String, Object> redisTemplate, AuctionEventsProducer auctionEventsProducer, AuctionRepository auctionRepository) {
        this.redisTemplate = redisTemplate;
        this.auctionEventsProducer = auctionEventsProducer;
        this.auctionRepository = auctionRepository;
    }

    public String requestAuctionCreation(CreateAuctionRequest req) {
        String requestId = UUID.randomUUID().toString();
        LOGGER.info("Auction creation request received: {}", requestId);
        if (auctionRepository.existsByItemId(req.itemId())) {
            LOGGER.error("Auction with provided item already exists: {}", req.itemId());
            throw new AuctionAlreadyExistsException();
        }

        if (req.endTime().isBefore(req.startTime())) {
            LOGGER.error("Auction with provided end time is before start time");
            throw new WrongTimesException("Auction with provided end time is before start time");
        }
        redisTemplate.opsForValue().set(requestId, req, Duration.ofMinutes(5));

        AuctionCreateRequestEvent event = new AuctionCreateRequestEvent(
                requestId,
                req.itemId(),
                req.startingPrice(),
                req.startTime(),
                req.endTime()
        );

        auctionEventsProducer.requestAuctionCreation(event);
        return requestId;
    }

    public Auction createAuction(CreateAuctionRequest req, String requestId) {
        LOGGER.info("Auction creation started: {}", requestId);
        Auction auction = new Auction(req.itemId(), req.startingPrice(), req.startTime(), req.endTime());
        Auction saved = auctionRepository.save(auction);

        Duration delay = Duration.between(Instant.now(), saved.getEndTime());
        if (!delay.isNegative()) {
            redisTemplate.opsForValue().set("end_auction:" + saved.getAuctionId(), "", delay);
        } else {
            LOGGER.error("Auction end time has passed: {}", saved.getEndTime());
        }

        scheduleAuctionEnd(saved);
        return saved;
    }

    public void cancelAuction(Long auctionId) {
        LOGGER.info("Canceling auction: {}", auctionId);

        Auction auction = auctionRepository.findById(auctionId)
                .orElseThrow(AuctionNotFoundException::new);

        if (auction.getStatus() == AuctionStatus.CANCELLED || auction.getStatus() == AuctionStatus.COMPLETED) {
            LOGGER.warn("Auction {} is already {}. Skipping cancel.", auctionId, auction.getStatus());
            return;
        }

        auction.setStatus(AuctionStatus.CANCELLED);
        auctionRepository.save(auction);

        LOGGER.info("Auction {} cancelled successfully", auctionId);
    }

    public void completeAuction(Long auctionId) {
        LOGGER.info("Completing auction: {}", auctionId);
        Auction auction = auctionRepository.findByAuctionIdAndStatus(auctionId, AuctionStatus.ACTIVE)
                .orElseThrow(AuctionNotFoundException::new);


        //TODO send kafka event
        auction.setStatus(AuctionStatus.COMPLETED);
        auctionRepository.save(auction);
    }

    private void scheduleAuctionEnd(Auction auction) {
        String key = "end_auction:" + auction.getAuctionId();
        Duration ttl = Duration.between(Instant.now(), auction.getEndTime());

        redisTemplate.opsForValue().set(key, "", ttl);
    }
}
