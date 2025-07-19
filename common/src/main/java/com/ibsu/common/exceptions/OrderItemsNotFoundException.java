package com.ibsu.common.exceptions;

public class OrderItemsNotFoundException extends RuntimeException {
    public OrderItemsNotFoundException(String message) {
        super(message);
    }
}
