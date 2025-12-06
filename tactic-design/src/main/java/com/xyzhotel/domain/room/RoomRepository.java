package com.xyzhotel.domain.room;

import com.xyzhotel.domain.shared.EntityId;
import java.util.List;
import java.util.Optional;

/**
 * Port pour la persistence des chambres
 */
public interface RoomRepository {
    
    /**
     * Sauvegarde une chambre
     * @param room La chambre à sauvegarder
     * @return La chambre sauvegardée
     */
    Room save(Room room);
    
    /**
     * Trouve une chambre par son identifiant
     * @param id L'identifiant de la chambre
     * @return La chambre si elle existe
     */
    Optional<Room> findById(EntityId id);
    
    /**
     * Trouve toutes les chambres
     * @return La liste de toutes les chambres
     */
    List<Room> findAll();
    
    /**
     * Trouve les chambres disponibles d'un type donné
     * @param type Le type de chambre recherché
     * @return La liste des chambres disponibles
     */
    List<Room> findAvailableByType(RoomType type);
    
    /**
     * Compte le nombre de chambres disponibles
     * @return Le nombre de chambres disponibles
     */
    long countAvailable();
    
    /**
     * Compte le nombre de chambres occupées
     * @return Le nombre de chambres occupées
     */
    long countOccupied();
}
