package com.ibsu.item_service.kafka;

import com.ibsu.common.enums.ItemStatusEnum;
import com.ibsu.common.event.ItemsReservedEvent;
import com.ibsu.common.event.OrderCanceledEvent;
import com.ibsu.common.event.OrderConfirmedEvent;
import com.ibsu.item_service.dto.ItemsRemovedDTO;
import com.ibsu.item_service.model.Item;
import com.ibsu.item_service.service.ItemService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ItemEventListener {
    private static final Logger LOGGER = LoggerFactory.getLogger(ItemEventListener.class);
    private final SimpMessagingTemplate messagingTemplate;
    private final ItemService itemService;


    public ItemEventListener(SimpMessagingTemplate messagingTemplate, ItemService itemService) {
        this.messagingTemplate = messagingTemplate;
        this.itemService = itemService;
    }

    @KafkaListener(topics = "items.reserved", groupId = "item-service")
    public void handleItemsCheckout(ItemsReservedEvent event) {
        LOGGER.info("Received items checkout request: {}", event.getItemIds());
        try {
            if (!event.getItemIds().isEmpty()) {
                itemService.editStatusByIdList(event.getItemIds(), ItemStatusEnum.RESERVED);
            }

            messagingTemplate.convertAndSend("/topic/items/removed", new ItemsRemovedDTO(event.getItemIds()));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @KafkaListener(topics = "order.cancelled", groupId = "item-service")
    public void handleOrderCancelled(OrderCanceledEvent event) {
        LOGGER.info("Received order cancel request: {}", event.orderId());
        try {
            if (!event.itemIds().isEmpty()) {
                itemService.editStatusByIdList(event.itemIds(), ItemStatusEnum.AVAILABLE);
            }

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @KafkaListener(topics = "order.approved", groupId = "item-service")
    public void handleOrderConfirmed(OrderConfirmedEvent event) {
        LOGGER.info("Received order confirmed request: {}", event.orderId());
        try {
            if (!event.orderItemIds().isEmpty()) {
                itemService.editStatusByIdList(event.orderItemIds(), ItemStatusEnum.SOLD);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}