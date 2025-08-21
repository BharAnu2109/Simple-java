package com.restaurant.service.order.repository;

import com.restaurant.common.entity.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {
    
    List<OrderItem> findByOrderId(Long orderId);
    
    @Query("SELECT oi FROM OrderItem oi JOIN oi.order o WHERE o.customerId = :customerId")
    List<OrderItem> findByCustomerId(@Param("customerId") Long customerId);
    
    @Query("SELECT oi FROM OrderItem oi JOIN oi.order o WHERE o.restaurantId = :restaurantId")
    List<OrderItem> findByRestaurantId(@Param("restaurantId") Long restaurantId);
    
    @Query("SELECT oi.menuItemId, SUM(oi.quantity) FROM OrderItem oi " +
           "JOIN oi.order o WHERE o.restaurantId = :restaurantId " +
           "GROUP BY oi.menuItemId ORDER BY SUM(oi.quantity) DESC")
    List<Object[]> findPopularItemsByRestaurant(@Param("restaurantId") Long restaurantId);
}