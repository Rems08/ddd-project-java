package com.xyzhotel.infrastructure.persistence.room;

import jakarta.persistence.*;
import java.util.Objects;

/**
 * Entity JPA pour la persistence des chambres
 */
@Entity
@Table(name = "rooms")
public class RoomEntity {
    
    @Id
    @Column(name = "id", nullable = false, length = 50)
    private String id;
    
    @Column(name = "room_number", nullable = false, unique = true)
    private String roomNumber;
    
    @Column(name = "room_type", nullable = false, length = 20)
    private String roomType;
    
    @Column(name = "is_available", nullable = false)
    private Boolean isAvailable;
    
    public RoomEntity() {
    }
    
    public RoomEntity(String id, String roomNumber, String roomType, Boolean isAvailable) {
        this.id = id;
        this.roomNumber = roomNumber;
        this.roomType = roomType;
        this.isAvailable = isAvailable;
    }
    
    public String getId() {
        return id;
    }
    
    public void setId(String id) {
        this.id = id;
    }
    
    public String getRoomNumber() {
        return roomNumber;
    }
    
    public void setRoomNumber(String roomNumber) {
        this.roomNumber = roomNumber;
    }
    
    public String getRoomType() {
        return roomType;
    }
    
    public void setRoomType(String roomType) {
        this.roomType = roomType;
    }
    
    public Boolean getIsAvailable() {
        return isAvailable;
    }
    
    public void setIsAvailable(Boolean available) {
        isAvailable = available;
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RoomEntity that = (RoomEntity) o;
        return Objects.equals(id, that.id);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
