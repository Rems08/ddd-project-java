package com.xyzhotel.infrastructure.cli.commands.room;

import com.xyzhotel.application.room.GetRoomInfoUseCase;
import com.xyzhotel.infrastructure.cli.AbstractCommand;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Commande CLI pour lister les chambres disponibles
 */
@Component
public class ListRoomsCommand extends AbstractCommand {
    
    private final GetRoomInfoUseCase getRoomInfoUseCase;
    
    public ListRoomsCommand(GetRoomInfoUseCase getRoomInfoUseCase) {
        this.getRoomInfoUseCase = getRoomInfoUseCase;
    }
    
    @Override
    public void execute(String[] args) throws Exception {
        List<GetRoomInfoUseCase.RoomInfoResult> results = getRoomInfoUseCase.execute();
        
        printSeparator();
        System.out.println("TYPES DE CHAMBRES DISPONIBLES");
        printSeparator();
        
        for (GetRoomInfoUseCase.RoomInfoResult result : results) {
            System.out.println("\n" + result.displayName() + " (" + result.type() + ")");
            System.out.println("  Prix par nuit:    " + formatAmount(result.pricePerNight()));
            System.out.println("  Chambres dispos:  " + result.availableCount());
            System.out.println("  Équipements:      " + result.amenities());
        }
        printSeparator();
    }
    
    @Override
    public String getName() {
        return "room:list";
    }
    
    @Override
    public String getDescription() {
        return "Lister les types de chambres et disponibilités";
    }
    
    @Override
    public String getUsage() {
        return "room:list";
    }
}
