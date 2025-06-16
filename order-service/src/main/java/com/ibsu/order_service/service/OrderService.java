package com.ibsu.order_service.service;

import com.ibsu.common.dto.CartItemResponseDTO;
import com.ibsu.common.dto.CartResponseDTO;
import com.ibsu.common.event.ItemsRemovedEvent;
import com.ibsu.order_service.client.CartClient;
import com.ibsu.order_service.dto.OrderItemResponseDTO;
import com.ibsu.order_service.dto.OrderResponseDTO;
import com.ibsu.order_service.model.Order;
import com.ibsu.order_service.model.OrderItem;
import com.ibsu.order_service.repository.OrderRepository;
import jakarta.transaction.Transactional;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final CartClient cartClient;
    private final KafkaTemplate<String, OrderResponseDTO> orderResponseKafkaTemplate;
    private final KafkaTemplate<String, ItemsRemovedEvent> itemsRemovedKafkaTemplate;

    private static final String NOTIFICATION_TOPIC = "order.checkout";
    private static final String ITEM_TOPIC = "items.removed";

    public OrderService(OrderRepository orderRepository, CartClient cartClient,
                        KafkaTemplate<String, OrderResponseDTO> orderResponseKafkaTemplate, KafkaTemplate<String, ItemsRemovedEvent> itemsRemovedKafkaTemplate) {
        this.orderRepository = orderRepository;
        this.cartClient = cartClient;
        this.orderResponseKafkaTemplate = orderResponseKafkaTemplate;

        this.itemsRemovedKafkaTemplate = itemsRemovedKafkaTemplate;
    }

    @Transactional
    public OrderResponseDTO checkout(Long userId) {
        CartResponseDTO cart = cartClient.getCart();

        if (cart.getItems() == null || cart.getItems().isEmpty()) {
            throw new IllegalArgumentException("Cart is empty");
        }

        List<OrderItem> orderItems = new ArrayList<>();
        for (CartItemResponseDTO cartItem : cart.getItems()) {
            OrderItem orderItem = new OrderItem();
            orderItem.setItemId(cartItem.getItemId());
            orderItem.setItemName(cartItem.getItemName());
            orderItem.setItemImage(cartItem.getItemImage());
            orderItem.setPriceSnapshot(cartItem.getPriceSnapshot());
            orderItems.add(orderItem);
        }

        Order order = new Order();
        order.setUserId(userId);
        order.setTotalPrice(cart.getTotalPrice());
        order.setCreatedAt(Instant.now());
        order.setItems(orderItems);

        for (OrderItem item : orderItems) {
            item.setOrder(order);
        }

        Order savedOrder = orderRepository.save(order);

        cartClient.clearCart();

        List<OrderItemResponseDTO> itemDTOs = new ArrayList<>();
        List<Long> itemIds = new ArrayList<>();
        for (OrderItem item : savedOrder.getItems()) {
            OrderItemResponseDTO itemDTO = new OrderItemResponseDTO();
            itemDTO.setItemId(item.getItemId());
            itemDTO.setItemName(item.getItemName());
            itemDTO.setItemImage(item.getItemImage());
            itemDTO.setPriceSnapshot(item.getPriceSnapshot());
            itemDTOs.add(itemDTO);
            itemIds.add(item.getItemId());
        }

        OrderResponseDTO response = new OrderResponseDTO();
        response.setOrderId(savedOrder.getId());
        response.setUserId(savedOrder.getUserId());
        response.setTotalPrice(savedOrder.getTotalPrice());
        response.setCreatedAt(savedOrder.getCreatedAt());
        response.setItems(itemDTOs);

        orderResponseKafkaTemplate.send(NOTIFICATION_TOPIC, response);

        ItemsRemovedEvent event = new ItemsRemovedEvent(
                itemIds
        );

        itemsRemovedKafkaTemplate.send(ITEM_TOPIC, event);
        return response;
    }
}

