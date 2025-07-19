package com.ibsu.common.dto;

public class OrderItemResponseDTO {
    private Long itemId;
    private String itemName;
    private String artistName;
    private String itemImage;
    private Double priceSnapshot;

    public OrderItemResponseDTO(Long itemId, String itemName, String artistName, String itemImage, Double priceSnapshot) {
        this.itemId = itemId;
        this.itemName = itemName;
        this.artistName = artistName;
        this.itemImage = itemImage;
        this.priceSnapshot = priceSnapshot;
    }

    public OrderItemResponseDTO() {
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