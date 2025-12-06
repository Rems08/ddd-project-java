package com.xyzhotel.infrastructure.cli.commands.admin;

import com.xyzhotel.application.admin.GetRoomStatisticsUseCase;
import com.xyzhotel.infrastructure.cli.AbstractCommand;
import org.springframework.stereotype.Component;

/**
 * Commande CLI pour afficher les statistiques des chambres
 */
@Component
public class RoomStatisticsCommand extends AbstractCommand {
    
    private final GetRoomStatisticsUseCase getRoomStatisticsUseCase;
    
    public RoomStatisticsCommand(GetRoomStatisticsUseCase getRoomStatisticsUseCase) {
        this.getRoomStatisticsUseCase = getRoomStatisticsUseCase;
    }
    
    @Override
    public void execute(String[] args) throws Exception {
        GetRoomStatisticsUseCase.RoomStatisticsResult result = getRoomStatisticsUseCase.execute();
        
        printSeparator();
        System.out.println("STATISTIQUES DES CHAMBRES");
        printSeparator();
        System.out.println("Chambres totales:      " + result.totalRooms());
        System.out.println("Chambres disponibles:  " + result.availableRooms());
        System.out.println("Chambres occupées:     " + result.occupiedRooms());
        
        double occupancyRate = result.totalRooms() > 0 
            ? (result.occupiedRooms() * 100.0 / result.totalRooms()) 
            : 0.0;
        System.out.printf("Taux d'occupation:     %.1f%%%n", occupancyRate);
        

        printSeparator();
    }
    
    @Override
    public String getName() {
        return "admin:stats";
    }
    
    @Override
    public String getDescription() {
        return "Afficher les statistiques des chambres (admin)";
    }
    
    @Override
    public String getUsage() {
        return "admin:stats";
    }
}
