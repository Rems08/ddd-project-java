package com.xyzhotel.domain.room;

import com.xyzhotel.domain.shared.Money;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests unitaires pour l'énumération RoomType
 */
class RoomTypeTest {
    
    @Test
    void shouldHaveCorrectPriceForStandardRoom() {
        assertEquals(Money.of(50.0), RoomType.STANDARD.getPricePerNight());
    }
    
    @Test
    void shouldHaveCorrectPriceForSuperiorRoom() {
        assertEquals(Money.of(100.0), RoomType.SUPERIOR.getPricePerNight());
    }
    
    @Test
    void shouldHaveCorrectPriceForSuite() {
        assertEquals(Money.of(200.0), RoomType.SUITE.getPricePerNight());
    }
    
    @Test
    void shouldHaveDisplayNames() {
        assertEquals("Chambre Standard", RoomType.STANDARD.getDisplayName());
        assertEquals("Chambre Supérieure", RoomType.SUPERIOR.getDisplayName());
        assertEquals("Suite", RoomType.SUITE.getDisplayName());
    }
    
    @Test
    void shouldHaveAmenities() {
        assertNotNull(RoomType.STANDARD.getAmenities());
        assertNotNull(RoomType.SUPERIOR.getAmenities());
        assertNotNull(RoomType.SUITE.getAmenities());
        
        assertTrue(RoomType.STANDARD.getAmenities().contains("Lit 1 place"));
        assertTrue(RoomType.SUPERIOR.getAmenities().contains("Lit 2 places"));
        assertTrue(RoomType.SUITE.getAmenities().contains("Terrasse"));
    }
    
    @Test
    void shouldCalculatePriceForMultipleNights() {
        Money price = RoomType.STANDARD.calculatePrice(3);
        
        assertEquals(Money.of(150.0), price); // 50 * 3 = 150
    }
    
    @Test
    void shouldCalculatePriceForSuperiorRoom() {
        Money price = RoomType.SUPERIOR.calculatePrice(5);
        
        assertEquals(Money.of(500.0), price); // 100 * 5 = 500
    }
    
    @Test
    void shouldCalculatePriceForSuite() {
        Money price = RoomType.SUITE.calculatePrice(2);
        
        assertEquals(Money.of(400.0), price); // 200 * 2 = 400
    }
    
    @Test
    void shouldNotCalculatePriceForZeroNights() {
        assertThrows(IllegalArgumentException.class, 
            () -> RoomType.STANDARD.calculatePrice(0));
    }
    
    @Test
    void shouldNotCalculatePriceForNegativeNights() {
        assertThrows(IllegalArgumentException.class, 
            () -> RoomType.STANDARD.calculatePrice(-1));
    }
    
    @Test
    void shouldHaveAllExpectedRoomTypes() {
        RoomType[] types = RoomType.values();
        
        assertEquals(3, types.length);
        assertArrayEquals(
            new RoomType[]{RoomType.STANDARD, RoomType.SUPERIOR, RoomType.SUITE},
            types
        );
    }
}
