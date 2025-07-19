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

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class ItemService {
    private final static Logger LOGGER = LoggerFactory.getLogger(ItemService.class);
    private final ItemRepository itemRepository;

    public ItemService(ItemRepository itemRepository) {
        this.itemRepository = itemRepository;
    }

    public Item addItem(String title, String description, String size, String medium,
                        double price, String imageUrl, LocalDate paintedDate, String artist) {
        LOGGER.info("Adding item: {}", title);
        Item item = new Item(title, description, size, medium, price, imageUrl, paintedDate, artist);
        return itemRepository.save(item);
    }

    public void deleteItems(List<Long> itemIds) {
        LOGGER.info("Deleting items: {}", itemIds);
        itemRepository.deleteAllById(itemIds);
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
                               Optional<String> imageUrl, Optional<LocalDate> paintedDate) {
        LOGGER.info("Updating item: {}", id);
        Item item = getItemById(id);
        title.ifPresent(item::setTitle);
        description.ifPresent(item::setDescription);
        price.ifPresent(item::setPrice);
        artist.ifPresent(item::setArtist);
        size.ifPresent(item::setSize);
        medium.ifPresent(item::setMedium);
        imageUrl.ifPresent(item::setImageUrl);
        paintedDate.ifPresent(item::setPaintedDate);

        return itemRepository.save(item);
    }

    public List<Item> getItemsByIds(List<Long> itemIds) {
        LOGGER.info("Getting items by ids: {}", itemIds);
        return itemRepository.findAllById(itemIds);
    }
}
