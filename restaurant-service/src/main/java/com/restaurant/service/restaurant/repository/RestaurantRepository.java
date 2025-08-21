package com.restaurant.service.restaurant.repository;

import com.restaurant.service.restaurant.entity.Restaurant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RestaurantRepository extends JpaRepository<Restaurant, Long> {
    
    List<Restaurant> findByIsActiveTrue();
    
    List<Restaurant> findByCuisineTypeAndIsActiveTrue(Restaurant.CuisineType cuisineType);
    
    @Query("SELECT r FROM Restaurant r WHERE r.isActive = true AND " +
           "(:name IS NULL OR LOWER(r.name) LIKE LOWER(CONCAT('%', :name, '%')))")
    List<Restaurant> findActiveRestaurantsByName(@Param("name") String name);
    
    @Query("SELECT r FROM Restaurant r WHERE r.isActive = true AND " +
           "r.deliveryEnabled = true AND " +
           "(6371 * acos(cos(radians(:latitude)) * cos(radians(r.latitude)) * " +
           "cos(radians(r.longitude) - radians(:longitude)) + " +
           "sin(radians(:latitude)) * sin(radians(r.latitude)))) <= r.deliveryRadiusKm")
    List<Restaurant> findRestaurantsWithinDeliveryRadius(@Param("latitude") Double latitude, 
                                                        @Param("longitude") Double longitude);
    
    @Query("SELECT r FROM Restaurant r WHERE r.isActive = true AND r.rating >= :minRating " +
           "ORDER BY r.rating DESC")
    List<Restaurant> findTopRatedRestaurants(@Param("minRating") Double minRating);
    
    Optional<Restaurant> findByIdAndIsActiveTrue(Long id);
}