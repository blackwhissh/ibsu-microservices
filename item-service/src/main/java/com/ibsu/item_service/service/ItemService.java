package com.ibsu.item_service.service;

import com.ibsu.common.dto.ItemPreviewDTO;
import com.ibsu.common.enums.ItemStatusEnum;
import com.ibsu.item_service.model.Item;
import com.ibsu.item_service.repository.ItemRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class ItemService {
    private final static Logger LOGGER = LoggerFactory.getLogger(ItemService.class);
    private final ItemRepository itemRepository;
    private final CloudinaryService cloudinaryService;

    public ItemService(ItemRepository itemRepository, CloudinaryService cloudinaryService) {
        this.itemRepository = itemRepository;
        this.cloudinaryService = cloudinaryService;
    }

    public Item addItem(String title, String description, String size, String medium,
                        double price, MultipartFile imageFile, LocalDate paintedDate, String artist) {
        LOGGER.info("Adding item: {}", title);
        Map<String, String> imageInfo = cloudinaryService.uploadImage(imageFile, artist);
        String publicId = imageInfo.get("public_id");
        String imageUrl = imageInfo.get("url");
        Item item = new Item(title, description, size, medium, price, imageUrl, paintedDate, artist);
        item.setCloudinaryPublicId(publicId);
        return itemRepository.save(item);
    }

    @Transactional
    public void deleteItems(List<Long> itemIds) {
        LOGGER.info("Deleting items: {}", itemIds);

        List<Item> items = itemRepository.findAllById(itemIds);

        itemRepository.deleteAll(items);
        LOGGER.info("Deleted {} items from DB", items.size());

        TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronization() {
            @Override
            public void afterCommit() {
                for (Item item : items) {
                    String publicId = item.getCloudinaryPublicId();
                    if (publicId != null && !publicId.isBlank()) {
                        try {
                            cloudinaryService.deleteImage(publicId);
                            LOGGER.info("Deleted Cloudinary image: {}", publicId);
                        } catch (Exception e) {
                            LOGGER.error("Failed to delete Cloudinary image for item {}: {}", item.getItemId(), e.getMessage());
                        }
                    }
                }
            }
        });
    }

    public void deleteItem(Long itemId) {
        Item item = itemRepository.findById(itemId)
                .orElseThrow(() -> new RuntimeException("Item not found"));

        if (item.getCloudinaryPublicId() != null) {
            cloudinaryService.deleteImage(item.getCloudinaryPublicId());
        }

        itemRepository.delete(item);
    }

    public Page<Item> getAllAvailableItems(Pageable pageable) {
        LOGGER.info("Getting all items");
        return itemRepository.findAllByItemStatus(ItemStatusEnum.AVAILABLE, pageable);
    }

    @Transactional
    public void editStatusByIdList(List<Long> itemIds, ItemStatusEnum status) {
        LOGGER.info("Updating item status");
        itemRepository.updateItemsStatusByIds(itemIds, status);
    }

    public Item getItemById(Long id) {
        LOGGER.info("Getting item by id: {}", id);
        return itemRepository.findById(id).orElseThrow();
    }

    public Item updateItemById(Long id, Optional<String> title, Optional<String> description, Optional<Double> price,
                               Optional<String> artist, Optional<String> size, Optional<String> medium,
                               Optional<MultipartFile> imageFile, Optional<LocalDate> paintedDate) {
        LOGGER.info("Updating item: {}", id);
        Item item = getItemById(id);
        title.ifPresent(item::setTitle);
        description.ifPresent(item::setDescription);
        price.ifPresent(item::setPrice);
        artist.ifPresent(item::setArtist);
        size.ifPresent(item::setSize);
        medium.ifPresent(item::setMedium);
        if (imageFile.isPresent()) {
            Map<String, String> imageInfo = cloudinaryService.uploadImage(imageFile.get(), item.getArtist());
            item.setCloudinaryPublicId(imageInfo.get("public_id"));
            item.setImageUrl(imageInfo.get("url"));
        }
        paintedDate.ifPresent(item::setPaintedDate);

        return itemRepository.save(item);
    }

    public List<Item> getItemsByIds(List<Long> itemIds) {
        LOGGER.info("Getting items by ids: {}", itemIds);
        return itemRepository.findAllById(itemIds);
    }

    public ItemStatusEnum getItemStatus(Long itemId) {
        return itemRepository.getItemStatusByItemId(itemId);
    }
}
