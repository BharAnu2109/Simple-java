package com.hotel.management.common.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RoomDto {
    
    private Long id;
    
    @NotBlank(message = "Room number is required")
    private String roomNumber;
    
    @NotBlank(message = "Room type is required")
    private String roomType; // SINGLE, DOUBLE, SUITE, DELUXE, PRESIDENTIAL
    
    @Min(value = 1, message = "Floor must be at least 1")
    private Integer floor;
    
    @Min(value = 1, message = "Capacity must be at least 1")
    private Integer capacity;
    
    @NotNull(message = "Price per night is required")
    @DecimalMin(value = "0.01", message = "Price must be greater than 0")
    private BigDecimal pricePerNight;
    
    private String description;
    private String amenities; // JSON string of amenities
    private String status; // AVAILABLE, OCCUPIED, MAINTENANCE, OUT_OF_ORDER
    
    private boolean hasBalcony;
    private boolean hasSeaView;
    private boolean hasWifi;
    private boolean hasAirConditioning;
    private boolean hasMiniBar;
    
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}