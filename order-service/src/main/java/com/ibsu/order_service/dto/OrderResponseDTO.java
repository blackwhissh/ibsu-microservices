package com.ibsu.order_service.dto;

import com.ibsu.common.dto.OrderItemResponseDTO;
import com.ibsu.common.enums.DeliveryTypeEnum;
import com.ibsu.common.enums.OrderStatusEnum;

import java.time.Instant;
import java.util.List;

public class OrderResponseDTO {
    private Long orderId;
    private Long userId;
    private Double totalPrice;
    private Instant createdAt;
    private List<OrderItemResponseDTO> items;
    private OrderStatusEnum orderStatus;
    private DeliveryTypeEnum deliveryType;
    public OrderResponseDTO(Long orderId, Long userId, Double totalPrice, Instant createdAt, List<OrderItemResponseDTO> items, OrderStatusEnum orderStatus, DeliveryTypeEnum deliveryType) {
        this.orderId = orderId;
        this.userId = userId;
        this.totalPrice = totalPrice;
        this.createdAt = createdAt;
        this.items = items;
        this.orderStatus = orderStatus;
        this.deliveryType = deliveryType;
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

    public OrderStatusEnum getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(OrderStatusEnum orderStatus) {
        this.orderStatus = orderStatus;
    }

    public void setItems(List<OrderItemResponseDTO> items) {
        this.items = items;
    }

    public DeliveryTypeEnum getDeliveryType() {
        return deliveryType;
    }

    public void setDeliveryType(DeliveryTypeEnum deliveryType) {
        this.deliveryType = deliveryType;
    }
}
