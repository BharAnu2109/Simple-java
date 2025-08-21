package com.restaurant.service.order.repository;

import com.restaurant.common.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    
    List<Order> findByCustomerId(Long customerId);
    
    List<Order> findByRestaurantId(Long restaurantId);
    
    List<Order> findByStatus(Order.OrderStatus status);
    
    List<Order> findByCustomerIdAndStatus(Long customerId, Order.OrderStatus status);
    
    List<Order> findByRestaurantIdAndStatus(Long restaurantId, Order.OrderStatus status);
    
    @Query("SELECT o FROM Order o WHERE o.customerId = :customerId " +
           "ORDER BY o.createdAt DESC")
    List<Order> findRecentOrdersByCustomer(@Param("customerId") Long customerId);
    
    @Query("SELECT o FROM Order o WHERE o.restaurantId = :restaurantId " +
           "AND o.createdAt >= :startDate AND o.createdAt <= :endDate " +
           "ORDER BY o.createdAt DESC")
    List<Order> findOrdersByRestaurantAndDateRange(@Param("restaurantId") Long restaurantId,
                                                  @Param("startDate") LocalDateTime startDate,
                                                  @Param("endDate") LocalDateTime endDate);
    
    @Query("SELECT COUNT(o) FROM Order o WHERE o.restaurantId = :restaurantId " +
           "AND o.status IN :statuses")
    Long countOrdersByRestaurantAndStatuses(@Param("restaurantId") Long restaurantId,
                                          @Param("statuses") List<Order.OrderStatus> statuses);
    
    @Query("SELECT o FROM Order o WHERE o.orderType = :orderType " +
           "AND o.status = :status ORDER BY o.createdAt ASC")
    List<Order> findOrdersByTypeAndStatus(@Param("orderType") Order.OrderType orderType,
                                        @Param("status") Order.OrderStatus status);
    
    Optional<Order> findByIdAndCustomerId(Long id, Long customerId);
}