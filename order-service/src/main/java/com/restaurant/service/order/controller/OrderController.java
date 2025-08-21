package com.restaurant.service.order.controller;

import com.restaurant.common.dto.ApiResponse;
import com.restaurant.common.dto.CreateOrderRequest;
import com.restaurant.common.dto.OrderResponse;
import com.restaurant.common.entity.Order;
import com.restaurant.service.order.service.OrderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
@Slf4j
@CrossOrigin(origins = "*")
public class OrderController {
    
    private final OrderService orderService;
    
    @PostMapping
    public ResponseEntity<ApiResponse<OrderResponse>> createOrder(@Valid @RequestBody CreateOrderRequest request) {
        try {
            OrderResponse order = orderService.createOrder(request);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(ApiResponse.success(order, "Order created successfully"));
        } catch (Exception e) {
            log.error("Error creating order", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error("Failed to create order: " + e.getMessage()));
        }
    }
    
    @GetMapping("/{orderId}")
    public ResponseEntity<ApiResponse<OrderResponse>> getOrder(@PathVariable Long orderId) {
        try {
            return orderService.getOrderById(orderId)
                    .map(order -> ResponseEntity.ok(ApiResponse.success(order, "Order found")))
                    .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND)
                            .body(ApiResponse.error("Order not found")));
        } catch (Exception e) {
            log.error("Error retrieving order: {}", orderId, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error("Failed to retrieve order"));
        }
    }
    
    @GetMapping("/customer/{customerId}")
    public ResponseEntity<ApiResponse<List<OrderResponse>>> getCustomerOrders(@PathVariable Long customerId) {
        try {
            List<OrderResponse> orders = orderService.getOrdersByCustomer(customerId);
            return ResponseEntity.ok(ApiResponse.success(orders, "Customer orders retrieved successfully"));
        } catch (Exception e) {
            log.error("Error retrieving orders for customer: {}", customerId, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error("Failed to retrieve customer orders"));
        }
    }
    
    @GetMapping("/customer/{customerId}/order/{orderId}")
    public ResponseEntity<ApiResponse<OrderResponse>> getCustomerOrder(
            @PathVariable Long customerId, @PathVariable Long orderId) {
        try {
            return orderService.getOrderByIdAndCustomer(orderId, customerId)
                    .map(order -> ResponseEntity.ok(ApiResponse.success(order, "Order found")))
                    .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND)
                            .body(ApiResponse.error("Order not found")));
        } catch (Exception e) {
            log.error("Error retrieving order {} for customer {}", orderId, customerId, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error("Failed to retrieve order"));
        }
    }
    
    @GetMapping("/restaurant/{restaurantId}")
    public ResponseEntity<ApiResponse<List<OrderResponse>>> getRestaurantOrders(@PathVariable Long restaurantId) {
        try {
            List<OrderResponse> orders = orderService.getOrdersByRestaurant(restaurantId);
            return ResponseEntity.ok(ApiResponse.success(orders, "Restaurant orders retrieved successfully"));
        } catch (Exception e) {
            log.error("Error retrieving orders for restaurant: {}", restaurantId, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error("Failed to retrieve restaurant orders"));
        }
    }
    
    @PutMapping("/{orderId}/status")
    public ResponseEntity<ApiResponse<OrderResponse>> updateOrderStatus(
            @PathVariable Long orderId,
            @RequestParam Order.OrderStatus status,
            @RequestParam(required = false) String reason) {
        try {
            return orderService.updateOrderStatus(orderId, status, reason)
                    .map(order -> ResponseEntity.ok(ApiResponse.success(order, "Order status updated successfully")))
                    .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND)
                            .body(ApiResponse.error("Order not found")));
        } catch (Exception e) {
            log.error("Error updating order {} status to {}", orderId, status, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error("Failed to update order status"));
        }
    }
    
    @PostMapping("/customer/{customerId}/order/{orderId}/cancel")
    public ResponseEntity<ApiResponse<Void>> cancelOrder(
            @PathVariable Long customerId, @PathVariable Long orderId) {
        try {
            boolean cancelled = orderService.cancelOrder(orderId, customerId);
            if (cancelled) {
                return ResponseEntity.ok(ApiResponse.success(null, "Order cancelled successfully"));
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(ApiResponse.error("Order cannot be cancelled"));
            }
        } catch (Exception e) {
            log.error("Error cancelling order {} for customer {}", orderId, customerId, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error("Failed to cancel order"));
        }
    }
}