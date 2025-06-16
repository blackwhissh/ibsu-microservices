package com.ibsu.cart_service.service;

import com.ibsu.cart_service.client.ItemClient;
import com.ibsu.common.dto.CartItemResponseDTO;
import com.ibsu.common.dto.CartResponseDTO;
import com.ibsu.cart_service.dto.ItemDTO;
import com.ibsu.cart_service.model.CartItem;
import com.ibsu.cart_service.repository.CartItemRepository;
import jakarta.transaction.Transactional;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CartItemService {

    private final CachedItemService cachedItemService;

    private final CartItemRepository cartItemRepository;

    public CartItemService(CachedItemService cachedItemService, CartItemRepository cartItemRepository) {
        this.cachedItemService = cachedItemService;
        this.cartItemRepository = cartItemRepository;
    }

    public CartResponseDTO getCartByUserId(Long userId) {
        List<CartItem> cartItems = cartItemRepository.findByUserId(userId);

        List<CartItemResponseDTO> itemDTOs = cartItems.stream()
                .map(this::mapToDTO)
                .toList();

        double total = itemDTOs.stream()
                .mapToDouble(CartItemResponseDTO::getTotal)
                .sum();

        return new CartResponseDTO(itemDTOs, total);
    }

    public CartItem addToCart(Long userId, Long itemId, double priceSnapshot) {
        if (cartItemRepository.existsByUserIdAndItemId(userId, itemId)) {
            throw new IllegalArgumentException("Item already in cart");
        }
        CartItem newItem = new CartItem(itemId, userId, priceSnapshot);
        return cartItemRepository.save(newItem);
    }

    @Transactional
    public void removeFromCart(Long userId, Long itemId) {
        cartItemRepository.deleteByUserIdAndItemId(userId, itemId);
    }

    @Transactional
    public void clearCart(Long userId) {
        cartItemRepository.deleteByUserId(userId);
    }

    public boolean existsByUserIdAndItemId(Long userId, Long itemId) {
        return cartItemRepository.existsByUserIdAndItemId(userId, itemId);
    }

    private CartItemResponseDTO mapToDTO(CartItem item) {
        ItemDTO itemDTO = cachedItemService.getItemById(item.getItemId());

        return new CartItemResponseDTO(
                item.getItemId(),
                itemDTO.getTitle(),
                itemDTO.getImageUrl(),
                item.getPriceSnapshot(),
                item.getPriceSnapshot()
        );
    }

}
