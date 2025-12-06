package com.xyzhotel.domain.room;

import com.xyzhotel.domain.shared.EntityId;
import java.util.Objects;

/**
 * Entity représentant une chambre d'hôtel
 */
public class Room {
    
    private final EntityId id;
    private final String roomNumber;
    private final RoomType type;
    private boolean isAvailable;
    
    private Room(EntityId id, String roomNumber, RoomType type, boolean isAvailable) {
        this.id = Objects.requireNonNull(id, "L'identifiant de la chambre ne peut pas être null");
        this.roomNumber = Objects.requireNonNull(roomNumber, "Le numéro de chambre ne peut pas être null");
        this.type = Objects.requireNonNull(type, "Le type de chambre ne peut pas être null");
        this.isAvailable = isAvailable;
    }
    
    /**
     * Crée une nouvelle chambre
     * @param roomNumber Le numéro de la chambre
     * @param type Le type de chambre
     * @return Une nouvelle chambre
     */
    public static Room create(String roomNumber, RoomType type) {
        if (roomNumber == null || roomNumber.isBlank()) {
            throw new IllegalArgumentException("Le numéro de chambre ne peut pas être vide");
        }
        return new Room(EntityId.generate(), roomNumber, type, true);
    }
    
    /**
     * Reconstitue une chambre existante depuis la persistence
     */
    public static Room reconstitute(EntityId id, String roomNumber, RoomType type, boolean isAvailable) {
        return new Room(id, roomNumber, type, isAvailable);
    }
    
    /**
     * Marque la chambre comme occupée
     * @throws RoomException Si la chambre n'est pas disponible
     */
    public void markAsOccupied() throws RoomException {
        if (!this.isAvailable) {
            throw new RoomException("La chambre " + roomNumber + " n'est pas disponible");
        }
        this.isAvailable = false;
    }
    
    /**
     * Marque la chambre comme disponible
     */
    public void markAsAvailable() {
        this.isAvailable = true;
    }
    
    public EntityId getId() {
        return id;
    }
    
    public String getRoomNumber() {
        return roomNumber;
    }
    
    public RoomType getType() {
        return type;
    }
    
    public boolean isAvailable() {
        return isAvailable;
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Room room = (Room) o;
        return Objects.equals(id, room.id);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
