package com.restaurant.common.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateOrderRequest {
    
    @NotNull
    private Long customerId;
    
    @NotNull
    private Long restaurantId;
    
    @NotBlank
    private String orderType; // DINE_IN, TAKEAWAY, DELIVERY
    
    private String deliveryAddress;
    
    private String specialInstructions;
    
    @NotNull
    private List<OrderItemRequest> items;
    
    private String paymentMethod;
    
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class OrderItemRequest {
        @NotNull
        private Long menuItemId;
        
        @NotNull
        @Positive
        private Integer quantity;
        
        private String specialInstructions;
        
        private String customizations;
    }
}