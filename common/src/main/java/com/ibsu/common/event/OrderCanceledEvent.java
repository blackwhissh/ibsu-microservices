package com.ibsu.common.event;

import com.ibsu.common.dto.OrderItemResponseDTO;

import java.util.List;

public record OrderCanceledEvent (List<Long> itemIds, Long orderId) {

}
