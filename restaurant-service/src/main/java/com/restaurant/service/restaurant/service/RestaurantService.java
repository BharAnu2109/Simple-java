package com.restaurant.service.restaurant.service;

import com.restaurant.common.entity.MenuItem;
import com.restaurant.service.restaurant.entity.Restaurant;
import com.restaurant.service.restaurant.repository.MenuItemRepository;
import com.restaurant.service.restaurant.repository.RestaurantRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class RestaurantService {
    
    private final RestaurantRepository restaurantRepository;
    private final MenuItemRepository menuItemRepository;
    
    public List<Restaurant> getAllActiveRestaurants() {
        return restaurantRepository.findByIsActiveTrue();
    }
    
    public Optional<Restaurant> getRestaurantById(Long id) {
        return restaurantRepository.findByIdAndIsActiveTrue(id);
    }
    
    public List<Restaurant> searchRestaurantsByName(String name) {
        return restaurantRepository.findActiveRestaurantsByName(name);
    }
    
    public List<Restaurant> getRestaurantsByCuisine(Restaurant.CuisineType cuisineType) {
        return restaurantRepository.findByCuisineTypeAndIsActiveTrue(cuisineType);
    }
    
    public List<Restaurant> getRestaurantsWithinDeliveryRadius(Double latitude, Double longitude) {
        return restaurantRepository.findRestaurantsWithinDeliveryRadius(latitude, longitude);
    }
    
    public List<Restaurant> getTopRatedRestaurants(Double minRating) {
        return restaurantRepository.findTopRatedRestaurants(minRating);
    }
    
    public Restaurant createRestaurant(Restaurant restaurant) {
        log.info("Creating new restaurant: {}", restaurant.getName());
        return restaurantRepository.save(restaurant);
    }
    
    public Optional<Restaurant> updateRestaurant(Long id, Restaurant restaurantDetails) {
        return restaurantRepository.findById(id)
                .map(restaurant -> {
                    restaurant.setName(restaurantDetails.getName());
                    restaurant.setDescription(restaurantDetails.getDescription());
                    restaurant.setAddress(restaurantDetails.getAddress());
                    restaurant.setPhoneNumber(restaurantDetails.getPhoneNumber());
                    restaurant.setEmail(restaurantDetails.getEmail());
                    restaurant.setWebsite(restaurantDetails.getWebsite());
                    restaurant.setCuisineType(restaurantDetails.getCuisineType());
                    restaurant.setOpeningTime(restaurantDetails.getOpeningTime());
                    restaurant.setClosingTime(restaurantDetails.getClosingTime());
                    restaurant.setDeliveryEnabled(restaurantDetails.getDeliveryEnabled());
                    restaurant.setTakeawayEnabled(restaurantDetails.getTakeawayEnabled());
                    restaurant.setDineInEnabled(restaurantDetails.getDineInEnabled());
                    restaurant.setDeliveryRadiusKm(restaurantDetails.getDeliveryRadiusKm());
                    restaurant.setMinimumOrderAmount(restaurantDetails.getMinimumOrderAmount());
                    restaurant.setDeliveryFee(restaurantDetails.getDeliveryFee());
                    restaurant.setLatitude(restaurantDetails.getLatitude());
                    restaurant.setLongitude(restaurantDetails.getLongitude());
                    restaurant.setImageUrl(restaurantDetails.getImageUrl());
                    
                    log.info("Updated restaurant: {}", restaurant.getName());
                    return restaurantRepository.save(restaurant);
                });
    }
    
    public boolean deleteRestaurant(Long id) {
        return restaurantRepository.findById(id)
                .map(restaurant -> {
                    restaurant.setIsActive(false);
                    restaurantRepository.save(restaurant);
                    log.info("Deactivated restaurant: {}", restaurant.getName());
                    return true;
                })
                .orElse(false);
    }
    
    // Menu Item Methods
    public List<MenuItem> getMenuItemsByRestaurant(Long restaurantId) {
        return menuItemRepository.findByRestaurantIdAndAvailableTrue(restaurantId);
    }
    
    public List<MenuItem> getMenuItemsByCategory(Long restaurantId, MenuItem.MenuCategory category) {
        return menuItemRepository.findByRestaurantIdAndCategoryAndAvailableTrue(restaurantId, category);
    }
    
    public List<MenuItem> searchMenuItems(Long restaurantId, String name) {
        return menuItemRepository.findAvailableMenuItemsByName(restaurantId, name);
    }
    
    public Optional<MenuItem> getMenuItemById(Long id, Long restaurantId) {
        return menuItemRepository.findByIdAndRestaurantIdAndAvailableTrue(id, restaurantId);
    }
    
    public MenuItem createMenuItem(MenuItem menuItem) {
        log.info("Creating new menu item: {} for restaurant: {}", 
                menuItem.getName(), menuItem.getRestaurantId());
        return menuItemRepository.save(menuItem);
    }
    
    public Optional<MenuItem> updateMenuItem(Long id, MenuItem menuItemDetails) {
        return menuItemRepository.findById(id)
                .map(menuItem -> {
                    menuItem.setName(menuItemDetails.getName());
                    menuItem.setDescription(menuItemDetails.getDescription());
                    menuItem.setPrice(menuItemDetails.getPrice());
                    menuItem.setCategory(menuItemDetails.getCategory());
                    menuItem.setImageUrl(menuItemDetails.getImageUrl());
                    menuItem.setAvailable(menuItemDetails.getAvailable());
                    menuItem.setPreparationTimeMinutes(menuItemDetails.getPreparationTimeMinutes());
                    menuItem.setAllergenInfo(menuItemDetails.getAllergenInfo());
                    menuItem.setNutritionalInfo(menuItemDetails.getNutritionalInfo());
                    
                    log.info("Updated menu item: {}", menuItem.getName());
                    return menuItemRepository.save(menuItem);
                });
    }
    
    public boolean deleteMenuItem(Long id) {
        return menuItemRepository.findById(id)
                .map(menuItem -> {
                    menuItem.setAvailable(false);
                    menuItemRepository.save(menuItem);
                    log.info("Deactivated menu item: {}", menuItem.getName());
                    return true;
                })
                .orElse(false);
    }
    
    public List<MenuItem.MenuCategory> getDistinctCategories(Long restaurantId) {
        return menuItemRepository.findDistinctCategoriesByRestaurantId(restaurantId);
    }
}