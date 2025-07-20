package com.ibsu.item_service.dto;

import com.ibsu.common.enums.ItemStatusEnum;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;

public class AddItemDTO {
    private String title;
    private String description;
    private String size;
    private String medium;
    private double price;
    private MultipartFile imageFile;
    private LocalDate paintedDate;
    private String artist;
    private ItemStatusEnum itemStatus;

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

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String getMedium() {
        return medium;
    }

    public void setMedium(String medium) {
        this.medium = medium;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public MultipartFile getImageFile() {
        return imageFile;
    }

    public void setImageFile(MultipartFile imageFile) {
        this.imageFile = imageFile;
    }

    public LocalDate getPaintedDate() {
        return paintedDate;
    }

    public void setPaintedDate(LocalDate paintedDate) {
        this.paintedDate = paintedDate;
    }

    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public ItemStatusEnum getItemStatus() {
        return itemStatus;
    }

    public void setItemStatus(ItemStatusEnum itemStatus) {
        this.itemStatus = itemStatus;
    }
}
