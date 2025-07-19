package com.ibsu.item_service.dto;

import com.ibsu.common.enums.ItemStatusEnum;

import java.time.LocalDate;

public record AddItemDTO(String title, String description, String size, String medium,
                         double price, String imageUrl, LocalDate paintedDate, String artist,
                         ItemStatusEnum itemStatus) {
}
