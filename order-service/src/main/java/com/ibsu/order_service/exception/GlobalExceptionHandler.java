package com.ibsu.order_service.exception;

import com.ibsu.common.exceptions.ExceptionResponse;
import com.ibsu.common.exceptions.OrderItemsNotFoundException;
import com.ibsu.common.exceptions.OrderNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.LocalDateTime;

@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(OrderItemsNotFoundException.class)
    public ResponseEntity<Object> handleOrderItemsNotFoundException(OrderItemsNotFoundException e) {
        ExceptionResponse response = new ExceptionResponse();
        response.setDateTime(LocalDateTime.now());
        response.setMessage("Order items not found!");
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(OrderNotFoundException.class)
    public ResponseEntity<Object> handleOrderNotFoundException(OrderNotFoundException e) {
        ExceptionResponse response = new ExceptionResponse();
        response.setDateTime(LocalDateTime.now());
        response.setMessage("Order not found!");
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

}
