package com.xyzhotel.infrastructure.cli.commands.booking;

import com.xyzhotel.application.booking.CreateBookingUseCase;
import com.xyzhotel.infrastructure.cli.AbstractCommand;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

/**
 * Commande CLI pour créer une réservation
 */
@Component
public class CreateBookingCommand extends AbstractCommand {
    
    private final CreateBookingUseCase createBookingUseCase;
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    
    public CreateBookingCommand(CreateBookingUseCase createBookingUseCase) {
        this.createBookingUseCase = createBookingUseCase;
    }
    
    @Override
    public void execute(String[] args) throws Exception {
        validateArguments(args, 4);
        
        String accountId = args[0];
        LocalDate checkInDate = LocalDate.parse(args[1], DateTimeFormatter.ISO_LOCAL_DATE);
        int numberOfNights = Integer.parseInt(args[2]);
        
        // Parse room types: STANDARD:2,SUPERIOR:1
        Map<String, Integer> rooms = parseRooms(args[3]);
        
        CreateBookingUseCase.CreateBookingCommand command = 
            new CreateBookingUseCase.CreateBookingCommand(accountId, checkInDate, numberOfNights, rooms);
        
        CreateBookingUseCase.CreateBookingResult result = createBookingUseCase.execute(command);
        
        printSuccess("Réservation créée avec succès!");
        printSeparator();
        System.out.println("ID Réservation:     " + result.bookingId());
        System.out.println("Check-in:           " + result.checkInDate().format(DATE_FORMATTER));
        System.out.println("Nombre de nuits:    " + result.numberOfNights());
        System.out.println("Montant total:      " + formatAmount(result.totalAmount()));
        System.out.println("Acompte payé (50%): " + formatAmount(result.paidAmount()));
        System.out.println("Statut:             " + result.status());
        printSeparator();
        printInfo("Utilisez 'booking:confirm " + result.bookingId() + "' pour confirmer et payer le reste.");
    }
    
    private Map<String, Integer> parseRooms(String roomsStr) {
        Map<String, Integer> rooms = new HashMap<>();
        String[] pairs = roomsStr.split(",");
        for (String pair : pairs) {
            String[] parts = pair.split(":");
            if (parts.length != 2) {
                throw new IllegalArgumentException("Format invalide pour les chambres: " + pair);
            }
            rooms.put(parts[0].toUpperCase(), Integer.parseInt(parts[1]));
        }
        return rooms;
    }
    
    @Override
    public String getName() {
        return "booking:create";
    }
    
    @Override
    public String getDescription() {
        return "Créer une réservation (paie 50% d'acompte)";
    }
    
    @Override
    public String getUsage() {
        return "booking:create <account_id> <date_checkin> <nombre_nuits> <chambres>\n" +
               "       Date format: YYYY-MM-DD\n" +
               "       Chambres format: TYPE:QUANTITE,TYPE:QUANTITE\n" +
               "       Types disponibles: STANDARD, SUPERIOR, SUITE\n" +
               "       Exemple: booking:create acc123 2025-12-15 3 STANDARD:1,SUPERIOR:1";
    }
}
