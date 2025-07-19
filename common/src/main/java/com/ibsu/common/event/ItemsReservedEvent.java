package com.ibsu.common.event;

import java.util.List;

public class ItemsReservedEvent {
    private List<Long> itemIds;

    public ItemsReservedEvent(List<Long> itemIds) {
        this.itemIds = itemIds;
    }
    public ItemsReservedEvent() {
    }

    public List<Long> getItemIds() {
        return itemIds;
    }

    public void setItemIds(List<Long> itemIds) {
        this.itemIds = itemIds;
    }
}
