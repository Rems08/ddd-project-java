package com.xyzhotel.domain.room;

import com.xyzhotel.domain.shared.Money;

/**
 * Énumération des types de chambres avec leurs caractéristiques et tarifs
 */
public enum RoomType {
    STANDARD(
        "Chambre Standard",
        Money.of(50.0),
        "Lit 1 place, Wifi, TV"
    ),
    SUPERIOR(
        "Chambre Supérieure",
        Money.of(100.0),
        "Lit 2 places, Wifi, TV écran plat, Minibar, Climatiseur"
    ),
    SUITE(
        "Suite",
        Money.of(200.0),
        "Lit 2 places, Wifi, TV écran plat, Minibar, Climatiseur, Baignoire, Terrasse"
    );
    
    private final String displayName;
    private final Money pricePerNight;
    private final String amenities;
    
    RoomType(String displayName, Money pricePerNight, String amenities) {
        this.displayName = displayName;
        this.pricePerNight = pricePerNight;
        this.amenities = amenities;
    }
    
    public String getDisplayName() {
        return displayName;
    }
    
    public Money getPricePerNight() {
        return pricePerNight;
    }
    
    public String getAmenities() {
        return amenities;
    }
    
    /**
     * Calcule le prix total pour un nombre de nuits donné
     * @param numberOfNights Le nombre de nuits
     * @return Le prix total
     */
    public Money calculatePrice(int numberOfNights) {
        if (numberOfNights <= 0) {
            throw new IllegalArgumentException("Le nombre de nuits doit être positif");
        }
        return pricePerNight.multiply(numberOfNights);
    }
}
