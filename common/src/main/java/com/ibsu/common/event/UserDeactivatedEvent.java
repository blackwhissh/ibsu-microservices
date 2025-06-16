package com.ibsu.common.event;

public class UserDeactivatedEvent {
    private Long userId;
    public UserDeactivatedEvent(Long userId) {
        this.userId = userId;
    }
    public UserDeactivatedEvent() {
    }
    public Long getUserId() {
        return userId;
    }
    public void setUserId(Long userId) {}
}
