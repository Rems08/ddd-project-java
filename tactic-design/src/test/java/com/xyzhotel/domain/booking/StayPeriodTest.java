package com.xyzhotel.domain.booking;

import org.junit.jupiter.api.Test;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests unitaires pour le Value Object StayPeriod
 */
class StayPeriodTest {
    
    @Test
    void shouldCreateValidStayPeriod() {
        LocalDate checkIn = LocalDate.of(2024, 1, 15);
        StayPeriod period = StayPeriod.of(checkIn, 3);
        
        assertEquals(checkIn, period.checkInDate());
        assertEquals(3, period.numberOfNights());
    }
    
    @Test
    void shouldCalculateCheckOutDate() {
        LocalDate checkIn = LocalDate.of(2024, 1, 15);
        StayPeriod period = StayPeriod.of(checkIn, 3);
        
        LocalDate checkOut = period.getCheckOutDate();
        
        assertEquals(LocalDate.of(2024, 1, 18), checkOut);
    }
    
    @Test
    void shouldNotCreateStayPeriodWithNullCheckInDate() {
        assertThrows(NullPointerException.class, () -> StayPeriod.of(null, 3));
    }
    
    @Test
    void shouldNotCreateStayPeriodWithZeroNights() {
        LocalDate checkIn = LocalDate.of(2024, 1, 15);
        assertThrows(IllegalArgumentException.class, () -> StayPeriod.of(checkIn, 0));
    }
    
    @Test
    void shouldNotCreateStayPeriodWithNegativeNights() {
        LocalDate checkIn = LocalDate.of(2024, 1, 15);
        assertThrows(IllegalArgumentException.class, () -> StayPeriod.of(checkIn, -1));
    }
    
    @Test
    void shouldDetectOverlappingPeriods() {
        StayPeriod period1 = StayPeriod.of(LocalDate.of(2024, 1, 10), 5); // 10-15
        StayPeriod period2 = StayPeriod.of(LocalDate.of(2024, 1, 12), 3); // 12-15
        
        assertTrue(period1.overlaps(period2));
        assertTrue(period2.overlaps(period1));
    }
    
    @Test
    void shouldDetectOverlappingPeriodsAtStart() {
        StayPeriod period1 = StayPeriod.of(LocalDate.of(2024, 1, 10), 3); // 10-13
        StayPeriod period2 = StayPeriod.of(LocalDate.of(2024, 1, 12), 3); // 12-15
        
        assertTrue(period1.overlaps(period2));
        assertTrue(period2.overlaps(period1));
    }
    
    @Test
    void shouldDetectOverlappingPeriodsAtEnd() {
        StayPeriod period1 = StayPeriod.of(LocalDate.of(2024, 1, 15), 3); // 15-18
        StayPeriod period2 = StayPeriod.of(LocalDate.of(2024, 1, 10), 6); // 10-16
        
        assertTrue(period1.overlaps(period2));
        assertTrue(period2.overlaps(period1));
    }
    
    @Test
    void shouldNotDetectOverlapForAdjacentPeriods() {
        StayPeriod period1 = StayPeriod.of(LocalDate.of(2024, 1, 10), 3); // 10-13
        StayPeriod period2 = StayPeriod.of(LocalDate.of(2024, 1, 13), 3); // 13-16
        
        assertFalse(period1.overlaps(period2));
        assertFalse(period2.overlaps(period1));
    }
    
    @Test
    void shouldNotDetectOverlapForSeparatePeriods() {
        StayPeriod period1 = StayPeriod.of(LocalDate.of(2024, 1, 10), 3); // 10-13
        StayPeriod period2 = StayPeriod.of(LocalDate.of(2024, 1, 20), 3); // 20-23
        
        assertFalse(period1.overlaps(period2));
        assertFalse(period2.overlaps(period1));
    }
    
    @Test
    void shouldHaveEqualityBasedOnDateAndNights() {
        LocalDate checkIn = LocalDate.of(2024, 1, 15);
        StayPeriod period1 = StayPeriod.of(checkIn, 3);
        StayPeriod period2 = StayPeriod.of(checkIn, 3);
        StayPeriod period3 = StayPeriod.of(checkIn, 5);
        
        assertEquals(period1, period2);
        assertNotEquals(period1, period3);
    }
}
