package com.xyzhotel.domain.booking;

import com.xyzhotel.domain.room.RoomType;
import com.xyzhotel.domain.shared.EntityId;
import com.xyzhotel.domain.shared.Money;
import java.util.Objects;

/**
 * Value Object représentant un élément de réservation (une chambre réservée)
 */
public class BookingItem {
    
    private final EntityId roomId;
    private final RoomType roomType;
    private final Money pricePerNight;
    
    public BookingItem(EntityId roomId, RoomType roomType, Money pricePerNight) {
        this.roomId = Objects.requireNonNull(roomId, "L'identifiant de la chambre ne peut pas être null");
        this.roomType = Objects.requireNonNull(roomType, "Le type de chambre ne peut pas être null");
        this.pricePerNight = Objects.requireNonNull(pricePerNight, "Le prix par nuit ne peut pas être null");
    }
    
    /**
     * Calcule le prix total pour cette chambre pour un nombre de nuits donné
     * @param numberOfNights Le nombre de nuits
     * @return Le prix total
     */
    public Money calculateTotalPrice(int numberOfNights) {
        return pricePerNight.multiply(numberOfNights);
    }
    
    public EntityId getRoomId() {
        return roomId;
    }
    
    public RoomType getRoomType() {
        return roomType;
    }
    
    public Money getPricePerNight() {
        return pricePerNight;
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BookingItem that = (BookingItem) o;
        return Objects.equals(roomId, that.roomId);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(roomId);
    }
}
