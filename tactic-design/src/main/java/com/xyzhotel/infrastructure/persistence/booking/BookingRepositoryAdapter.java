package com.xyzhotel.infrastructure.persistence.booking;

import com.xyzhotel.domain.booking.*;
import com.xyzhotel.domain.room.RoomType;
import com.xyzhotel.domain.shared.EntityId;
import com.xyzhotel.domain.shared.Money;
import org.springframework.stereotype.Component;
import java.util.List;
import java.util.Optional;

/**
 * Adapter pour la persistence des réservations avec PostgreSQL
 */
@Component
public class BookingRepositoryAdapter implements BookingRepository {
    
    private final JpaBookingRepository jpaRepository;
    
    public BookingRepositoryAdapter(JpaBookingRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }
    
    @Override
    public Booking save(Booking booking) {
        BookingEntity entity = toEntity(booking);
        jpaRepository.save(entity);
        return booking;
    }
    
    @Override
    public Optional<Booking> findById(EntityId id) {
        return jpaRepository.findById(id.value())
            .map(this::toDomain);
    }
    
    @Override
    public List<Booking> findByAccountId(EntityId accountId) {
        return jpaRepository.findByAccountId(accountId.value()).stream()
            .map(this::toDomain)
            .toList();
    }
    
    @Override
    public List<Booking> findByRoomId(EntityId roomId) {
        return jpaRepository.findByRoomId(roomId.value()).stream()
            .map(this::toDomain)
            .toList();
    }
    
    @Override
    public List<Booking> findAll() {
        return jpaRepository.findAll().stream()
            .map(this::toDomain)
            .toList();
    }
    
    private BookingEntity toEntity(Booking booking) {
        BookingEntity entity = new BookingEntity(
            booking.getId().value(),
            booking.getAccountId().value(),
            booking.getStayPeriod().checkInDate(),
            booking.getStayPeriod().numberOfNights(),
            booking.getTotalAmount().amount(),
            booking.getPaidAmount().amount(),
            booking.getStatus().name(),
            booking.getCreatedAt()
        );
        
        for (BookingItem item : booking.getItems()) {
            BookingItemEntity itemEntity = new BookingItemEntity(
                item.getRoomId().value(),
                item.getRoomType().name(),
                item.getPricePerNight().amount()
            );
            entity.addItem(itemEntity);
        }
        
        return entity;
    }
    
    private Booking toDomain(BookingEntity entity) {
        List<BookingItem> items = entity.getItems().stream()
            .map(itemEntity -> new BookingItem(
                EntityId.of(itemEntity.getRoomId()),
                RoomType.valueOf(itemEntity.getRoomType()),
                Money.of(itemEntity.getPricePerNight())
            ))
            .toList();
        
        return Booking.reconstitute(
            EntityId.of(entity.getId()),
            EntityId.of(entity.getAccountId()),
            StayPeriod.of(entity.getCheckInDate(), entity.getNumberOfNights()),
            items,
            Money.of(entity.getTotalAmount()),
            Money.of(entity.getPaidAmount()),
            BookingStatus.valueOf(entity.getStatus()),
            entity.getCreatedAt()
        );
    }
}
