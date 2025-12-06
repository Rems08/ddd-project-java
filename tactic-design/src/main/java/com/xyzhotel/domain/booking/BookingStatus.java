package com.xyzhotel.domain.booking;

/**
 * Énumération des états possibles d'une réservation
 */
public enum BookingStatus {
    /**
     * Réservation créée, 50% payé
     */
    PENDING,
    
    /**
     * Réservation confirmée, 100% payé
     */
    CONFIRMED,
    
    /**
     * Réservation annulée (pas de remboursement)
     */
    CANCELLED
}
