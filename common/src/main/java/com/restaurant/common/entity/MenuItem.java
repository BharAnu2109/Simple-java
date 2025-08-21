package com.restaurant.common.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;

@Entity
@Table(name = "menu_items")
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class MenuItem extends BaseEntity {
    
    @NotBlank
    @Column(name = "name", nullable = false)
    private String name;
    
    @Column(name = "description", length = 1000)
    private String description;
    
    @NotNull
    @Positive
    @Column(name = "price", nullable = false, precision = 10, scale = 2)
    private BigDecimal price;
    
    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "category", nullable = false)
    private MenuCategory category;
    
    @Column(name = "image_url")
    private String imageUrl;
    
    @Column(name = "available", nullable = false)
    private Boolean available = true;
    
    @Column(name = "preparation_time_minutes")
    private Integer preparationTimeMinutes;
    
    @Column(name = "allergen_info")
    private String allergenInfo;
    
    @Column(name = "nutritional_info", columnDefinition = "TEXT")
    private String nutritionalInfo;
    
    @NotNull
    @Column(name = "restaurant_id", nullable = false)
    private Long restaurantId;
    
    public enum MenuCategory {
        APPETIZER,
        MAIN_COURSE,
        DESSERT,
        BEVERAGE,
        SALAD,
        SOUP,
        SIDE_DISH,
        SPECIAL
    }
}