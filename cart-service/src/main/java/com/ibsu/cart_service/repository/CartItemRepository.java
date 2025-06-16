package com.ibsu.cart_service.repository;

import com.ibsu.cart_service.model.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CartItemRepository extends JpaRepository<CartItem, Long> {
    List<CartItem> findByUserId(Long userId);
    void deleteByUserIdAndItemId(Long userId, Long itemId);

    void deleteByItemId(long itemId);

    void deleteByUserId(long userId);

    boolean existsByUserIdAndItemId(Long userId, Long itemId);
}
