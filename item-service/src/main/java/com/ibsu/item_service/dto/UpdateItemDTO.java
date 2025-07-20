package com.ibsu.item_service.dto;

import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.util.Optional;

public record UpdateItemDTO (Long id, Optional<String> title, Optional<String> description, Optional<Double> price,
                             Optional<String> artist, Optional<String> size, Optional<String> medium,
                             Optional<MultipartFile> imageFile, Optional<LocalDate> paintedDate) {

}
