package com.xyzhotel.infrastructure.persistence.booking;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

/**
 * Repository JPA pour les réservations
 */
@Repository
public interface JpaBookingRepository extends JpaRepository<BookingEntity, String> {
    
    List<BookingEntity> findByAccountId(String accountId);
    
    @Query("SELECT DISTINCT b FROM BookingEntity b JOIN b.items i WHERE i.roomId = :roomId")
    List<BookingEntity> findByRoomId(@Param("roomId") String roomId);
}
