package com.restaurant.common.event;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class PaymentProcessedEvent extends BaseEvent {
    private Long orderId;
    private Long customerId;
    private BigDecimal amount;
    private String paymentMethod;
    private String paymentStatus;
    private String transactionId;
    private String gatewayResponse;
    
    public PaymentProcessedEvent(Long orderId, Long customerId, BigDecimal amount,
                               String paymentMethod, String paymentStatus, 
                               String transactionId, String gatewayResponse) {
        super("PAYMENT_PROCESSED", "payment-service");
        this.orderId = orderId;
        this.customerId = customerId;
        this.amount = amount;
        this.paymentMethod = paymentMethod;
        this.paymentStatus = paymentStatus;
        this.transactionId = transactionId;
        this.gatewayResponse = gatewayResponse;
    }
}