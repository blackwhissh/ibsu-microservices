package com.ibsu.auction_service.dto;

import java.time.Instant;

public class AuctionCreatedEvent {
    private Long auctionId;
    private Long itemId;
    private Instant startTime;
    private Instant endTime;

    public AuctionCreatedEvent(Long auctionId, Long itemId, Instant startTime, Instant endTime) {
        this.auctionId = auctionId;
        this.itemId = itemId;
        this.startTime = startTime;
        this.endTime = endTime;
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
}
