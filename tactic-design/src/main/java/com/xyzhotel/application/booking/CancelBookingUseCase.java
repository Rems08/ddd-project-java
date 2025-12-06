package com.xyzhotel.application.booking;

import com.xyzhotel.domain.booking.Booking;
import com.xyzhotel.domain.booking.BookingException;
import com.xyzhotel.domain.booking.BookingItem;
import com.xyzhotel.domain.booking.BookingRepository;
import com.xyzhotel.domain.room.Room;
import com.xyzhotel.domain.room.RoomException;
import com.xyzhotel.domain.room.RoomRepository;
import com.xyzhotel.domain.shared.EntityId;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Cas d'usage : Annuler une réservation
 * Pas de remboursement
 */
@Service
public class CancelBookingUseCase {
    
    private final BookingRepository bookingRepository;
    private final RoomRepository roomRepository;
    
    public CancelBookingUseCase(BookingRepository bookingRepository, RoomRepository roomRepository) {
        this.bookingRepository = bookingRepository;
        this.roomRepository = roomRepository;
    }
    
    @Transactional
    public CancelBookingResult execute(String bookingId) throws BookingException, RoomException {
        // Trouver la réservation
        EntityId id = EntityId.of(bookingId);
        Booking booking = bookingRepository.findById(id)
            .orElseThrow(() -> new BookingException("Réservation non trouvée: " + bookingId));
        
        // Annuler la réservation
        booking.cancel();
        
        // Libérer les chambres
        for (BookingItem item : booking.getItems()) {
            Room room = roomRepository.findById(item.getRoomId())
                .orElseThrow(() -> new RoomException("Chambre non trouvée"));
            room.markAsAvailable();
            roomRepository.save(room);
        }
        
        // Sauvegarder
        bookingRepository.save(booking);
        
        return new CancelBookingResult(
            booking.getId().value(),
            booking.getStatus().name(),
            "La réservation a été annulée. Aucun remboursement n'a été effectué."
        );
    }
    
    public record CancelBookingResult(
        String bookingId,
        String status,
        String message
    ) {}
}
