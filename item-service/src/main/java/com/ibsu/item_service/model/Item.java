package com.ibsu.item_service.model;

import com.ibsu.common.enums.ItemStatusEnum;
import jakarta.persistence.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "item")
public class Item {
    @Id
    @SequenceGenerator(name = "item_id_seq", sequenceName = "item_id_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "item_id_seq")
    private Long itemId;
    private String title;
    private String description;
    private Double price;
    private String artist;
    private String size;
    private String medium;
    private String imageUrl;
    private LocalDateTime publishDate;
    private LocalDate paintedDate;
    @Enumerated(EnumType.STRING)
    private ItemStatusEnum itemStatus;

    public Item(String title, String description, String size, String medium, Double price,
                String imageUrl, LocalDate paintedDate, String artist) {
        this.title = title;
        this.description = description;
        this.price = price;
        this.artist = artist;
        this.size = size;
        this.medium = medium;
        this.imageUrl = imageUrl;
        this.publishDate = LocalDateTime.now();
        this.paintedDate = paintedDate;
        this.itemStatus = ItemStatusEnum.AVAILABLE;
    }

    public Item() {
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

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public LocalDateTime getPublishDate() {
        return publishDate;
    }

    public void setPublishDate(LocalDateTime publishDate) {
        this.publishDate = publishDate;
    }

    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
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

    public LocalDate getPaintedDate() {
        return paintedDate;
    }

    public void setPaintedDate(LocalDate paintedDate) {
        this.paintedDate = paintedDate;
    }

    public ItemStatusEnum getItemStatus() {
        return itemStatus;
    }

    public void setItemStatus(ItemStatusEnum itemStatus) {
        this.itemStatus = itemStatus;
    }
}
