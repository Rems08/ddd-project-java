package com.xyzhotel.application.admin;

import com.xyzhotel.domain.room.RoomRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Cas d'usage : Visualiser les statistiques des chambres
 */
@Service
public class GetRoomStatisticsUseCase {
    
    private final RoomRepository roomRepository;
    
    public GetRoomStatisticsUseCase(RoomRepository roomRepository) {
        this.roomRepository = roomRepository;
    }
    
    @Transactional(readOnly = true)
    public RoomStatisticsResult execute() {
        long availableRooms = roomRepository.countAvailable();
        long occupiedRooms = roomRepository.countOccupied();
        long totalRooms = availableRooms + occupiedRooms;
        
        return new RoomStatisticsResult(
            totalRooms,
            availableRooms,
            occupiedRooms
        );
    }
    
    public record RoomStatisticsResult(
        long totalRooms,
        long availableRooms,
        long occupiedRooms
    ) {}
}
