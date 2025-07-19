package com.ibsu.item_service.dto;

import java.util.List;

public class ItemsRemovedDTO {
    private List<Long> itemIds;

    public ItemsRemovedDTO(List<Long> itemIds) {
        this.itemIds = itemIds;
    }

    public List<Long> getItemIds() {
        return itemIds;
    }

    public void setItemIds(List<Long> itemIds) {
        this.itemIds = itemIds;
    }
}

