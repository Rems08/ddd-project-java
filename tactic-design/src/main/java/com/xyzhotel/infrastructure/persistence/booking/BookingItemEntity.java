package com.xyzhotel.infrastructure.persistence.booking;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.util.Objects;

/**
 * Entity JPA pour la persistence des items de réservation
 */
@Entity
@Table(name = "booking_items")
public class BookingItemEntity {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "booking_id", nullable = false)
    private BookingEntity booking;
    
    @Column(name = "room_id", nullable = false, length = 50)
    private String roomId;
    
    @Column(name = "room_type", nullable = false)
    private String roomType;
    
    @Column(name = "price_per_night", nullable = false, precision = 19, scale = 2)
    private BigDecimal pricePerNight;
    
    public BookingItemEntity() {
    }
    
    public BookingItemEntity(String roomId, String roomType, BigDecimal pricePerNight) {
        this.roomId = roomId;
        this.roomType = roomType;
        this.pricePerNight = pricePerNight;
    }
    
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public BookingEntity getBooking() {
        return booking;
    }
    
    public void setBooking(BookingEntity booking) {
        this.booking = booking;
    }
    
    public String getRoomId() {
        return roomId;
    }
    
    public void setRoomId(String roomId) {
        this.roomId = roomId;
    }
    
    public String getRoomType() {
        return roomType;
    }
    
    public void setRoomType(String roomType) {
        this.roomType = roomType;
    }
    
    public BigDecimal getPricePerNight() {
        return pricePerNight;
    }
    
    public void setPricePerNight(BigDecimal pricePerNight) {
        this.pricePerNight = pricePerNight;
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BookingItemEntity that = (BookingItemEntity) o;
        return Objects.equals(id, that.id);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
