package com.restaurant.service.order.service;

import com.restaurant.common.dto.CreateOrderRequest;
import com.restaurant.common.dto.OrderResponse;
import com.restaurant.common.entity.MenuItem;
import com.restaurant.common.entity.Order;
import com.restaurant.common.entity.OrderItem;
import com.restaurant.common.event.OrderCreatedEvent;
import com.restaurant.common.event.OrderStatusUpdatedEvent;
import com.restaurant.service.order.client.RestaurantServiceClient;
import com.restaurant.service.order.repository.OrderItemRepository;
import com.restaurant.service.order.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class OrderService {
    
    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;
    private final RestaurantServiceClient restaurantServiceClient;
    private final KafkaTemplate<String, Object> kafkaTemplate;
    
    private static final String ORDER_CREATED_TOPIC = "order-created";
    private static final String ORDER_STATUS_UPDATED_TOPIC = "order-status-updated";
    
    public OrderResponse createOrder(CreateOrderRequest request) {
        log.info("Creating order for customer: {} at restaurant: {}", 
                request.getCustomerId(), request.getRestaurantId());
        
        // Validate menu items and calculate total
        BigDecimal totalAmount = BigDecimal.ZERO;
        List<OrderItem> orderItems = new ArrayList<>();
        
        for (CreateOrderRequest.OrderItemRequest itemRequest : request.getItems()) {
            // Fetch menu item from restaurant service
            var menuItemResponse = restaurantServiceClient.getMenuItem(
                request.getRestaurantId(), itemRequest.getMenuItemId());
            
            if (!menuItemResponse.isSuccess()) {
                throw new RuntimeException("Menu item not found: " + itemRequest.getMenuItemId());
            }
            
            MenuItem menuItem = menuItemResponse.getData();
            BigDecimal itemTotal = menuItem.getPrice().multiply(BigDecimal.valueOf(itemRequest.getQuantity()));
            totalAmount = totalAmount.add(itemTotal);
            
            OrderItem orderItem = OrderItem.builder()
                    .menuItemId(menuItem.getId())
                    .menuItemName(menuItem.getName())
                    .quantity(itemRequest.getQuantity())
                    .unitPrice(menuItem.getPrice())
                    .totalPrice(itemTotal)
                    .specialInstructions(itemRequest.getSpecialInstructions())
                    .customizations(itemRequest.getCustomizations())
                    .build();
            
            orderItems.add(orderItem);
        }
        
        // Create order
        Order order = Order.builder()
                .customerId(request.getCustomerId())
                .restaurantId(request.getRestaurantId())
                .status(Order.OrderStatus.PENDING)
                .totalAmount(totalAmount)
                .taxAmount(calculateTax(totalAmount))
                .orderType(Order.OrderType.valueOf(request.getOrderType()))
                .deliveryAddress(request.getDeliveryAddress())
                .specialInstructions(request.getSpecialInstructions())
                .paymentMethod(request.getPaymentMethod())
                .paymentStatus(Order.PaymentStatus.PENDING)
                .estimatedDeliveryTime(calculateEstimatedDeliveryTime(request.getOrderType()))
                .build();
        
        Order savedOrder = orderRepository.save(order);
        
        // Save order items
        for (OrderItem item : orderItems) {
            item.setOrder(savedOrder);
            orderItemRepository.save(item);
        }
        
        // Publish order created event
        OrderCreatedEvent event = new OrderCreatedEvent(
                savedOrder.getId(),
                savedOrder.getCustomerId(),
                savedOrder.getRestaurantId(),
                savedOrder.getTotalAmount(),
                savedOrder.getOrderType().toString(),
                savedOrder.getDeliveryAddress(),
                savedOrder.getEstimatedDeliveryTime()
        );
        
        kafkaTemplate.send(ORDER_CREATED_TOPIC, event);
        log.info("Published order created event for order: {}", savedOrder.getId());
        
        return mapToOrderResponse(savedOrder, orderItems);
    }
    
    public Optional<OrderResponse> getOrderById(Long orderId) {
        return orderRepository.findById(orderId)
                .map(order -> {
                    List<OrderItem> items = orderItemRepository.findByOrderId(orderId);
                    return mapToOrderResponse(order, items);
                });
    }
    
    public Optional<OrderResponse> getOrderByIdAndCustomer(Long orderId, Long customerId) {
        return orderRepository.findByIdAndCustomerId(orderId, customerId)
                .map(order -> {
                    List<OrderItem> items = orderItemRepository.findByOrderId(orderId);
                    return mapToOrderResponse(order, items);
                });
    }
    
    public List<OrderResponse> getOrdersByCustomer(Long customerId) {
        List<Order> orders = orderRepository.findRecentOrdersByCustomer(customerId);
        return orders.stream()
                .map(order -> {
                    List<OrderItem> items = orderItemRepository.findByOrderId(order.getId());
                    return mapToOrderResponse(order, items);
                })
                .toList();
    }
    
    public List<OrderResponse> getOrdersByRestaurant(Long restaurantId) {
        List<Order> orders = orderRepository.findByRestaurantId(restaurantId);
        return orders.stream()
                .map(order -> {
                    List<OrderItem> items = orderItemRepository.findByOrderId(order.getId());
                    return mapToOrderResponse(order, items);
                })
                .toList();
    }
    
    public Optional<OrderResponse> updateOrderStatus(Long orderId, Order.OrderStatus newStatus, String reason) {
        return orderRepository.findById(orderId)
                .map(order -> {
                    Order.OrderStatus previousStatus = order.getStatus();
                    order.setStatus(newStatus);
                    
                    // Update delivery time if delivered
                    if (newStatus == Order.OrderStatus.DELIVERED) {
                        order.setActualDeliveryTime(LocalDateTime.now());
                    }
                    
                    Order updatedOrder = orderRepository.save(order);
                    
                    // Publish status update event
                    OrderStatusUpdatedEvent event = new OrderStatusUpdatedEvent(
                            orderId,
                            previousStatus.toString(),
                            newStatus.toString(),
                            reason,
                            order.getRestaurantId(),
                            order.getCustomerId()
                    );
                    
                    kafkaTemplate.send(ORDER_STATUS_UPDATED_TOPIC, event);
                    log.info("Updated order {} status from {} to {}", orderId, previousStatus, newStatus);
                    
                    List<OrderItem> items = orderItemRepository.findByOrderId(orderId);
                    return mapToOrderResponse(updatedOrder, items);
                });
    }
    
    public boolean cancelOrder(Long orderId, Long customerId) {
        return orderRepository.findByIdAndCustomerId(orderId, customerId)
                .map(order -> {
                    if (order.getStatus() == Order.OrderStatus.PENDING || 
                        order.getStatus() == Order.OrderStatus.CONFIRMED) {
                        updateOrderStatus(orderId, Order.OrderStatus.CANCELLED, "Cancelled by customer");
                        return true;
                    }
                    return false;
                })
                .orElse(false);
    }
    
    private BigDecimal calculateTax(BigDecimal amount) {
        // Simple tax calculation - 8% tax rate
        return amount.multiply(BigDecimal.valueOf(0.08));
    }
    
    private LocalDateTime calculateEstimatedDeliveryTime(String orderType) {
        // Simple estimation logic
        int estimatedMinutes = switch (Order.OrderType.valueOf(orderType)) {
            case DINE_IN -> 30;
            case TAKEAWAY -> 20;
            case DELIVERY -> 45;
        };
        
        return LocalDateTime.now().plusMinutes(estimatedMinutes);
    }
    
    private OrderResponse mapToOrderResponse(Order order, List<OrderItem> items) {
        List<OrderResponse.OrderItemResponse> itemResponses = items.stream()
                .map(item -> new OrderResponse.OrderItemResponse(
                        item.getId(),
                        item.getMenuItemId(),
                        item.getMenuItemName(),
                        item.getQuantity(),
                        item.getUnitPrice(),
                        item.getTotalPrice(),
                        item.getSpecialInstructions(),
                        item.getCustomizations()
                ))
                .toList();
        
        return new OrderResponse(
                order.getId(),
                order.getCustomerId(),
                order.getRestaurantId(),
                order.getStatus().toString(),
                order.getTotalAmount(),
                order.getTaxAmount(),
                order.getDiscountAmount(),
                order.getOrderType().toString(),
                order.getDeliveryAddress(),
                order.getSpecialInstructions(),
                order.getEstimatedDeliveryTime(),
                order.getActualDeliveryTime(),
                order.getPaymentMethod(),
                order.getPaymentStatus().toString(),
                order.getCreatedAt(),
                order.getUpdatedAt(),
                itemResponses
        );
    }
}