package com.ibsu.cart_service.service;

import com.ibsu.cart_service.client.ItemClient;
import com.ibsu.cart_service.dto.ItemDTO;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

@Service
public class CachedItemService {

    private final ItemClient itemClient;

    public CachedItemService(ItemClient itemClient) {
        this.itemClient = itemClient;
    }

    @Cacheable(value = "items", key = "#itemId")
    public ItemDTO getItemById(Long itemId) {
        return itemClient.getItemById(itemId);
    }
}

