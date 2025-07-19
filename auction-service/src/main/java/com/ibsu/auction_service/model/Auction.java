package com.ibsu.auction_service.model;

import com.ibsu.common.enums.AuctionStatus;
import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.Instant;

@Entity
public class Auction {
    @Id
    @SequenceGenerator(name = "auction_id_seq", sequenceName = "auction_id_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "auction_id_seq")
    private Long auctionId;

    @Column(name = "item_id", nullable = false)
    private Long itemId;

    @Column(name = "starting_price", nullable = false, precision = 19, scale = 2)
    private BigDecimal startingPrice;

    @Column(name = "current_price", nullable = false, precision = 19, scale = 2)
    private BigDecimal currentPrice;

    @Column(name = "start_time", nullable = false)
    private Instant startTime;

    @Column(name = "end_time", nullable = false)
    private Instant endTime;

    @Column(name = "status", nullable = false)
    @Enumerated(EnumType.STRING)
    private AuctionStatus status;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private Instant createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private Instant updatedAt;


    public Auction(Long itemId, BigDecimal startingPrice,
                   Instant startTime, Instant endTime) {
        this.itemId = itemId;
        this.startingPrice = startingPrice;
        this.currentPrice = startingPrice;
        this.startTime = startTime;
        this.endTime = endTime;
        this.status = AuctionStatus.ACTIVE;
    }

    public Auction() {
    }

    public Long getAuctionId() {
        return auctionId;
    }

    public void setAuctionId(Long auctionId) {
        this.auctionId = auctionId;
    }

    public Long getItemId() {
        return itemId;
    }

    public void setItemId(Long itemId) {
        this.itemId = itemId;
    }

    public BigDecimal getStartingPrice() {
        return startingPrice;
    }

    public void setStartingPrice(BigDecimal startingPrice) {
        this.startingPrice = startingPrice;
    }

    public BigDecimal getCurrentPrice() {
        return currentPrice;
    }

    public void setCurrentPrice(BigDecimal currentPrice) {
        this.currentPrice = currentPrice;
    }

    public Instant getStartTime() {
        return startTime;
    }

    public void setStartTime(Instant startTime) {
        this.startTime = startTime;
    }

    public Instant getEndTime() {
        return endTime;
    }

    public void setEndTime(Instant endTime) {
        this.endTime = endTime;
    }

    public AuctionStatus getStatus() {
        return status;
    }

    public void setStatus(AuctionStatus status) {
        this.status = status;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Instant updatedAt) {
        this.updatedAt = updatedAt;
    }
}
