package com.xyzhotel.domain.room;

import com.xyzhotel.domain.shared.EntityId;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests unitaires pour l'entité Room
 */
class RoomTest {
    
    @Test
    void shouldCreateRoomWithValidData() {
        Room room = Room.create("101", RoomType.STANDARD);
        
        assertNotNull(room);
        assertNotNull(room.getId());
        assertEquals("101", room.getRoomNumber());
        assertEquals(RoomType.STANDARD, room.getType());
        assertTrue(room.isAvailable());
    }
    
    @Test
    void shouldGenerateUniqueIdForEachRoom() {
        Room room1 = Room.create("101", RoomType.STANDARD);
        Room room2 = Room.create("102", RoomType.STANDARD);
        
        assertNotEquals(room1.getId(), room2.getId());
    }
    
    @Test
    void shouldCreateRoomWithDifferentTypes() {
        Room standard = Room.create("101", RoomType.STANDARD);
        Room superior = Room.create("201", RoomType.SUPERIOR);
        Room suite = Room.create("301", RoomType.SUITE);
        
        assertEquals(RoomType.STANDARD, standard.getType());
        assertEquals(RoomType.SUPERIOR, superior.getType());
        assertEquals(RoomType.SUITE, suite.getType());
    }
    
    @Test
    void shouldNotCreateRoomWithNullRoomNumber() {
        assertThrows(IllegalArgumentException.class, 
            () -> Room.create(null, RoomType.STANDARD));
    }
    
    @Test
    void shouldNotCreateRoomWithBlankRoomNumber() {
        assertThrows(IllegalArgumentException.class, 
            () -> Room.create("", RoomType.STANDARD));
        assertThrows(IllegalArgumentException.class, 
            () -> Room.create("   ", RoomType.STANDARD));
    }
    
    @Test
    void shouldNotCreateRoomWithNullRoomType() {
        assertThrows(NullPointerException.class, 
            () -> Room.create("101", null));
    }
    
    @Test
    void shouldMarkRoomAsOccupied() throws RoomException {
        Room room = Room.create("101", RoomType.STANDARD);
        
        room.markAsOccupied();
        
        assertFalse(room.isAvailable());
    }
    
    @Test
    void shouldNotMarkOccupiedRoomAsOccupiedAgain() throws RoomException {
        Room room = Room.create("101", RoomType.STANDARD);
        room.markAsOccupied();
        
        RoomException exception = assertThrows(RoomException.class, 
            () -> room.markAsOccupied());
        
        assertTrue(exception.getMessage().contains("n'est pas disponible"));
    }
    
    @Test
    void shouldMarkRoomAsAvailable() throws RoomException {
        Room room = Room.create("101", RoomType.STANDARD);
        room.markAsOccupied();
        
        room.markAsAvailable();
        
        assertTrue(room.isAvailable());
    }
    
    @Test
    void shouldAllowMarkingAlreadyAvailableRoomAsAvailable() {
        Room room = Room.create("101", RoomType.STANDARD);
        
        assertDoesNotThrow(() -> room.markAsAvailable());
        assertTrue(room.isAvailable());
    }
    
    @Test
    void shouldReconstituteRoomFromPersistence() {
        EntityId id = EntityId.generate();
        
        Room room = Room.reconstitute(id, "101", RoomType.STANDARD, false);
        
        assertNotNull(room);
        assertEquals(id, room.getId());
        assertEquals("101", room.getRoomNumber());
        assertEquals(RoomType.STANDARD, room.getType());
        assertFalse(room.isAvailable());
    }
    
    @Test
    void shouldHaveEqualityBasedOnId() {
        EntityId id = EntityId.generate();
        
        Room room1 = Room.reconstitute(id, "101", RoomType.STANDARD, true);
        Room room2 = Room.reconstitute(id, "101", RoomType.STANDARD, true);
        
        assertEquals(room1, room2);
        assertEquals(room1.hashCode(), room2.hashCode());
    }
    
    @Test
    void shouldNotBeEqualWithDifferentIds() {
        Room room1 = Room.create("101", RoomType.STANDARD);
        Room room2 = Room.create("101", RoomType.STANDARD);
        
        assertNotEquals(room1, room2);
    }
}
