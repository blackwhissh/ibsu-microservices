package com.ibsu.common.event;

import com.ibsu.common.dto.OrderItemResponseDTO;

import java.util.List;

public record OrderConfirmedEvent (Long orderId, Long userId, List<Long> orderItemIds) {
}
