package com.ibsu.auction_service.repository;

import com.ibsu.auction_service.model.Auction;
import com.ibsu.common.enums.AuctionStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.lang.ScopedValue;
import java.time.Instant;
import java.util.List;
import java.util.Optional;

public interface AuctionRepository extends JpaRepository<Auction, Long> {
    List<Auction> findByStatusAndEndTimeBefore(AuctionStatus status, Instant endTime);
    Optional<Auction> findByItemId(Long itemId);
    List<Auction> findByStatus(AuctionStatus status);
    boolean existsByItemId(Long itemId);

    Optional<Auction> findByAuctionIdAndStatus(Long auctionId, AuctionStatus auctionStatus);
}
