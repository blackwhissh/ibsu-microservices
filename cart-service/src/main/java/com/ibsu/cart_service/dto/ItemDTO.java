package com.ibsu.cart_service.dto;

import java.io.Serializable;

public class ItemDTO implements Serializable {
    private Long userId;
    private String title;
    private String imageUrl;
    public ItemDTO() {
    }
    public ItemDTO(Long userId, String title, String imageUrl) {
        this.userId = userId;
        this.title = title;
        this.imageUrl = imageUrl;
    }

    public Long getUserId() {
        return userId;
    }
    public void setUserId(Long userId) {
        this.userId = userId;
    }
    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }
    public String getImageUrl() {
        return imageUrl;
    }
    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

}
