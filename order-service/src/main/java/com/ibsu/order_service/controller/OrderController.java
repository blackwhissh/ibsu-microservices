package com.ibsu.order_service.controller;

import com.ibsu.order_service.dto.OrderHistoryDTO;
import com.ibsu.order_service.dto.OrderResponseDTO;
import com.ibsu.order_service.model.Order;
import com.ibsu.order_service.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
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

    @GetMapping("/history")
    public ResponseEntity<Page<OrderHistoryDTO>> getOrdersByCurrentUser(
            @PageableDefault(page = 0, size = 9, sort = "createdAt", direction = Sort.Direction.DESC)
            Pageable pageable) {
        Long userId = Long.valueOf(SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString());
        return ResponseEntity.ok(orderService.getUserOrderHistory(userId, pageable));
    }

    @GetMapping("/{orderId}")
    public ResponseEntity<OrderResponseDTO> getOrderByCurrentUser(@PathVariable Long orderId) {
        Long userId = Long.valueOf(SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString());
        return ResponseEntity.ok(orderService.getOrderDetails(userId, orderId));
    }

    @DeleteMapping("/cancel/{orderId}")
    public ResponseEntity<OrderResponseDTO> cancelOrderByCurrentUser(@PathVariable Long orderId) {
        Long userId = Long.valueOf(SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString());
        orderService.deleteOrderByCurrentUser(userId, orderId);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/confirm/{orderId}")
    public ResponseEntity<?> confirmOrderByCurrentUser(@PathVariable Long orderId) {
        Long userId = Long.valueOf(SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString());
        orderService.confirmOrder(userId, orderId);
        return ResponseEntity.ok().body("Order confirmed");
    }

    @PostMapping("/approve")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<OrderResponseDTO> approveOrderByCurrentUser(@RequestParam Long userId, @RequestParam Long orderId) {
        orderService.approveOrder(userId, orderId);
        return ResponseEntity.ok().build();
    }


}
