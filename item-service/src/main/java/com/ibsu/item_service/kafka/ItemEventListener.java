package com.ibsu.item_service.kafka;

import com.ibsu.common.event.ItemsRemovedEvent;
import com.ibsu.item_service.repository.ItemRepository;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class ItemEventListener {

    private final ItemRepository itemRepository;

    public ItemEventListener(ItemRepository itemRepository) {
        this.itemRepository = itemRepository;
    }

    @KafkaListener(topics = "items.removed", groupId = "item-service")
    public void handleItemsRemoved(ItemsRemovedEvent event) {
        System.out.println("Received item deletion request: " + event.getItemIds());
        try {
            itemRepository.deleteAllById(event.getItemIds());
        } catch (Exception e) {
            System.err.println("Error deleting items: " + e.getMessage());
            e.printStackTrace();
        }

    }
}