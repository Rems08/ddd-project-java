package com.xyzhotel.domain.booking;

import com.xyzhotel.domain.room.RoomType;
import com.xyzhotel.domain.shared.EntityId;
import com.xyzhotel.domain.shared.Money;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests unitaires pour l'entité Booking
 */
class BookingTest {
    
    private EntityId accountId;
    private StayPeriod stayPeriod;
    private List<BookingItem> items;
    
    @BeforeEach
    void setUp() {
        accountId = EntityId.generate();
        stayPeriod = StayPeriod.of(LocalDate.of(2024, 1, 15), 3);
        items = new ArrayList<>();
        items.add(new BookingItem(EntityId.generate(), RoomType.STANDARD, Money.of(50.0)));
    }
    
    @Test
    void shouldCreateBookingWithValidData() {
        Booking booking = Booking.create(accountId, stayPeriod, items);
        
        assertNotNull(booking);
        assertNotNull(booking.getId());
        assertEquals(accountId, booking.getAccountId());
        assertEquals(stayPeriod, booking.getStayPeriod());
        assertEquals(1, booking.getItems().size());
        assertEquals(BookingStatus.PENDING, booking.getStatus());
        assertNotNull(booking.getCreatedAt());
    }
    
    @Test
    void shouldCalculateTotalAmountCorrectly() {
        Booking booking = Booking.create(accountId, stayPeriod, items);
        
        // 50€ per night * 3 nights = 150€
        assertEquals(Money.of(150.0), booking.getTotalAmount());
    }
    
    @Test
    void shouldCalculateTotalAmountForMultipleRooms() {
        items.add(new BookingItem(EntityId.generate(), RoomType.SUPERIOR, Money.of(100.0)));
        Booking booking = Booking.create(accountId, stayPeriod, items);
        
        // (50€ * 3) + (100€ * 3) = 150€ + 300€ = 450€
        assertEquals(Money.of(450.0), booking.getTotalAmount());
    }
    
    @Test
    void shouldPay50PercentDepositAtCreation() {
        Booking booking = Booking.create(accountId, stayPeriod, items);
        
        // Total: 150€, Deposit: 50% = 75€
        assertEquals(Money.of(75.0), booking.getPaidAmount());
        assertEquals(Money.of(75.0), booking.getDepositAmount());
    }
    
    @Test
    void shouldCalculateRemainingAmountCorrectly() throws BookingException {
        Booking booking = Booking.create(accountId, stayPeriod, items);
        
        Money remaining = booking.getRemainingAmount();
        
        // Total: 150€, Paid: 75€, Remaining: 75€
        assertEquals(Money.of(75.0), remaining);
    }
    
    @Test
    void shouldConfirmBookingAndPayRemainingAmount() throws BookingException {
        Booking booking = Booking.create(accountId, stayPeriod, items);
        
        Money remainingAmount = booking.confirm();
        
        assertEquals(BookingStatus.CONFIRMED, booking.getStatus());
        assertEquals(Money.of(75.0), remainingAmount);
        assertEquals(Money.of(150.0), booking.getPaidAmount());
        assertEquals(0, Money.zero().amount().compareTo(booking.getRemainingAmount().amount()));
    }
    
    @Test
    void shouldNotConfirmAlreadyConfirmedBooking() throws BookingException {
        Booking booking = Booking.create(accountId, stayPeriod, items);
        booking.confirm();
        
        BookingException exception = assertThrows(BookingException.class, 
            () -> booking.confirm());
        
        assertTrue(exception.getMessage().contains("déjà confirmée"));
    }
    
    @Test
    void shouldNotConfirmCancelledBooking() throws BookingException {
        Booking booking = Booking.create(accountId, stayPeriod, items);
        booking.cancel();
        
        BookingException exception = assertThrows(BookingException.class, 
            () -> booking.confirm());
        
        assertTrue(exception.getMessage().contains("annulée"));
    }
    
    @Test
    void shouldCancelPendingBooking() throws BookingException {
        Booking booking = Booking.create(accountId, stayPeriod, items);
        
        booking.cancel();
        
        assertEquals(BookingStatus.CANCELLED, booking.getStatus());
    }
    
    @Test
    void shouldCancelConfirmedBooking() throws BookingException {
        Booking booking = Booking.create(accountId, stayPeriod, items);
        booking.confirm();
        
        booking.cancel();
        
        assertEquals(BookingStatus.CANCELLED, booking.getStatus());
    }
    
