package com.restaurant.common.event;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class OrderCreatedEvent extends BaseEvent {
    private Long orderId;
    private Long customerId;
    private Long restaurantId;
    private BigDecimal totalAmount;
    private String orderType;
    private String deliveryAddress;
    private LocalDateTime estimatedDeliveryTime;
    
    public OrderCreatedEvent(Long orderId, Long customerId, Long restaurantId, 
                           BigDecimal totalAmount, String orderType, String deliveryAddress,
                           LocalDateTime estimatedDeliveryTime) {
        super("ORDER_CREATED", "order-service");
        this.orderId = orderId;
        this.customerId = customerId;
        this.restaurantId = restaurantId;
        this.totalAmount = totalAmount;
        this.orderType = orderType;
        this.deliveryAddress = deliveryAddress;
        this.estimatedDeliveryTime = estimatedDeliveryTime;
    }
}