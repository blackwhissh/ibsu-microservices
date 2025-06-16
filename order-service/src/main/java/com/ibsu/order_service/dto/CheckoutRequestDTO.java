package com.ibsu.order_service.dto;

public class CheckoutRequestDTO {
    private Long userId;

    public CheckoutRequestDTO(Long userId) {
        this.userId = userId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }
}