    @Test
    void shouldNotCancelAlreadyCancelledBooking() throws BookingException {
        Booking booking = Booking.create(accountId, stayPeriod, items);
        booking.cancel();
        
        BookingException exception = assertThrows(BookingException.class, 
            () -> booking.cancel());
        
        assertTrue(exception.getMessage().contains("déjà annulée"));
    }
    
    @Test
    void shouldNotCreateBookingWithNullAccountId() {
        assertThrows(NullPointerException.class, 
            () -> Booking.create(null, stayPeriod, items));
    }
    
    @Test
    void shouldNotCreateBookingWithNullStayPeriod() {
        assertThrows(NullPointerException.class, 
            () -> Booking.create(accountId, null, items));
    }
    
    @Test
    void shouldNotCreateBookingWithNullItems() {
        assertThrows(NullPointerException.class, 
            () -> Booking.create(accountId, stayPeriod, null));
    }
    
    @Test
    void shouldNotCreateBookingWithEmptyItems() {
        List<BookingItem> emptyItems = new ArrayList<>();
        
        assertThrows(IllegalArgumentException.class, 
            () -> Booking.create(accountId, stayPeriod, emptyItems));
    }
    
    @Test
    void shouldReturnUnmodifiableListOfItems() {
        Booking booking = Booking.create(accountId, stayPeriod, items);
        List<BookingItem> retrievedItems = booking.getItems();
        
        assertThrows(UnsupportedOperationException.class, 
            () -> retrievedItems.add(new BookingItem(EntityId.generate(), RoomType.SUITE, Money.of(200.0))));
    }
    
    @Test
    void shouldReconstituteBookingFromPersistence() {
        EntityId id = EntityId.generate();
        Money totalAmount = Money.of(150.0);
        Money paidAmount = Money.of(150.0);
        
        Booking booking = Booking.reconstitute(
            id, accountId, stayPeriod, items, 
            totalAmount, paidAmount, BookingStatus.CONFIRMED, 
            java.time.LocalDateTime.now()
        );
        
        assertNotNull(booking);
        assertEquals(id, booking.getId());
        assertEquals(BookingStatus.CONFIRMED, booking.getStatus());
        assertEquals(totalAmount, booking.getTotalAmount());
        assertEquals(paidAmount, booking.getPaidAmount());
    }
    
    @Test
    void shouldHaveEqualityBasedOnId() {
        EntityId id = EntityId.generate();
        Money totalAmount = Money.of(150.0);
        Money paidAmount = Money.of(75.0);
        
        Booking booking1 = Booking.reconstitute(
            id, accountId, stayPeriod, items, 
            totalAmount, paidAmount, BookingStatus.PENDING, 
            java.time.LocalDateTime.now()
        );
        Booking booking2 = Booking.reconstitute(
            id, accountId, stayPeriod, items, 
            totalAmount, paidAmount, BookingStatus.PENDING, 
            java.time.LocalDateTime.now()
        );
        
        assertEquals(booking1, booking2);
        assertEquals(booking1.hashCode(), booking2.hashCode());
    }
    
    @Test
    void shouldNotBeEqualWithDifferentIds() {
        Booking booking1 = Booking.create(accountId, stayPeriod, items);
        Booking booking2 = Booking.create(accountId, stayPeriod, items);
        
        assertNotEquals(booking1, booking2);
    }
    
    @Test
    void shouldHandleComplexBookingScenario() throws BookingException {
        // Create a booking with multiple rooms
        items.add(new BookingItem(EntityId.generate(), RoomType.SUPERIOR, Money.of(100.0)));
        items.add(new BookingItem(EntityId.generate(), RoomType.SUITE, Money.of(200.0)));
        
        // Stay for 5 nights
        StayPeriod longStay = StayPeriod.of(LocalDate.of(2024, 2, 1), 5);
        
        Booking booking = Booking.create(accountId, longStay, items);
        
        // Total: (50 * 5) + (100 * 5) + (200 * 5) = 250 + 500 + 1000 = 1750€
        assertEquals(Money.of(1750.0), booking.getTotalAmount());
        
        // Deposit: 50% = 875€
        assertEquals(Money.of(875.0), booking.getPaidAmount());
        
        // Remaining: 875€
        assertEquals(Money.of(875.0), booking.getRemainingAmount());
        
        // Confirm booking
        Money remainingPaid = booking.confirm();
        assertEquals(Money.of(875.0), remainingPaid);
        assertEquals(BookingStatus.CONFIRMED, booking.getStatus());
        assertEquals(Money.of(1750.0), booking.getPaidAmount());
    }
}
