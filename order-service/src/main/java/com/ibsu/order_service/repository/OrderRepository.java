package com.ibsu.order_service.repository;

import com.ibsu.common.enums.OrderStatusEnum;
import com.ibsu.order_service.model.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.time.Instant;
import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {
    Page<Order> findAllByUserId(Long userId, Pageable pageable);
    boolean existsOrderByIdAndUserId(Long id, Long userId);
    @Modifying
    @Query("UPDATE Order o SET o.orderStatus = :status WHERE o.id = :orderId")
    void updateOrderStatus(OrderStatusEnum status, Long orderId);
    List<Order> findByCreatedAtBeforeAndOrderStatus(Instant time, OrderStatusEnum status);

}
