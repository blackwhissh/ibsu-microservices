package com.ibsu.common.event;

import java.util.List;

public class ItemsRemovedEvent {
    private List<Long> itemIds;

    public ItemsRemovedEvent(List<Long> itemIds) {
        this.itemIds = itemIds;
    }
    public ItemsRemovedEvent() {
    }

    public List<Long> getItemIds() {
        return itemIds;
    }

    public void setItemIds(List<Long> itemIds) {
        this.itemIds = itemIds;
    }
}
