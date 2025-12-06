package com.xyzhotel.application.booking;

import com.xyzhotel.domain.account.AccountException;
import com.xyzhotel.domain.account.AccountRepository;
import com.xyzhotel.domain.booking.Booking;
import com.xyzhotel.domain.booking.BookingException;
import com.xyzhotel.domain.booking.BookingRepository;
import com.xyzhotel.domain.shared.EntityId;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Use case pour récupérer toutes les réservations d'un compte
 */
public class GetAccountBookingsUseCase {
    
    private final BookingRepository bookingRepository;
    private final AccountRepository accountRepository;
    
    public GetAccountBookingsUseCase(BookingRepository bookingRepository,
                                     AccountRepository accountRepository) {
        this.bookingRepository = bookingRepository;
        this.accountRepository = accountRepository;
    }
    
    /**
     * Récupère toutes les réservations d'un compte
     * @param accountId L'identifiant du compte
     * @return La liste des réservations du compte
     * @throws AccountException Si le compte n'existe pas
     */
    public List<BookingSummaryResult> execute(String accountId) throws AccountException {
        EntityId id = new EntityId(accountId);
        
        // Vérifier que le compte existe
        accountRepository.findById(id)
            .orElseThrow(() -> new AccountException("Compte non trouvé avec l'identifiant: " + accountId));
        
        List<Booking> bookings = bookingRepository.findByAccountId(id);
        
        return bookings.stream()
            .map(booking -> {
                try {
                    return new BookingSummaryResult(
                        booking.getId().value(),
                        booking.getStayPeriod().checkInDate(),
                        booking.getStayPeriod().numberOfNights(),
                        booking.getTotalAmount().amount(),
                        booking.getPaidAmount().amount(),
                        booking.getRemainingAmount().amount(),
                        booking.getStatus().toString(),
                        booking.getCreatedAt()
                    );
                } catch (BookingException e) {
                    throw new RuntimeException(e);
                }
            })
            .toList();
    }
    
    public record BookingSummaryResult(
        String bookingId,
        LocalDate checkInDate,
        int numberOfNights,
        BigDecimal totalAmount,
        BigDecimal paidAmount,
        BigDecimal remainingAmount,
        String status,
        LocalDateTime createdAt
    ) {}
}
