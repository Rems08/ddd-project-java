package com.xyzhotel.infrastructure.persistence.room;

import com.xyzhotel.domain.room.Room;
import com.xyzhotel.domain.room.RoomRepository;
import com.xyzhotel.domain.room.RoomType;
import com.xyzhotel.domain.shared.EntityId;
import org.springframework.stereotype.Component;
import java.util.List;
import java.util.Optional;

/**
 * Adapter pour la persistence des chambres avec PostgreSQL
 */
@Component
public class RoomRepositoryAdapter implements RoomRepository {
    
    private final JpaRoomRepository jpaRepository;
    
    public RoomRepositoryAdapter(JpaRoomRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }
    
    @Override
    public Room save(Room room) {
        RoomEntity entity = toEntity(room);
        jpaRepository.save(entity);
        return room;
    }
    
    @Override
    public Optional<Room> findById(EntityId id) {
        return jpaRepository.findById(id.value())
            .map(this::toDomain);
    }
    
    @Override
    public List<Room> findAll() {
        return jpaRepository.findAll().stream()
            .map(this::toDomain)
            .toList();
    }
    
    @Override
    public List<Room> findAvailableByType(RoomType type) {
        return jpaRepository.findByRoomTypeAndIsAvailable(type.name(), true).stream()
            .map(this::toDomain)
            .toList();
    }
    
    @Override
    public long countAvailable() {
        return jpaRepository.countAvailable();
    }
    
    @Override
    public long countOccupied() {
        return jpaRepository.countOccupied();
    }
    
    private RoomEntity toEntity(Room room) {
        return new RoomEntity(
            room.getId().value(),
            room.getRoomNumber(),
            room.getType().name(),
            room.isAvailable()
        );
    }
    
    private Room toDomain(RoomEntity entity) {
        return Room.reconstitute(
            EntityId.of(entity.getId()),
            entity.getRoomNumber(),
            RoomType.valueOf(entity.getRoomType()),
            entity.getIsAvailable()
        );
    }
}
