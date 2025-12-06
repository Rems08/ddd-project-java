package com.xyzhotel.application.admin;

import com.xyzhotel.domain.booking.Booking;
import com.xyzhotel.domain.booking.BookingRepository;
import com.xyzhotel.domain.room.RoomException;
import com.xyzhotel.domain.shared.EntityId;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Cas d'usage : Afficher l'historique des réservations pour une chambre
 */
@Service
public class GetRoomBookingHistoryUseCase {
    
    private final BookingRepository bookingRepository;
    
    public GetRoomBookingHistoryUseCase(BookingRepository bookingRepository) {
        this.bookingRepository = bookingRepository;
    }
    
    @Transactional(readOnly = true)
    public List<BookingHistoryResult> execute(String roomId) throws RoomException {
        EntityId id = EntityId.of(roomId);
        List<Booking> bookings = bookingRepository.findByRoomId(id);
        
        return bookings.stream()
            .map(booking -> new BookingHistoryResult(
                booking.getId().value(),
                booking.getAccountId().value(),
                booking.getStayPeriod().checkInDate(),
                booking.getStayPeriod().numberOfNights(),
                booking.getTotalAmount().amount(),
                booking.getStatus().name(),
                booking.getCreatedAt()
            ))
            .toList();
    }
    
    public record BookingHistoryResult(
        String bookingId,
        String accountId,
        LocalDate checkInDate,
        int numberOfNights,
        BigDecimal totalAmount,
        String status,
        LocalDateTime createdAt
    ) {}
}
