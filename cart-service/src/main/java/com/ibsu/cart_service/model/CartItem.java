package com.ibsu.cart_service.model;

import jakarta.persistence.*;

@Entity
public class CartItem {
    @Id
    @SequenceGenerator(name = "cart_item_id_seq", sequenceName = "cart_item_id_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "cart_item_id_seq")
    private Long cartItemId;
    private Long itemId;
    private Long userId;
    private Double priceSnapshot;
    private String artistName;

    public CartItem(Long itemId, Long userId, Double priceSnapshot, String artistName) {
        this.itemId = itemId;
        this.userId = userId;
        this.priceSnapshot = priceSnapshot;
        this.artistName = artistName;
    }
    public CartItem() {
    }

    public Long getCartItemId() {
        return cartItemId;
    }

    public void setCartItemId(Long cartItemId) {
        this.cartItemId = cartItemId;
    }

    public Long getItemId() {
        return itemId;
    }

    public void setItemId(Long itemId) {
        this.itemId = itemId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Double getPriceSnapshot() {
        return priceSnapshot;
    }

    public void setPriceSnapshot(Double priceSnapshot) {
        this.priceSnapshot = priceSnapshot;
    }

    public String getArtistName() {
        return artistName;
    }

    public void setArtistName(String artistName) {
        this.artistName = artistName;
    }
}
