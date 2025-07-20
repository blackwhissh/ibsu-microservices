package com.ibsu.order_service.service;

import com.ibsu.common.dto.CartItemResponseDTO;
import com.ibsu.common.dto.CartResponseDTO;
import com.ibsu.common.dto.OrderItemResponseDTO;
import com.ibsu.common.enums.DeliveryTypeEnum;
import com.ibsu.common.enums.OrderStatusEnum;
import com.ibsu.common.event.ItemsReservedEvent;
import com.ibsu.common.event.OrderCanceledEvent;
import com.ibsu.common.event.OrderConfirmedEvent;
import com.ibsu.common.exceptions.OrderItemsNotFoundException;
import com.ibsu.common.exceptions.OrderNotFoundException;
import com.ibsu.order_service.client.CartClient;
import com.ibsu.order_service.dto.OrderHistoryDTO;
import com.ibsu.order_service.dto.OrderResponseDTO;
import com.ibsu.order_service.model.Order;
import com.ibsu.order_service.model.OrderItem;
import com.ibsu.order_service.repository.OrderItemRepository;
import com.ibsu.order_service.repository.OrderRepository;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final CartClient cartClient;
    private final KafkaTemplate<String, OrderResponseDTO> orderCheckoutKafkaTemplate;
    private final KafkaTemplate<String, ItemsReservedEvent> itemsReservedKafkaTemplate;
    private final KafkaTemplate<String, OrderCanceledEvent> orderCancelledKafkaTemplate;
    private final KafkaTemplate<String, OrderConfirmedEvent> orderConfirmedKafkaTemplate;
    private final OrderItemRepository orderItemRepository;

    private static final String CHECKOUT_NOTIFICATION_TOPIC = "order.checkout";
    private static final String ORDER_CONFIRMED_NOTIFICATION_TOPIC = "order.confirmed";
    private static final String ORDER_APPROVED_TOPIC = "order.approved";
    private static final String ITEM_RESERVED_TOPIC = "items.reserved";
    private static final String ORDER_CANCELED_TOPIC = "order.cancelled";


    public OrderService(OrderRepository orderRepository, CartClient cartClient,
                        KafkaTemplate<String, OrderResponseDTO> orderCheckoutKafkaTemplate,
                        KafkaTemplate<String, ItemsReservedEvent> itemsReservedKafkaTemplate,
                        KafkaTemplate<String, OrderCanceledEvent> orderCancelledKafkaTemplate,
                        KafkaTemplate<String, OrderConfirmedEvent> orderConfirmedKafkaTemplate,
                        OrderItemRepository orderItemRepository) {
        this.orderRepository = orderRepository;
        this.cartClient = cartClient;
        this.orderCheckoutKafkaTemplate = orderCheckoutKafkaTemplate;

        this.itemsReservedKafkaTemplate = itemsReservedKafkaTemplate;
        this.orderCancelledKafkaTemplate = orderCancelledKafkaTemplate;
        this.orderConfirmedKafkaTemplate = orderConfirmedKafkaTemplate;
        this.orderItemRepository = orderItemRepository;
    }

    @Transactional
    public OrderResponseDTO checkout(Long userId, DeliveryTypeEnum deliveryType) {
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
            orderItem.setArtistName(cartItem.getArtistName());
            orderItems.add(orderItem);
        }
        double totalPrice = cart.getTotalPrice();
        switch (deliveryType) {
            case OUT -> totalPrice += 10;
            case TBILISI -> totalPrice += 5;
        }
        Order order = new Order();
        order.setUserId(userId);
        order.setTotalPrice(totalPrice);
        order.setCreatedAt(Instant.now());
        order.setItems(orderItems);
        order.setOrderStatus(OrderStatusEnum.PENDING);
        order.setDeliveryType(deliveryType);
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
            itemDTO.setArtistName(item.getArtistName());
            itemDTOs.add(itemDTO);
            itemIds.add(item.getItemId());
        }

        OrderResponseDTO response = new OrderResponseDTO();
        response.setOrderId(savedOrder.getId());
        response.setUserId(savedOrder.getUserId());
        response.setTotalPrice(savedOrder.getTotalPrice());
        response.setCreatedAt(savedOrder.getCreatedAt());
        response.setItems(itemDTOs);
        response.setDeliveryType(deliveryType);

        orderCheckoutKafkaTemplate.send(CHECKOUT_NOTIFICATION_TOPIC, response);

        ItemsReservedEvent event = new ItemsReservedEvent(
                itemIds
        );

        itemsReservedKafkaTemplate.send(ITEM_RESERVED_TOPIC, event);
        return response;
    }

    public Page<OrderHistoryDTO> getUserOrderHistory(Long userId, Pageable pageable) {
        Page<Order> ordersPage = orderRepository.findAllByUserId(userId, pageable);
        return ordersPage.map(order -> {
            OrderItem firstItem = order.getItems().isEmpty() ? null : order.getItems().get(0);
            return new OrderHistoryDTO(
                    order.getId(),
                    userId,
                    order.getTotalPrice(),
                    order.getCreatedAt(),
                    firstItem != null ? firstItem.getItemImage() : null,
                    order.getOrderStatus(),
                    order.getDeliveryType()
            );
        });
    }

    public OrderResponseDTO getOrderDetails(Long userId, Long id) {
        Order order = orderRepository.findById(id).orElseThrow(() -> new OrderNotFoundException("Order not found: " + id));
        List<OrderItemResponseDTO> orderItems = orderItemRepository.findByOrderIdAndOrder_UserId(id, userId);
        if (orderItems == null || orderItems.isEmpty()) {
            throw new  OrderItemsNotFoundException("Order items not found!");
        }
        return new OrderResponseDTO(
                order.getId(), userId, order.getTotalPrice(),
                order.getCreatedAt(), orderItems, order.getOrderStatus(), order.getDeliveryType()
        );
    }

    public void deleteOrderByCurrentUser(Long userId, Long id) {
        OrderResponseDTO orderResponseDTO = getOrderDetails(userId, id);
        if (orderResponseDTO != null) {
            orderRepository.deleteById(id);
            orderCancelledKafkaTemplate.send(ORDER_CANCELED_TOPIC,
                    new OrderCanceledEvent(getItemIds(orderResponseDTO.getItems()), orderResponseDTO.getOrderId()));
        } else {
            throw new OrderNotFoundException("Order not found: " + id);
        }
    }

    public void confirmOrder(Long userId, Long id) {
        OrderResponseDTO orderResponseDTO = getOrderDetails(userId, id);
        if (orderResponseDTO != null) {
            orderConfirmedKafkaTemplate.send(ORDER_CONFIRMED_NOTIFICATION_TOPIC,
                    new OrderConfirmedEvent(id, userId, getItemIds(orderResponseDTO.getItems())));
        } else {
            throw new OrderNotFoundException("Order not found: " + id);
        }
    }

    public void approveOrder(Long userId, Long id) {
        OrderResponseDTO orderResponseDTO = getOrderDetails(userId, id);
        if (orderResponseDTO != null) {
            orderConfirmedKafkaTemplate.send(ORDER_APPROVED_TOPIC, new OrderConfirmedEvent(id, userId, getItemIds(orderResponseDTO.getItems())));
        } else {
            throw new OrderNotFoundException("Order not found: " + id);
        }
    }

    private List<Long> getItemIds(List<OrderItemResponseDTO> orderItems) {
        List<Long> itemIds = new ArrayList<>();
        for (OrderItemResponseDTO orderItem : orderItems) {
            itemIds.add(orderItem.getItemId());
        }
        return itemIds;
    }
}

