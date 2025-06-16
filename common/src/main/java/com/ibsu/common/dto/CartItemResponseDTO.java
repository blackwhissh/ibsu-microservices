package com.ibsu.common.dto;

public class CartItemResponseDTO {
    private Long itemId;
    private String itemName;
    private String itemImage;
    private double priceSnapshot;
    private double total;

    public CartItemResponseDTO(Long itemId, String itemName, String itemImage, double priceSnapshot, double total) {
        this.itemId = itemId;
        this.itemName = itemName;
        this.itemImage = itemImage;
        this.priceSnapshot = priceSnapshot;
        this.total = total;
    }

    public CartItemResponseDTO() {
    }

    public Long getItemId() {
        return itemId;
    }

    public void setItemId(Long itemId) {
        this.itemId = itemId;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public String getItemImage() {
        return itemImage;
    }

    public void setItemImage(String itemImage) {
        this.itemImage = itemImage;
    }

    public double getPriceSnapshot() {
        return priceSnapshot;
    }

    public void setPriceSnapshot(double priceSnapshot) {
        this.priceSnapshot = priceSnapshot;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }
}
