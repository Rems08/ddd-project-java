package com.xyzhotel.application.room;

import com.xyzhotel.domain.room.RoomRepository;
import com.xyzhotel.domain.room.RoomType;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

/**
 * Cas d'usage : Voir les informations des chambres disponibles
 */
@Service
public class GetRoomInfoUseCase {
    
    private final RoomRepository roomRepository;
    
    public GetRoomInfoUseCase(RoomRepository roomRepository) {
        this.roomRepository = roomRepository;
    }
    
    @Transactional(readOnly = true)
    public List<RoomInfoResult> execute() {
        return Arrays.stream(RoomType.values())
            .map(type -> {
                long availableCount = roomRepository.findAvailableByType(type).size();
                return new RoomInfoResult(
                    type.name(),
                    type.getDisplayName(),
                    type.getPricePerNight().amount(),
                    type.getAmenities(),
                    availableCount
                );
            })
            .toList();
    }
    
    public record RoomInfoResult(
        String type,
        String displayName,
        BigDecimal pricePerNight,
        String amenities,
        long availableCount
    ) {}
}
