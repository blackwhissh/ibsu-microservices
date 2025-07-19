package com.ibsu.common.dto;

public record AuctionCreateRejectedEvent(String requestId, String reason) {
}
