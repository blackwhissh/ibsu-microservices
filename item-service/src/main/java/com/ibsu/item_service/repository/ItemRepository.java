package com.ibsu.item_service.repository;

import com.ibsu.common.enums.ItemStatusEnum;
import com.ibsu.item_service.model.Item;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ItemRepository extends JpaRepository<Item, Long> {
    Page<Item> findAllByItemStatus(ItemStatusEnum itemStatus, Pageable pageable);
    @Modifying
    @Query("UPDATE Item i SET i.itemStatus = :status WHERE i.itemId IN :itemIds")
    void updateItemsStatusByIds(@Param("itemIds") List<Long> itemIds, @Param("status") ItemStatusEnum status);
}
