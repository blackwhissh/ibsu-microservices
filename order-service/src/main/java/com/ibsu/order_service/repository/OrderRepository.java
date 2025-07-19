package com.ibsu.order_service.repository;

import com.ibsu.order_service.model.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {
    Page<Order> findAllByUserId(Long userId, Pageable pageable);
    boolean existsOrderByIdAndUserId(Long id, Long userId);
}
