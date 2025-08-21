package com.restaurant.service.restaurant.repository;

import com.restaurant.common.entity.MenuItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MenuItemRepository extends JpaRepository<MenuItem, Long> {
    
    List<MenuItem> findByRestaurantIdAndAvailableTrue(Long restaurantId);
    
    List<MenuItem> findByRestaurantIdAndCategoryAndAvailableTrue(Long restaurantId, MenuItem.MenuCategory category);
    
    @Query("SELECT m FROM MenuItem m WHERE m.restaurantId = :restaurantId AND m.available = true AND " +
           "(:name IS NULL OR LOWER(m.name) LIKE LOWER(CONCAT('%', :name, '%')))")
    List<MenuItem> findAvailableMenuItemsByName(@Param("restaurantId") Long restaurantId, 
                                               @Param("name") String name);
    
    @Query("SELECT m FROM MenuItem m WHERE m.restaurantId = :restaurantId AND m.available = true AND " +
           "m.price BETWEEN :minPrice AND :maxPrice ORDER BY m.price")
    List<MenuItem> findMenuItemsByPriceRange(@Param("restaurantId") Long restaurantId,
                                           @Param("minPrice") Double minPrice,
                                           @Param("maxPrice") Double maxPrice);
    
    Optional<MenuItem> findByIdAndRestaurantIdAndAvailableTrue(Long id, Long restaurantId);
    
    @Query("SELECT DISTINCT m.category FROM MenuItem m WHERE m.restaurantId = :restaurantId AND m.available = true")
    List<MenuItem.MenuCategory> findDistinctCategoriesByRestaurantId(@Param("restaurantId") Long restaurantId);
}