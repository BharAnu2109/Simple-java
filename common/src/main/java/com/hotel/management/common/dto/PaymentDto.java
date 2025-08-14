package com.hotel.management.common.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PaymentDto {
    
    private Long id;
    
    @NotNull(message = "Booking ID is required")
    private Long bookingId;
    
    @NotNull(message = "Amount is required")
    @DecimalMin(value = "0.01", message = "Amount must be greater than 0")
    private BigDecimal amount;
    
    private String paymentMethod; // CREDIT_CARD, DEBIT_CARD, CASH, BANK_TRANSFER, DIGITAL_WALLET
    private String paymentStatus; // PENDING, COMPLETED, FAILED, REFUNDED
    private String transactionId;
    private String paymentGateway;
    
    private String cardLast4;
    private String cardType;
    
    private String description;
    private String failureReason;
    
    private LocalDateTime paymentDate;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    
    // Additional fields for response
    private BookingDto booking;
}