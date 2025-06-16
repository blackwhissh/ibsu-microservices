package com.ibsu.order_service.controller;

import com.ibsu.order_service.dto.OrderResponseDTO;
import com.ibsu.order_service.model.Order;
import com.ibsu.order_service.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/order")
public class OrderController {
    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping(value = "/checkout")
    public ResponseEntity<OrderResponseDTO> checkout() {
        Long userId = Long.valueOf(SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString());
        return ResponseEntity.ok(orderService.checkout(userId));
    }

//    @GetMapping
//    public ResponseEntity<List<Order>> getUserOrders(@RequestHeader("X-User-Id") String userId) {
//        // Retrieve orders for this user
//        List<Order> orders = orderService.findByUserId(userId);
//        return ResponseEntity.ok(orders);
//    }

}
