package com.ibsu.order_service.dto;

import com.ibsu.common.enums.OrderStatusEnum;

import java.time.Instant;

public record OrderHistoryDTO (Long orderId, Long userId, Double totalPrice, Instant date,
                               String imageUrl, OrderStatusEnum orderStatus) {
}
