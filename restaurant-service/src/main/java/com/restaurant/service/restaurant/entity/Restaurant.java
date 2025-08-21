package com.restaurant.service.restaurant.entity;

import com.restaurant.common.entity.BaseEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.time.LocalTime;

@Entity
@Table(name = "restaurants")
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class Restaurant extends BaseEntity {
    
    @NotBlank
    @Column(name = "name", nullable = false)
    private String name;
    
    @Column(name = "description", length = 1000)
    private String description;
    
    @NotBlank
    @Column(name = "address", nullable = false, length = 500)
    private String address;
    
    @Pattern(regexp = "^\\+?[1-9]\\d{1,14}$")
    @Column(name = "phone_number")
    private String phoneNumber;
    
    @Column(name = "email")
    private String email;
    
    @Column(name = "website")
    private String website;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "cuisine_type")
    private CuisineType cuisineType;
    
    @Column(name = "opening_time")
    private LocalTime openingTime;
    
    @Column(name = "closing_time")
    private LocalTime closingTime;
    
    @Column(name = "is_active", nullable = false)
    private Boolean isActive = true;
    
    @Column(name = "delivery_enabled", nullable = false)
    private Boolean deliveryEnabled = true;
    
    @Column(name = "takeaway_enabled", nullable = false)
    private Boolean takeawayEnabled = true;
    
    @Column(name = "dine_in_enabled", nullable = false)
    private Boolean dineInEnabled = true;
    
    @Column(name = "delivery_radius_km")
    private Double deliveryRadiusKm;
    
    @Column(name = "minimum_order_amount")
    private Double minimumOrderAmount;
    
    @Column(name = "delivery_fee")
    private Double deliveryFee;
    
    @Column(name = "latitude")
    private Double latitude;
    
    @Column(name = "longitude")
    private Double longitude;
    
    @Column(name = "image_url")
    private String imageUrl;
    
    @Column(name = "rating")
    private Double rating = 0.0;
    
    @Column(name = "total_reviews")
    private Long totalReviews = 0L;
    
    public enum CuisineType {
        INDIAN, CHINESE, ITALIAN, MEXICAN, AMERICAN, THAI, JAPANESE, 
        MEDITERRANEAN, FRENCH, GERMAN, KOREAN, VIETNAMESE, OTHER
    }
}