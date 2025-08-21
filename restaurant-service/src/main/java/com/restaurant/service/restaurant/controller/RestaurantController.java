package com.restaurant.service.restaurant.controller;

import com.restaurant.common.dto.ApiResponse;
import com.restaurant.common.entity.MenuItem;
import com.restaurant.service.restaurant.entity.Restaurant;
import com.restaurant.service.restaurant.service.RestaurantService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/restaurants")
@RequiredArgsConstructor
@Slf4j
@CrossOrigin(origins = "*")
public class RestaurantController {
    
    private final RestaurantService restaurantService;
    
    @GetMapping
    public ResponseEntity<ApiResponse<List<Restaurant>>> getAllRestaurants() {
        try {
            List<Restaurant> restaurants = restaurantService.getAllActiveRestaurants();
            return ResponseEntity.ok(ApiResponse.success(restaurants, "Restaurants retrieved successfully"));
        } catch (Exception e) {
            log.error("Error retrieving restaurants", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error("Failed to retrieve restaurants"));
        }
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<Restaurant>> getRestaurantById(@PathVariable Long id) {
        try {
            return restaurantService.getRestaurantById(id)
                    .map(restaurant -> ResponseEntity.ok(ApiResponse.success(restaurant, "Restaurant found")))
                    .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND)
                            .body(ApiResponse.error("Restaurant not found")));
        } catch (Exception e) {
            log.error("Error retrieving restaurant with id: {}", id, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error("Failed to retrieve restaurant"));
        }
    }
    
    @GetMapping("/search")
    public ResponseEntity<ApiResponse<List<Restaurant>>> searchRestaurants(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) Restaurant.CuisineType cuisine,
            @RequestParam(required = false) Double latitude,
            @RequestParam(required = false) Double longitude,
            @RequestParam(required = false) Double minRating) {
        try {
            List<Restaurant> restaurants;
            
            if (latitude != null && longitude != null) {
                restaurants = restaurantService.getRestaurantsWithinDeliveryRadius(latitude, longitude);
            } else if (cuisine != null) {
                restaurants = restaurantService.getRestaurantsByCuisine(cuisine);
            } else if (name != null) {
                restaurants = restaurantService.searchRestaurantsByName(name);
            } else if (minRating != null) {
                restaurants = restaurantService.getTopRatedRestaurants(minRating);
            } else {
                restaurants = restaurantService.getAllActiveRestaurants();
            }
            
            return ResponseEntity.ok(ApiResponse.success(restaurants, "Search completed successfully"));
        } catch (Exception e) {
            log.error("Error searching restaurants", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error("Failed to search restaurants"));
        }
    }
    
    @PostMapping
    public ResponseEntity<ApiResponse<Restaurant>> createRestaurant(@Valid @RequestBody Restaurant restaurant) {
        try {
            Restaurant createdRestaurant = restaurantService.createRestaurant(restaurant);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(ApiResponse.success(createdRestaurant, "Restaurant created successfully"));
        } catch (Exception e) {
            log.error("Error creating restaurant", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error("Failed to create restaurant"));
        }
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<Restaurant>> updateRestaurant(
            @PathVariable Long id, @Valid @RequestBody Restaurant restaurant) {
        try {
            return restaurantService.updateRestaurant(id, restaurant)
                    .map(updatedRestaurant -> ResponseEntity.ok(
                            ApiResponse.success(updatedRestaurant, "Restaurant updated successfully")))
                    .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND)
                            .body(ApiResponse.error("Restaurant not found")));
        } catch (Exception e) {
            log.error("Error updating restaurant with id: {}", id, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error("Failed to update restaurant"));
        }
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteRestaurant(@PathVariable Long id) {
        try {
            boolean deleted = restaurantService.deleteRestaurant(id);
            if (deleted) {
                return ResponseEntity.ok(ApiResponse.success(null, "Restaurant deleted successfully"));
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(ApiResponse.error("Restaurant not found"));
            }
        } catch (Exception e) {
            log.error("Error deleting restaurant with id: {}", id, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error("Failed to delete restaurant"));
        }
    }
    
    // Menu Item endpoints
    @GetMapping("/{restaurantId}/menu")
    public ResponseEntity<ApiResponse<List<MenuItem>>> getMenuItems(
            @PathVariable Long restaurantId,
            @RequestParam(required = false) MenuItem.MenuCategory category,
            @RequestParam(required = false) String search) {
        try {
            List<MenuItem> menuItems;
            
            if (search != null) {
                menuItems = restaurantService.searchMenuItems(restaurantId, search);
            } else if (category != null) {
                menuItems = restaurantService.getMenuItemsByCategory(restaurantId, category);
            } else {
                menuItems = restaurantService.getMenuItemsByRestaurant(restaurantId);
            }
            
            return ResponseEntity.ok(ApiResponse.success(menuItems, "Menu items retrieved successfully"));
        } catch (Exception e) {
            log.error("Error retrieving menu items for restaurant: {}", restaurantId, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error("Failed to retrieve menu items"));
        }
    }
    
    @GetMapping("/{restaurantId}/menu/categories")
    public ResponseEntity<ApiResponse<List<MenuItem.MenuCategory>>> getMenuCategories(
            @PathVariable Long restaurantId) {
        try {
            List<MenuItem.MenuCategory> categories = restaurantService.getDistinctCategories(restaurantId);
            return ResponseEntity.ok(ApiResponse.success(categories, "Categories retrieved successfully"));
        } catch (Exception e) {
            log.error("Error retrieving menu categories for restaurant: {}", restaurantId, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error("Failed to retrieve menu categories"));
        }
    }
    
    @PostMapping("/{restaurantId}/menu")
    public ResponseEntity<ApiResponse<MenuItem>> createMenuItem(
            @PathVariable Long restaurantId, @Valid @RequestBody MenuItem menuItem) {
        try {
            menuItem.setRestaurantId(restaurantId);
            MenuItem createdMenuItem = restaurantService.createMenuItem(menuItem);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(ApiResponse.success(createdMenuItem, "Menu item created successfully"));
        } catch (Exception e) {
            log.error("Error creating menu item for restaurant: {}", restaurantId, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error("Failed to create menu item"));
        }
    }
    
    @PutMapping("/{restaurantId}/menu/{menuItemId}")
    public ResponseEntity<ApiResponse<MenuItem>> updateMenuItem(
            @PathVariable Long restaurantId,
            @PathVariable Long menuItemId,
            @Valid @RequestBody MenuItem menuItem) {
        try {
            return restaurantService.updateMenuItem(menuItemId, menuItem)
                    .map(updatedMenuItem -> ResponseEntity.ok(
                            ApiResponse.success(updatedMenuItem, "Menu item updated successfully")))
                    .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND)
                            .body(ApiResponse.error("Menu item not found")));
        } catch (Exception e) {
            log.error("Error updating menu item {} for restaurant: {}", menuItemId, restaurantId, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error("Failed to update menu item"));
        }
    }
    
    @DeleteMapping("/{restaurantId}/menu/{menuItemId}")
    public ResponseEntity<ApiResponse<Void>> deleteMenuItem(
            @PathVariable Long restaurantId, @PathVariable Long menuItemId) {
        try {
            boolean deleted = restaurantService.deleteMenuItem(menuItemId);
            if (deleted) {
                return ResponseEntity.ok(ApiResponse.success(null, "Menu item deleted successfully"));
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(ApiResponse.error("Menu item not found"));
            }
        } catch (Exception e) {
            log.error("Error deleting menu item {} for restaurant: {}", menuItemId, restaurantId, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error("Failed to delete menu item"));
        }
    }
}