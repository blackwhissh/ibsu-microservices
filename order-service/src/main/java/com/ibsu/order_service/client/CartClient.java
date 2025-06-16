package com.ibsu.order_service.client;

import com.ibsu.common.dto.CartResponseDTO;
import com.ibsu.order_service.config.FeignClientConfig;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(value = "cart-service", configuration = FeignClientConfig.class)
public interface CartClient {
    @GetMapping("/cart")
    CartResponseDTO getCart();
    @DeleteMapping("/cart")
    void clearCart();
}
