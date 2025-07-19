package com.ibsu.common.dto;

public class CartItemResponseDTO {
    private Long itemId;
    private String itemName;
    private String itemImage;
    private double priceSnapshot;
    private double total;
    private String artistName;

    public CartItemResponseDTO(Long itemId, String itemName, String itemImage, double priceSnapshot, double total, String artistName) {
        this.itemId = itemId;
        this.itemName = itemName;
        this.itemImage = itemImage;
        this.priceSnapshot = priceSnapshot;
        this.total = total;
        this.artistName = artistName;
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

    public String getArtistName() {
        return artistName;
    }

    public void setArtistName(String artistName) {
        this.artistName = artistName;
    }
}
