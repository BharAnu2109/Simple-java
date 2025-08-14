package com.hotel.management.common.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class InventoryDto {
    
    private Long id;
    
    @NotBlank(message = "Item name is required")
    private String itemName;
    
    @NotBlank(message = "Category is required")
    private String category; // CLEANING, MAINTENANCE, FOOD, BEVERAGE, AMENITIES, OFFICE
    
    private String description;
    private String sku;
    private String brand;
    private String supplier;
    
    @NotNull(message = "Current stock is required")
    @Min(value = 0, message = "Current stock cannot be negative")
    private Integer currentStock;
    
    @Min(value = 0, message = "Minimum stock cannot be negative")
    private Integer minimumStock;
    
    @Min(value = 0, message = "Maximum stock cannot be negative")
    private Integer maximumStock;
    
    private String unit; // PIECE, KG, LITER, METER, etc.
    
    private BigDecimal unitPrice;
    private BigDecimal totalValue;
    
    private String status; // ACTIVE, DISCONTINUED, OUT_OF_STOCK
    private String location; // MAIN_STORE, KITCHEN, HOUSEKEEPING, etc.
    
    private LocalDateTime expiryDate;
    private LocalDateTime lastRestocked;
    private LocalDateTime nextOrderDate;
    
    private String notes;
    
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}