package com.hotel.management.room.entity;

import com.hotel.management.common.enums.RoomStatus;
import com.hotel.management.common.enums.RoomType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "rooms")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Room {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false, unique = true)
    private String roomNumber;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private RoomType roomType;
    
    @Column(nullable = false)
    private Integer floor;
    
    @Column(nullable = false)
    private Integer capacity;
    
    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal pricePerNight;
    
    @Column(length = 1000)
    private String description;
    
    @Column(length = 2000)
    private String amenities; // JSON string
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private RoomStatus status = RoomStatus.AVAILABLE;
    
    private boolean hasBalcony = false;
    private boolean hasSeaView = false;
    private boolean hasWifi = true;
    private boolean hasAirConditioning = true;
    private boolean hasMiniBar = false;
    
    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;
    
    @UpdateTimestamp
    @Column(nullable = false)
    private LocalDateTime updatedAt;
}