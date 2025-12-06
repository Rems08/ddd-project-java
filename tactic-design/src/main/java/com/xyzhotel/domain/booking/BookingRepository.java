package com.xyzhotel.domain.booking;

import com.xyzhotel.domain.shared.EntityId;
import java.util.List;
import java.util.Optional;

/**
 * Port pour la persistence des réservations
 */
public interface BookingRepository {
    
    /**
     * Sauvegarde une réservation
     * @param booking La réservation à sauvegarder
     * @return La réservation sauvegardée
     */
    Booking save(Booking booking);
    
    /**
     * Trouve une réservation par son identifiant
     * @param id L'identifiant de la réservation
     * @return La réservation si elle existe
     */
    Optional<Booking> findById(EntityId id);
    
    /**
     * Trouve toutes les réservations d'un client
     * @param accountId L'identifiant du compte client
     * @return La liste des réservations du client
     */
    List<Booking> findByAccountId(EntityId accountId);
    
    /**
     * Trouve toutes les réservations pour une chambre donnée
     * @param roomId L'identifiant de la chambre
     * @return La liste des réservations pour cette chambre
     */
    List<Booking> findByRoomId(EntityId roomId);
    
    /**
     * Trouve toutes les réservations
     * @return La liste de toutes les réservations
     */
    List<Booking> findAll();
}
