package com.ibsu.item_service;

import com.ibsu.item_service.model.Item;
import com.ibsu.item_service.repository.ItemRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ItemService {
    private final static Logger LOGGER = LoggerFactory.getLogger(ItemService.class);
    private final ItemRepository itemRepository;

    public ItemService(ItemRepository itemRepository) {
        this.itemRepository = itemRepository;
    }

    public Item addItem(Item item) {
        LOGGER.info("Adding item: {}", item.getItemId());
        return itemRepository.save(item);
    }

    public void deleteItems(List<Long> itemIds) {
        LOGGER.info("Deleting items: {}", itemIds);
        itemRepository.deleteAllById(itemIds);
    }
}
