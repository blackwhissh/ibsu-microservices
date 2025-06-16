package com.ibsu.cart_service.controller;

import com.ibsu.common.dto.CartResponseDTO;
import com.ibsu.cart_service.model.CartItem;
import com.ibsu.cart_service.service.CartItemService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/cart")
public class CartItemController {

    private final CartItemService cartItemService;

    public CartItemController(CartItemService cartItemService) {
        this.cartItemService = cartItemService;
    }

    @GetMapping
    public CartResponseDTO getCart() {
        Long userIdFromContext = Long.valueOf(SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString());
        return cartItemService.getCartByUserId(userIdFromContext);
    }


    @PostMapping("/add")
    public CartItem addToCart(@RequestBody Map<String, Object> payload) {
        Long userId = Long.valueOf(SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString());
        Long itemId = Long.valueOf(payload.get("itemId").toString());
        double priceSnapshot = Double.parseDouble(payload.get("priceSnapshot").toString());

        return cartItemService.addToCart(userId, itemId, priceSnapshot);
    }

    @DeleteMapping("/remove")
    public void removeFromCart(@RequestParam Long itemId) {
        Long userIdFromContext = Long.valueOf(SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString());
        cartItemService.removeFromCart(userIdFromContext, itemId);
    }

    @DeleteMapping
    public ResponseEntity<Void> clearCart() {
        Long userIdFromContext = Long.valueOf(SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString());
        cartItemService.clearCart(userIdFromContext);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/exists")
    public boolean isItemInCart(@RequestParam Long itemId) {
        Long userId = Long.valueOf(SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString());
        System.out.println(userId);
        return cartItemService.existsByUserIdAndItemId(userId, itemId);
    }
}

