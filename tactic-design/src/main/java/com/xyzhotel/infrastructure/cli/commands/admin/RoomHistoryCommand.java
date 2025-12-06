package com.xyzhotel.infrastructure.cli.commands.admin;

import com.xyzhotel.application.admin.GetRoomBookingHistoryUseCase;
import com.xyzhotel.infrastructure.cli.AbstractCommand;
import org.springframework.stereotype.Component;

import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * Commande CLI pour afficher l'historique des réservations d'une chambre
 */
@Component
public class RoomHistoryCommand extends AbstractCommand {
    
    private final GetRoomBookingHistoryUseCase getRoomBookingHistoryUseCase;
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    
    public RoomHistoryCommand(GetRoomBookingHistoryUseCase getRoomBookingHistoryUseCase) {
        this.getRoomBookingHistoryUseCase = getRoomBookingHistoryUseCase;
    }
    
    @Override
    public void execute(String[] args) throws Exception {
        validateArguments(args, 1);
        
        String roomId = args[0];
        List<GetRoomBookingHistoryUseCase.BookingHistoryResult> results = getRoomBookingHistoryUseCase.execute(roomId);
        
        if (results.isEmpty()) {
            printInfo("Aucun historique de réservation trouvé pour cette chambre.");
            return;
        }
        
        printSeparator();
        System.out.println("HISTORIQUE DE LA CHAMBRE " + roomId + " (" + results.size() + " réservation(s))");
        printSeparator();
        
        for (GetRoomBookingHistoryUseCase.BookingHistoryResult result : results) {
            System.out.println("\nRéservation ID: " + result.bookingId());
            System.out.println("  Compte:         " + result.accountId());
            System.out.println("  Check-in:       " + result.checkInDate().format(DATE_FORMATTER));
            System.out.println("  Nuits:          " + result.numberOfNights());
            System.out.println("  Montant:        " + formatAmount(result.totalAmount()));
            System.out.println("  Statut:         " + result.status());
        }
        printSeparator();
    }
    
    @Override
    public String getName() {
        return "admin:history";
    }
    
    @Override
    public String getDescription() {
        return "Afficher l'historique des réservations d'une chambre (admin)";
    }
    
    @Override
    public String getUsage() {
        return "admin:history <room_id>";
    }
}
