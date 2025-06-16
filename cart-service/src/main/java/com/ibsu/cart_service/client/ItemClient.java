package com.ibsu.cart_service.client;

import com.ibsu.cart_service.config.FeignClientConfig;
import com.ibsu.cart_service.dto.ItemDTO;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.client.RestTemplate;

@FeignClient(name = "item-service", configuration = FeignClientConfig.class)
public interface ItemClient {

    @GetMapping("/item/get/{itemId}")
    ItemDTO getItemById(@PathVariable("itemId") Long itemId);
}
