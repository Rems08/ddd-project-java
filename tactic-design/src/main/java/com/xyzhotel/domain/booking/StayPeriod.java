package com.xyzhotel.domain.booking;

import com.xyzhotel.domain.shared.ValueObject;
import java.time.LocalDate;
import java.util.Objects;

/**
 * Value Object représentant une période de séjour
 */
public record StayPeriod(LocalDate checkInDate, int numberOfNights) implements ValueObject {
    
    public StayPeriod {
        Objects.requireNonNull(checkInDate, "La date de check-in ne peut pas être null");
        if (numberOfNights <= 0) {
            throw new IllegalArgumentException("Le nombre de nuits doit être positif");
        }
    }
    
    public static StayPeriod of(LocalDate checkInDate, int numberOfNights) {
        return new StayPeriod(checkInDate, numberOfNights);
    }
    
    /**
     * Calcule la date de check-out
     * @return La date de départ
     */
    public LocalDate getCheckOutDate() {
        return checkInDate.plusDays(numberOfNights);
    }
    
    /**
     * Vérifie si cette période chevauche une autre période
     * @param other L'autre période
     * @return true si les périodes se chevauchent
     */
    public boolean overlaps(StayPeriod other) {
        return !this.checkInDate.isAfter(other.getCheckOutDate().minusDays(1)) &&
               !this.getCheckOutDate().minusDays(1).isBefore(other.checkInDate);
    }
}
