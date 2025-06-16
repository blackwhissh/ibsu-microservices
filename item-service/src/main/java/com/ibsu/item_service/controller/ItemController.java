package com.ibsu.item_service.controller;

import com.ibsu.item_service.ItemService;
import com.ibsu.item_service.dto.ItemPreviewDTO;
import com.ibsu.item_service.model.Item;
import com.ibsu.item_service.repository.ItemRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/item")
public class ItemController {
    private final ItemService itemService;
    private final ItemRepository itemRepository;
    private final SimpMessagingTemplate messagingTemplate;

    public ItemController(ItemService itemService, ItemRepository itemRepository, SimpMessagingTemplate messagingTemplate) {
        this.itemService = itemService;
        this.itemRepository = itemRepository;
        this.messagingTemplate = messagingTemplate;
    }

    @PostMapping("/add")
    public ItemPreviewDTO addItem(@RequestBody Item item) {
        Item saved = itemService.addItem(item);

        ItemPreviewDTO dto = mapToDto(saved);
        messagingTemplate.convertAndSend("/topic/items", dto);
        return dto;
    }

    @GetMapping("/get/{itemId}")
    public Item getItem(@PathVariable Long itemId) {
        return itemRepository.findById(itemId).orElseThrow();
    }

    @GetMapping("/get-all")
    public List<Item> getAllItems() {
        return itemRepository.findAll();
    }

    @DeleteMapping("/delete-items")
    public ResponseEntity<?> deleteItem(@RequestBody List<Long> itemId) {
        itemService.deleteItems(itemId);
        return ResponseEntity.noContent().build();
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
