package com.xyzhotel.infrastructure.cli.commands.booking;

import com.xyzhotel.application.booking.ConfirmBookingUseCase;
import com.xyzhotel.infrastructure.cli.AbstractCommand;
import org.springframework.stereotype.Component;

/**
 * Commande CLI pour confirmer une réservation
 */
@Component
public class ConfirmBookingCommand extends AbstractCommand {
    
    private final ConfirmBookingUseCase confirmBookingUseCase;
    
    public ConfirmBookingCommand(ConfirmBookingUseCase confirmBookingUseCase) {
        this.confirmBookingUseCase = confirmBookingUseCase;
    }
    
    @Override
    public void execute(String[] args) throws Exception {
        validateArguments(args, 1);
        
        String bookingId = args[0];
        ConfirmBookingUseCase.ConfirmBookingResult result = confirmBookingUseCase.execute(bookingId);
        
        printSuccess("Réservation confirmée!");
        printSeparator();
        System.out.println("ID Réservation: " + result.bookingId());
        System.out.println("Montant total:  " + formatAmount(result.totalAmount()));
        System.out.println("Montant payé:   " + formatAmount(result.paidAmount()));
        System.out.println("Statut:         " + result.status());
        printSeparator();
        printInfo("Votre réservation est maintenant confirmée. Bon séjour!");
    }
    
    @Override
    public String getName() {
        return "booking:confirm";
    }
    
    @Override
    public String getDescription() {
        return "Confirmer une réservation (paie les 50% restants)";
    }
    
    @Override
    public String getUsage() {
        return "booking:confirm <booking_id>";
    }
}
