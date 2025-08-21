package com.restaurant.service.order.client;

import com.restaurant.common.dto.ApiResponse;
import com.restaurant.common.entity.MenuItem;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient(name = "restaurant-service", url = "${feign.restaurant-service.url:http://localhost:8081}")
public interface RestaurantServiceClient {
    
    @GetMapping("/api/restaurants/{restaurantId}/menu")
    ApiResponse<List<MenuItem>> getMenuItems(@PathVariable Long restaurantId);
    
    @GetMapping("/api/restaurants/{restaurantId}/menu/{menuItemId}")
    ApiResponse<MenuItem> getMenuItem(@PathVariable Long restaurantId, 
                                    @PathVariable Long menuItemId);
    
    @GetMapping("/api/restaurants/{restaurantId}")
    ApiResponse<Object> getRestaurant(@PathVariable Long restaurantId);
}