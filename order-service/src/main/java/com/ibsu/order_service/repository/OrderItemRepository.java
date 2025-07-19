package com.ibsu.order_service.repository;

import com.ibsu.common.dto.OrderItemResponseDTO;
import com.ibsu.order_service.model.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {
    @Query("SELECT new com.ibsu.common.dto.OrderItemResponseDTO(oi.itemId, oi.itemName,oi.artistName, oi.itemImage, oi.priceSnapshot) " +
            "FROM OrderItem oi WHERE oi.order.id = :orderId AND oi.order.userId = :userId")
    List<OrderItemResponseDTO> findByOrderIdAndOrder_UserId(Long orderId, Long userId);
}
