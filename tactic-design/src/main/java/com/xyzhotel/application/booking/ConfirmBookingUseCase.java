package com.xyzhotel.application.booking;

import com.xyzhotel.domain.account.Account;
import com.xyzhotel.domain.account.AccountException;
import com.xyzhotel.domain.account.AccountRepository;
import com.xyzhotel.domain.booking.Booking;
import com.xyzhotel.domain.booking.BookingException;
import com.xyzhotel.domain.booking.BookingRepository;
import com.xyzhotel.domain.shared.EntityId;
import com.xyzhotel.domain.shared.Money;
import com.xyzhotel.domain.wallet.Wallet;
import com.xyzhotel.domain.wallet.WalletException;
import com.xyzhotel.domain.wallet.WalletRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;

/**
 * Cas d'usage : Confirmer une réservation
 * Le client paie les 50% restants
 */
@Service
public class ConfirmBookingUseCase {
    
    private final BookingRepository bookingRepository;
    private final AccountRepository accountRepository;
    private final WalletRepository walletRepository;
    
    public ConfirmBookingUseCase(BookingRepository bookingRepository,
                                AccountRepository accountRepository,
                                WalletRepository walletRepository) {
        this.bookingRepository = bookingRepository;
        this.accountRepository = accountRepository;
        this.walletRepository = walletRepository;
    }
    
    @Transactional
    public ConfirmBookingResult execute(String bookingId) 
            throws BookingException, AccountException, WalletException {
        
        // Trouver la réservation
        EntityId id = EntityId.of(bookingId);
        Booking booking = bookingRepository.findById(id)
            .orElseThrow(() -> new BookingException("Réservation non trouvée: " + bookingId));
        
        // Trouver le compte
        Account account = accountRepository.findById(booking.getAccountId())
            .orElseThrow(() -> new AccountException("Compte non trouvé"));
        
        // Calculer le montant restant
        Money remainingAmount = booking.confirm();
        
        // Débiter le portefeuille
        Wallet wallet = walletRepository.findByAccountId(booking.getAccountId())
            .orElseThrow(() -> new AccountException("Portefeuille non trouvé"));
        wallet.debit(remainingAmount);
        
        // Sauvegarder
        walletRepository.save(wallet);
        bookingRepository.save(booking);
        
        return new ConfirmBookingResult(
            booking.getId().value(),
            booking.getStatus().name(),
            booking.getTotalAmount().amount(),
            booking.getPaidAmount().amount()
        );
    }
    
    public record ConfirmBookingResult(
        String bookingId,
        String status,
        BigDecimal totalAmount,
        BigDecimal paidAmount
    ) {}
}
