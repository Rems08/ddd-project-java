package com.xyzhotel.domain.booking;

import com.xyzhotel.domain.room.RoomType;
import com.xyzhotel.domain.shared.EntityId;
import com.xyzhotel.domain.shared.Money;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests unitaires pour la classe BookingItem
 */
class BookingItemTest {
    
    @Test
    void shouldCreateBookingItemWithValidData() {
        EntityId roomId = EntityId.generate();
        Money pricePerNight = Money.of(50.0);
        
        BookingItem item = new BookingItem(roomId, RoomType.STANDARD, pricePerNight);
        
        assertNotNull(item);
        assertEquals(roomId, item.getRoomId());
        assertEquals(RoomType.STANDARD, item.getRoomType());
        assertEquals(pricePerNight, item.getPricePerNight());
    }
    
    @Test
    void shouldCalculateTotalPriceForMultipleNights() {
        EntityId roomId = EntityId.generate();
        Money pricePerNight = Money.of(50.0);
        BookingItem item = new BookingItem(roomId, RoomType.STANDARD, pricePerNight);
        
        Money totalPrice = item.calculateTotalPrice(3);
        
        assertEquals(Money.of(150.0), totalPrice);
    }
    
    @Test
    void shouldCalculateTotalPriceForOneNight() {
        EntityId roomId = EntityId.generate();
        Money pricePerNight = Money.of(100.0);
        BookingItem item = new BookingItem(roomId, RoomType.SUPERIOR, pricePerNight);
        
        Money totalPrice = item.calculateTotalPrice(1);
        
        assertEquals(Money.of(100.0), totalPrice);
    }
    
    @Test
    void shouldCalculateTotalPriceForSuite() {
        EntityId roomId = EntityId.generate();
        Money pricePerNight = Money.of(200.0);
        BookingItem item = new BookingItem(roomId, RoomType.SUITE, pricePerNight);
        
        Money totalPrice = item.calculateTotalPrice(5);
        
        assertEquals(Money.of(1000.0), totalPrice);
    }
    
    @Test
    void shouldNotCreateBookingItemWithNullRoomId() {
        Money pricePerNight = Money.of(50.0);
        
        assertThrows(NullPointerException.class, 
            () -> new BookingItem(null, RoomType.STANDARD, pricePerNight));
    }
    
    @Test
    void shouldNotCreateBookingItemWithNullRoomType() {
        EntityId roomId = EntityId.generate();
        Money pricePerNight = Money.of(50.0);
        
        assertThrows(NullPointerException.class, 
            () -> new BookingItem(roomId, null, pricePerNight));
    }
    
    @Test
    void shouldNotCreateBookingItemWithNullPricePerNight() {
        EntityId roomId = EntityId.generate();
        
        assertThrows(NullPointerException.class, 
            () -> new BookingItem(roomId, RoomType.STANDARD, null));
    }
    
    @Test
    void shouldHaveEqualityBasedOnRoomId() {
        EntityId roomId = EntityId.generate();
        Money pricePerNight = Money.of(50.0);
        
        BookingItem item1 = new BookingItem(roomId, RoomType.STANDARD, pricePerNight);
        BookingItem item2 = new BookingItem(roomId, RoomType.STANDARD, pricePerNight);
        
        assertEquals(item1, item2);
        assertEquals(item1.hashCode(), item2.hashCode());
    }
    
    @Test
    void shouldNotBeEqualWithDifferentRoomIds() {
        EntityId roomId1 = EntityId.generate();
        EntityId roomId2 = EntityId.generate();
        Money pricePerNight = Money.of(50.0);
        
        BookingItem item1 = new BookingItem(roomId1, RoomType.STANDARD, pricePerNight);
        BookingItem item2 = new BookingItem(roomId2, RoomType.STANDARD, pricePerNight);
        
        assertNotEquals(item1, item2);
    }
}
