package com.xyzhotel.infrastructure.cli.commands.booking;

import com.xyzhotel.application.booking.GetBookingUseCase;
import com.xyzhotel.infrastructure.cli.AbstractCommand;
import org.springframework.stereotype.Component;

import java.time.format.DateTimeFormatter;

/**
 * Commande CLI pour afficher les détails d'une réservation
 */
@Component
public class GetBookingCommand extends AbstractCommand {
    
    private final GetBookingUseCase getBookingUseCase;
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    private static final DateTimeFormatter DATETIME_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
    
    public GetBookingCommand(GetBookingUseCase getBookingUseCase) {
        this.getBookingUseCase = getBookingUseCase;
    }
    
    @Override
    public void execute(String[] args) throws Exception {
        validateArguments(args, 1);
        
        String bookingId = args[0];
        GetBookingUseCase.BookingDetailsResult result = getBookingUseCase.execute(bookingId);
        
        printSeparator();
        System.out.println("DÉTAILS DE LA RÉSERVATION");
        printSeparator();
        System.out.println("ID Réservation:     " + result.bookingId());
        System.out.println("Compte:             " + result.accountId());
        System.out.println("Check-in:           " + result.checkInDate().format(DATE_FORMATTER));
        System.out.println("Nombre de nuits:    " + result.numberOfNights());
        System.out.println("Montant total:      " + formatAmount(result.totalAmount()));
        System.out.println("Montant payé:       " + formatAmount(result.paidAmount()));
        System.out.println("Montant restant:    " + formatAmount(result.remainingAmount()));
        System.out.println("Statut:             " + result.status());
        System.out.println("Créée le:           " + result.createdAt().format(DATETIME_FORMATTER));
        
        System.out.println("\nChambres réservées:");
        for (GetBookingUseCase.RoomItemResult room : result.rooms()) {
            System.out.printf("  - %s (ID: %s): %s/nuit x %d nuits%n",
                room.roomType(),
                room.roomId(),
                formatAmount(room.pricePerNight()),
                room.numberOfNights()
            );
        }
        printSeparator();
    }
    
    @Override
    public String getName() {
        return "booking:get";
    }
    
    @Override
    public String getDescription() {
        return "Afficher les détails d'une réservation";
    }
    
    @Override
    public String getUsage() {
        return "booking:get <booking_id>";
    }
}
