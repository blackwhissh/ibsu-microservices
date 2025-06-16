package com.ibsu.common.event;

import java.util.Optional;

public class UserEditedEvent {
    private Long userId;
    private Optional<String> firstName;
    private Optional<String> lastName;
    public UserEditedEvent(Long userId, Optional<String> firstName, Optional<String> lastName) {
        this.userId = userId;
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Optional<String> getFirstName() {
        return firstName;
    }

    public void setFirstName(Optional<String> firstName) {
        this.firstName = firstName;
    }

    public Optional<String> getLastName() {
        return lastName;
    }

    public void setLastName(Optional<String> lastName) {
        this.lastName = lastName;
    }
}
