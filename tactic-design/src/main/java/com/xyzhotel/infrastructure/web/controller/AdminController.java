package com.xyzhotel.infrastructure.web.controller;

import com.xyzhotel.application.admin.GetRoomBookingHistoryUseCase;
import com.xyzhotel.application.admin.GetRoomStatisticsUseCase;
import com.xyzhotel.domain.room.RoomException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Contrôleur REST pour les fonctionnalités d'administration
 */
@RestController
@RequestMapping("/api/admin")
public class AdminController {
    
    private final GetRoomStatisticsUseCase getRoomStatisticsUseCase;
    private final GetRoomBookingHistoryUseCase getRoomBookingHistoryUseCase;
    
    public AdminController(GetRoomStatisticsUseCase getRoomStatisticsUseCase,
                          GetRoomBookingHistoryUseCase getRoomBookingHistoryUseCase) {
        this.getRoomStatisticsUseCase = getRoomStatisticsUseCase;
        this.getRoomBookingHistoryUseCase = getRoomBookingHistoryUseCase;
    }
    
    /**
     * Visualiser les statistiques des chambres
     * GET /api/admin/rooms/statistics
     */
    @GetMapping("/rooms/statistics")
    public ResponseEntity<RoomStatisticsResponse> getRoomStatistics() {
        GetRoomStatisticsUseCase.RoomStatisticsResult result = getRoomStatisticsUseCase.execute();
        
        RoomStatisticsResponse response = new RoomStatisticsResponse(
            result.totalRooms(),
            result.availableRooms(),
            result.occupiedRooms()
        );
        
        return ResponseEntity.ok(response);
    }
    
    /**
     * Afficher l'historique des réservations pour une chambre
     * GET /api/admin/rooms/{roomId}/history
     */
    @GetMapping("/rooms/{roomId}/history")
    public ResponseEntity<List<BookingHistoryResponse>> getRoomBookingHistory(@PathVariable String roomId) 
            throws RoomException {
        
        List<GetRoomBookingHistoryUseCase.BookingHistoryResult> results = 
            getRoomBookingHistoryUseCase.execute(roomId);
        
        List<BookingHistoryResponse> response = results.stream()
            .map(result -> new BookingHistoryResponse(
                result.bookingId(),
                result.accountId(),
                result.checkInDate(),
                result.numberOfNights(),
                result.totalAmount(),
                result.status(),
                result.createdAt()
            ))
            .toList();
        
        return ResponseEntity.ok(response);
    }
    
    public record RoomStatisticsResponse(
        long totalRooms,
        long availableRooms,
        long occupiedRooms
    ) {}
    
    public record BookingHistoryResponse(
        String bookingId,
        String accountId,
        LocalDate checkInDate,
        int numberOfNights,
        BigDecimal totalAmount,
        String status,
        LocalDateTime createdAt
    ) {}
}
