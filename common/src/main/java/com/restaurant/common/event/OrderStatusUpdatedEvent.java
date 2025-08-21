package com.restaurant.common.event;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class OrderStatusUpdatedEvent extends BaseEvent {
    private Long orderId;
    private String previousStatus;
    private String newStatus;
    private String reason;
    private Long restaurantId;
    private Long customerId;
    
    public OrderStatusUpdatedEvent(Long orderId, String previousStatus, String newStatus, 
                                 String reason, Long restaurantId, Long customerId) {
        super("ORDER_STATUS_UPDATED", "order-service");
        this.orderId = orderId;
        this.previousStatus = previousStatus;
        this.newStatus = newStatus;
        this.reason = reason;
        this.restaurantId = restaurantId;
        this.customerId = customerId;
    }
}