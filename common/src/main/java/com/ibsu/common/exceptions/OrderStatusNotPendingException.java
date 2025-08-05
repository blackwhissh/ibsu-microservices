package com.ibsu.common.exceptions;

public class OrderStatusNotPendingException extends RuntimeException {
    public OrderStatusNotPendingException(String message) {
        super(message);
    }
}
