package com.xyzhotel.application.booking;

import com.xyzhotel.domain.account.Account;
import com.xyzhotel.domain.account.AccountException;
import com.xyzhotel.domain.account.AccountRepository;
import com.xyzhotel.domain.booking.*;
import com.xyzhotel.domain.room.Room;
import com.xyzhotel.domain.room.RoomException;
import com.xyzhotel.domain.room.RoomRepository;
import com.xyzhotel.domain.room.RoomType;
import com.xyzhotel.domain.shared.EntityId;
import com.xyzhotel.domain.shared.Money;
import com.xyzhotel.domain.wallet.Wallet;
import com.xyzhotel.domain.wallet.WalletException;
import com.xyzhotel.domain.wallet.WalletRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Cas d'usage : Effectuer une réservation
 * Le client paie 50% à la réservation
 */
@Service
public class CreateBookingUseCase {
    
    private final AccountRepository accountRepository;
    private final RoomRepository roomRepository;
    private final BookingRepository bookingRepository;
    private final WalletRepository walletRepository;
    
    public CreateBookingUseCase(AccountRepository accountRepository, 
                               RoomRepository roomRepository,
                               BookingRepository bookingRepository,
                               WalletRepository walletRepository) {
        this.accountRepository = accountRepository;
        this.roomRepository = roomRepository;
        this.bookingRepository = bookingRepository;
        this.walletRepository = walletRepository;
    }
    
    @Transactional
    public CreateBookingResult execute(CreateBookingCommand command) 
            throws AccountException, RoomException, BookingException, WalletException {
        
        // Vérifier que le compte existe
        EntityId accountId = EntityId.of(command.accountId());
        Account account = accountRepository.findById(accountId)
            .orElseThrow(() -> new AccountException("Compte non trouvé: " + command.accountId()));
        
        // Créer la période de séjour
        StayPeriod stayPeriod = StayPeriod.of(command.checkInDate(), command.numberOfNights());
        
        // Préparer les items de réservation
        List<BookingItem> items = new ArrayList<>();
        
        for (Map.Entry<String, Integer> entry : command.rooms().entrySet()) {
            RoomType roomType = RoomType.valueOf(entry.getKey());
            int quantity = entry.getValue();
            
            // Trouver les chambres disponibles
            List<Room> availableRooms = roomRepository.findAvailableByType(roomType);
            
            if (availableRooms.size() < quantity) {
                throw new RoomException(
                    String.format("Pas assez de chambres disponibles du type %s. Demandé: %d, Disponible: %d",
                        roomType.getDisplayName(), quantity, availableRooms.size())
                );
            }
            
            // Sélectionner et marquer les chambres comme occupées
            for (int i = 0; i < quantity; i++) {
                Room room = availableRooms.get(i);
                room.markAsOccupied();
                roomRepository.save(room);
                
                items.add(new BookingItem(
                    room.getId(),
                    roomType,
                    roomType.getPricePerNight()
                ));
            }
        }
        
        // Créer la réservation
        Booking booking = Booking.create(accountId, stayPeriod, items);
        
        // Vérifier et débiter le portefeuille (50% du total)
        Money depositAmount = booking.getDepositAmount();
        Wallet wallet = walletRepository.findByAccountId(accountId)
            .orElse(new Wallet(accountId));
        
        if (!wallet.hasSufficientFunds(depositAmount)) {
            // Annuler les réservations de chambres
            for (BookingItem item : items) {
                Room room = roomRepository.findById(item.getRoomId())
                    .orElseThrow(() -> new RoomException("Chambre non trouvée"));
                room.markAsAvailable();
                roomRepository.save(room);
            }
            
            throw new WalletException(
                String.format("Solde insuffisant pour effectuer la réservation. Requis: %.2f €, Disponible: %.2f €",
                    depositAmount.amount().doubleValue(),
                    wallet.getBalance().amount().doubleValue())
            );
        }
        
        wallet.debit(depositAmount);
        walletRepository.save(wallet);
        
        // Sauvegarder la réservation
        booking = bookingRepository.save(booking);
        
        return new CreateBookingResult(
            booking.getId().value(),
            booking.getAccountId().value(),
            booking.getStayPeriod().checkInDate(),
            booking.getStayPeriod().numberOfNights(),
            booking.getTotalAmount().amount(),
            booking.getPaidAmount().amount(),
            booking.getStatus().name()
        );
    }
    
    public record CreateBookingCommand(
        String accountId,
        LocalDate checkInDate,
        int numberOfNights,
        Map<String, Integer> rooms  // RoomType -> quantity
    ) {}
    
    public record CreateBookingResult(
        String bookingId,
        String accountId,
        LocalDate checkInDate,
        int numberOfNights,
        BigDecimal totalAmount,
        BigDecimal paidAmount,
        String status
    ) {}
}
