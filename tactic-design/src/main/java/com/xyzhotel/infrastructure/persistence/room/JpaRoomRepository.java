package com.xyzhotel.infrastructure.persistence.room;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;

/**
 * Repository JPA pour les chambres
 */
@Repository
public interface JpaRoomRepository extends JpaRepository<RoomEntity, String> {
    
    List<RoomEntity> findByRoomTypeAndIsAvailable(String roomType, Boolean isAvailable);
    
    @Query("SELECT COUNT(r) FROM RoomEntity r WHERE r.isAvailable = true")
    long countAvailable();
    
    @Query("SELECT COUNT(r) FROM RoomEntity r WHERE r.isAvailable = false")
    long countOccupied();
}
