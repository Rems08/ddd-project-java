package com.xyzhotel.infrastructure.cli.commands.booking;

import com.xyzhotel.application.booking.GetAccountBookingsUseCase;
import com.xyzhotel.infrastructure.cli.AbstractCommand;
import org.springframework.stereotype.Component;

import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * Commande CLI pour lister les réservations d'un compte
 */
@Component
public class ListAccountBookingsCommand extends AbstractCommand {
    
    private final GetAccountBookingsUseCase getAccountBookingsUseCase;
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    
    public ListAccountBookingsCommand(GetAccountBookingsUseCase getAccountBookingsUseCase) {
        this.getAccountBookingsUseCase = getAccountBookingsUseCase;
    }
    
    @Override
    public void execute(String[] args) throws Exception {
        validateArguments(args, 1);
        
        String accountId = args[0];
        List<GetAccountBookingsUseCase.BookingSummaryResult> results = getAccountBookingsUseCase.execute(accountId);
        
        if (results.isEmpty()) {
            printInfo("Aucune réservation trouvée pour ce compte.");
            return;
        }
        
        printSeparator();
        System.out.println("RÉSERVATIONS DU COMPTE " + accountId + " (" + results.size() + " réservation(s))");
        printSeparator();
        
        for (GetAccountBookingsUseCase.BookingSummaryResult result : results) {
            System.out.println("\nID: " + result.bookingId());
            System.out.println("  Check-in:       " + result.checkInDate().format(DATE_FORMATTER));
            System.out.println("  Nuits:          " + result.numberOfNights());
            System.out.println("  Total:          " + formatAmount(result.totalAmount()));
            System.out.println("  Payé:           " + formatAmount(result.paidAmount()));
            System.out.println("  Restant:        " + formatAmount(result.remainingAmount()));
            System.out.println("  Statut:         " + result.status());
        }
        printSeparator();
    }
    
    @Override
    public String getName() {
        return "booking:list";
    }
    
    @Override
    public String getDescription() {
        return "Lister les réservations d'un compte";
    }
    
    @Override
    public String getUsage() {
        return "booking:list <account_id>";
    }
}
