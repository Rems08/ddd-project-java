package com.xyzhotel.infrastructure.web.controller;

import com.xyzhotel.application.room.GetRoomInfoUseCase;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.math.BigDecimal;
import java.util.List;

/**
 * Contrôleur REST pour la gestion des chambres
 */
@RestController
@RequestMapping("/api/rooms")
public class RoomController {
    
    private final GetRoomInfoUseCase getRoomInfoUseCase;
    
    public RoomController(GetRoomInfoUseCase getRoomInfoUseCase) {
        this.getRoomInfoUseCase = getRoomInfoUseCase;
    }
    
    /**
     * Voir les informations des chambres
     * GET /api/rooms/info
     */
    @GetMapping("/info")
    public ResponseEntity<List<RoomInfoResponse>> getRoomInfo() {
        List<GetRoomInfoUseCase.RoomInfoResult> results = getRoomInfoUseCase.execute();
        
        List<RoomInfoResponse> response = results.stream()
            .map(result -> new RoomInfoResponse(
                result.type(),
                result.displayName(),
                result.pricePerNight(),
                result.amenities(),
                result.availableCount()
            ))
            .toList();
        
        return ResponseEntity.ok(response);
    }
    
    public record RoomInfoResponse(
        String type,
        String displayName,
        BigDecimal pricePerNight,
        String amenities,
        long availableCount
    ) {}
}
