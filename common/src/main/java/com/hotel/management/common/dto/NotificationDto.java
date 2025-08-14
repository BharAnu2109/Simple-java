package com.hotel.management.common.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class NotificationDto {
    
    private Long id;
    
    @NotNull(message = "Recipient ID is required")
    private Long recipientId;
    
    @NotBlank(message = "Type is required")
    private String type; // EMAIL, SMS, PUSH, IN_APP
    
    @NotBlank(message = "Subject is required")
    private String subject;
    
    @NotBlank(message = "Content is required")
    private String content;
    
    private String channel; // BOOKING, PAYMENT, MAINTENANCE, MARKETING
    private String priority; // LOW, MEDIUM, HIGH, URGENT
    private String status; // PENDING, SENT, DELIVERED, FAILED, READ
    
    private String recipientEmail;
    private String recipientPhone;
    
    private String templateId;
    private String templateData; // JSON string
    
    private LocalDateTime scheduledTime;
    private LocalDateTime sentTime;
    private LocalDateTime deliveredTime;
    private LocalDateTime readTime;
    
    private String failureReason;
    private Integer retryCount;
    
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}