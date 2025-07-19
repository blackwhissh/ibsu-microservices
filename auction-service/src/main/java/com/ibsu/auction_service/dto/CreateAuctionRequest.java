package com.ibsu.auction_service.dto;

import java.math.BigDecimal;
import java.time.Instant;

public record CreateAuctionRequest(Long itemId,BigDecimal startingPrice, Instant startTime, Instant endTime) {
}
