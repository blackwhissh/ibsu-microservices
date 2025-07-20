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

    @PostMapping("/add")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ItemPreviewDTO addItem(@RequestBody AddItemDTO addItemDTO) {
        Item saved = itemService.addItem(
                addItemDTO.title(), addItemDTO.description(), addItemDTO.size(),
                addItemDTO.medium(), addItemDTO.price(), addItemDTO.imageUrl(),
                addItemDTO.paintedDate(), addItemDTO.artist()
        );

        ItemPreviewDTO dto = mapToDto(saved);
        messagingTemplate.convertAndSend("/topic/items", dto);
        return dto;
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

    @DeleteMapping("/delete-items")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<?> deleteItem(@RequestBody List<Long> itemId) {
        itemService.deleteItems(itemId);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/update")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<Item> updateItem(@RequestBody UpdateItemDTO updateItemDTO) {
        return ResponseEntity.ok(itemService.updateItemById(
                updateItemDTO.id(), updateItemDTO.title(), updateItemDTO.description(), updateItemDTO.price(),
                updateItemDTO.artist(), updateItemDTO.size(), updateItemDTO.medium(), updateItemDTO.imageUrl(),
                updateItemDTO.paintedDate()
        ));
    }

    @GetMapping("/get-by-ids")
    public ResponseEntity<List<ItemPreviewDTO>> getItemsByIds(@RequestBody List<Long> itemIds) {
        List<ItemPreviewDTO> itemPreviewDTOS = new ArrayList<>();
        for (Long itemId : itemIds) {
            itemPreviewDTOS.add(mapToDto(itemService.getItemById(itemId)));
        }
        return ResponseEntity.ok(itemPreviewDTOS);
    }

    @GetMapping("/item/get/{itemId}/status")
    public ItemStatusEnum getItemStatus(@PathVariable("itemId") Long itemId) {
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
