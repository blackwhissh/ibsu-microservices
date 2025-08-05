package com.ibsu.item_service.controller;

import com.ibsu.common.enums.ItemStatusEnum;
import com.ibsu.item_service.dto.AddItemDTO;
import com.ibsu.item_service.dto.UpdateItemDTO;
import com.ibsu.item_service.service.ItemService;
import com.ibsu.common.dto.ItemPreviewDTO;
import com.ibsu.item_service.model.Item;
import com.ibsu.item_service.repository.ItemRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/item")
public class ItemController {
    private final ItemService itemService;
    private final SimpMessagingTemplate messagingTemplate;

    public ItemController(ItemService itemService, SimpMessagingTemplate messagingTemplate) {
        this.itemService = itemService;
        this.messagingTemplate = messagingTemplate;
    }

    @GetMapping("/get/{itemId}")
    public ResponseEntity<Item> getItem(@PathVariable Long itemId) {
        return ResponseEntity.ok(itemService.getItemById(itemId));
    }

    @GetMapping("/get-all")
    public ResponseEntity<Page<Item>> getAllAvailableItems(
            @PageableDefault(page = 0, size = 9, sort = "publishDate", direction = Sort.Direction.DESC)
            Pageable pageable) {
        return ResponseEntity.ok(itemService.getAllAvailableItems(pageable));
    }

    @GetMapping("/admin/items")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Page<Item>> getAllItems(
            @PageableDefault(page = 0, size = 9, sort = "publishDate", direction = Sort.Direction.DESC)
            Pageable pageable) {
        return ResponseEntity.ok(itemService.getAllItems(pageable));
    }

    @DeleteMapping("/admin/delete-items")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<?> deleteItems(@RequestBody List<Long> itemId) {
        itemService.deleteItems(itemId);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/admin/delete")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<?> deleteItem(@RequestParam Long itemId) {
        itemService.deleteItem(itemId);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/admin/update")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<Item> updateItem(@RequestBody UpdateItemDTO updateItemDTO) {
        return ResponseEntity.ok(itemService.updateItemById(
                updateItemDTO.id(), updateItemDTO.title(), updateItemDTO.description(), updateItemDTO.price(),
                updateItemDTO.artist(), updateItemDTO.size(), updateItemDTO.medium(), updateItemDTO.imageFile(),
                updateItemDTO.paintedDate()
        ));
    }

    @PostMapping(value = "/admin/add", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ItemPreviewDTO addItem(@ModelAttribute AddItemDTO addItemDTO) {
        Item saved = itemService.addItem(
                addItemDTO.getTitle(), addItemDTO.getDescription(), addItemDTO.getSize(),
                addItemDTO.getMedium(), addItemDTO.getPrice(), addItemDTO.getImageFile(),
                addItemDTO.getPaintedDate(), addItemDTO.getArtist()
        );

        ItemPreviewDTO dto = mapToDto(saved);
        messagingTemplate.convertAndSend("/topic/items", dto);
        return dto;
    }

    @GetMapping("/get-by-ids")
    public ResponseEntity<List<ItemPreviewDTO>> getItemsByIds(@RequestBody List<Long> itemIds) {
        List<ItemPreviewDTO> itemPreviewDTOS = new ArrayList<>();
        for (Long itemId : itemIds) {
            itemPreviewDTOS.add(mapToDto(itemService.getItemById(itemId)));
        }
        return ResponseEntity.ok(itemPreviewDTOS);
    }

    @GetMapping("/internal/item/{itemId}/status")
    public ItemStatusEnum getItemStatusInternal(@PathVariable Long itemId) {
        return itemService.getItemStatus(itemId);
    }

    private ItemPreviewDTO mapToDto(Item item) {
        return new ItemPreviewDTO(
                item.getItemId(),
                item.getTitle(),
                item.getDescription(),
                item.getPrice(),
                item.getArtist(),
                item.getImageUrl()
        );
    }
}
