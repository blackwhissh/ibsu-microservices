package com.ibsu.cart_service.kafka;

import com.ibsu.cart_service.repository.CartItemRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.kafka.annotation.KafkaListener;

import java.util.List;

public class CartEventListener {
    private static final Logger LOGGER = LoggerFactory.getLogger(CartEventListener.class);
    private final CartItemRepository cartItemRepository;

    public CartEventListener(CartItemRepository cartItemRepository) {
        this.cartItemRepository = cartItemRepository;
    }

    @KafkaListener(topics = "item.deleted", groupId = "item-service")
    @CacheEvict(value = "items", key = "#itemId")
    public void handleItemDeleted(String itemId) {
        LOGGER.info("Received item.deleted event for itemId: {}", itemId);
        cartItemRepository.deleteByItemId(Long.parseLong(itemId));
    }

    @KafkaListener(topics = "user.deleted", groupId = "user-service")
    public void handleUserDeleted(String userId) {
        LOGGER.info("Received user.deleted event for userId: {}", userId);
        cartItemRepository.deleteByUserId(Long.parseLong(userId));
    }

    @KafkaListener(topics = "items.deleted", groupId = "item-service")
    public void handleItemsDeleted(List<String> itemIds) {
        LOGGER.info("Received items.deleted event for itemIds: {}", itemIds);
        cartItemRepository.deleteAllById(itemIds.stream().map(Long::parseLong).toList());
    }

    @KafkaListener(topics = "${kafka.topic.user-deactivated}", groupId = "user-service")
    public void handleUserDeactivated(String userId) {
        LOGGER.info("Received user.deactivated event for userId: {}", userId);
        cartItemRepository.deleteByUserId(Long.parseLong(userId));
    }
}
