package com.xyzhotel.application.booking;

import com.xyzhotel.domain.booking.Booking;
import com.xyzhotel.domain.booking.BookingException;
import com.xyzhotel.domain.booking.BookingRepository;
import com.xyzhotel.domain.shared.EntityId;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Use case pour récupérer les détails d'une réservation
 */
public class GetBookingUseCase {
    
    private final BookingRepository bookingRepository;
    
    public GetBookingUseCase(BookingRepository bookingRepository) {
        this.bookingRepository = bookingRepository;
    }
    
    /**
     * Récupère les détails d'une réservation par son identifiant
     * @param bookingId L'identifiant de la réservation
     * @return Les détails de la réservation
     * @throws BookingException Si la réservation n'existe pas
     */
    public BookingDetailsResult execute(String bookingId) throws BookingException {
        EntityId id = new EntityId(bookingId);
        
        Booking booking = bookingRepository.findById(id)
            .orElseThrow(() -> new BookingException("Réservation non trouvée avec l'identifiant: " + bookingId));
        
        List<RoomItemResult> rooms = booking.getItems().stream()
            .map(item -> new RoomItemResult(
                item.getRoomId().value(),
                item.getRoomType().toString(),
                item.getPricePerNight().amount(),
                booking.getStayPeriod().numberOfNights()
            ))
            .toList();
        
        return new BookingDetailsResult(
            booking.getId().value(),
            booking.getAccountId().value(),
            booking.getStayPeriod().checkInDate(),
            booking.getStayPeriod().numberOfNights(),
            booking.getTotalAmount().amount(),
            booking.getPaidAmount().amount(),
            booking.getRemainingAmount().amount(),
            booking.getStatus().toString(),
            booking.getCreatedAt(),
            rooms
        );
    }
    
    public record BookingDetailsResult(
        String bookingId,
        String accountId,
        LocalDate checkInDate,
        int numberOfNights,
        BigDecimal totalAmount,
        BigDecimal paidAmount,
        BigDecimal remainingAmount,
        String status,
        LocalDateTime createdAt,
        List<RoomItemResult> rooms
    ) {}
    
    public record RoomItemResult(
        String roomId,
        String roomType,
        BigDecimal pricePerNight,
        int numberOfNights
    ) {}
}
