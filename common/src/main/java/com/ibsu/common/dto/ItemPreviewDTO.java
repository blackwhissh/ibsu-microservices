package com.ibsu.common.dto;

public class ItemPreviewDTO {
    private Long itemId;
    private String title;
    private String description;
    private Double price;
    private String artist;
    private String imageUrl;

    public ItemPreviewDTO(Long itemId, String title, String description, Double price, String artist, String imageUrl) {
        this.itemId = itemId;
        this.title = title;
        this.description = description;
        this.price = price;
        this.artist = artist;
        this.imageUrl = imageUrl;
    }

    public Long getItemId() {
        return itemId;
    }

    public void setItemId(Long itemId) {
        this.itemId = itemId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}
