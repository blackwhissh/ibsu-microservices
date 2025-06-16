package com.ibsu.order_service.dto;

import java.time.Instant;
import java.util.List;

public class OrderResponseDTO {
    private Long orderId;
    private Long userId;
    private Double totalPrice;
    private Instant createdAt;
    private List<OrderItemResponseDTO> items;
    public OrderResponseDTO(Long orderId, Long userId, Double totalPrice, Instant createdAt, List<OrderItemResponseDTO> items) {
        this.orderId = orderId;
        this.userId = userId;
        this.totalPrice = totalPrice;
        this.createdAt = createdAt;
        this.items = items;
    }
    public OrderResponseDTO() {
    }

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(Double totalPrice) {
        this.totalPrice = totalPrice;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public List<OrderItemResponseDTO> getItems() {
        return items;
    }

    public void setItems(List<OrderItemResponseDTO> items) {
        this.items = items;
    }
}
