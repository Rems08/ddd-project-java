package com.xyzhotel.infrastructure.config;

import com.xyzhotel.domain.room.Room;
import com.xyzhotel.domain.room.RoomRepository;
import com.xyzhotel.domain.room.RoomType;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configuration pour initialiser des données de test
 */
@Configuration
public class DataInitializer {
    
    @Bean
    public CommandLineRunner initializeRooms(RoomRepository roomRepository) {
        return args -> {
            // Vérifier si des chambres existent déjà
            if (roomRepository.findAll().isEmpty()) {
                System.out.println("Initialisation des chambres...");
                
                // Créer 10 chambres standard
                for (int i = 1; i <= 10; i++) {
                    Room room = Room.create("STD-" + String.format("%03d", i), RoomType.STANDARD);
                    roomRepository.save(room);
                }
                
                // Créer 5 chambres supérieures
                for (int i = 1; i <= 5; i++) {
                    Room room = Room.create("SUP-" + String.format("%03d", i), RoomType.SUPERIOR);
                    roomRepository.save(room);
                }
                
                // Créer 3 suites
                for (int i = 1; i <= 3; i++) {
                    Room room = Room.create("SUI-" + String.format("%03d", i), RoomType.SUITE);
                    roomRepository.save(room);
                }
                
                System.out.println("18 chambres créées avec succès!");
            } else {
                System.out.println("Les chambres sont déjà initialisées.");
            }
        };
    }
}
