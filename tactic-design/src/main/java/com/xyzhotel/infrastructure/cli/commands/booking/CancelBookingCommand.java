package com.xyzhotel.infrastructure.cli.commands.booking;

import com.xyzhotel.application.booking.CancelBookingUseCase;
import com.xyzhotel.infrastructure.cli.AbstractCommand;
import org.springframework.stereotype.Component;

/**
 * Commande CLI pour annuler une réservation
 */
@Component
public class CancelBookingCommand extends AbstractCommand {
    
    private final CancelBookingUseCase cancelBookingUseCase;
    
    public CancelBookingCommand(CancelBookingUseCase cancelBookingUseCase) {
        this.cancelBookingUseCase = cancelBookingUseCase;
    }
    
    @Override
    public void execute(String[] args) throws Exception {
        validateArguments(args, 1);
        
        String bookingId = args[0];
        CancelBookingUseCase.CancelBookingResult result = cancelBookingUseCase.execute(bookingId);
        
        printSuccess("Réservation annulée!");
        printSeparator();
        System.out.println("ID Réservation: " + result.bookingId());
        System.out.println("Statut:         " + result.status());
        System.out.println("Message:        " + result.message());
        printSeparator();
    }
    
    @Override
    public String getName() {
        return "booking:cancel";
    }
    
    @Override
    public String getDescription() {
        return "Annuler une réservation";
    }
    
    @Override
    public String getUsage() {
        return "booking:cancel <booking_id>";
    }
}
