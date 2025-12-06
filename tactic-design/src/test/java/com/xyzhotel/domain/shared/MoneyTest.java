package com.xyzhotel.domain.shared;

import org.junit.jupiter.api.Test;
import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests unitaires pour le Value Object Money
 */
class MoneyTest {
    
    @Test
    void shouldCreateMoneyWithValidAmount() {
        Money money = Money.of(100.0);
        
        assertEquals(BigDecimal.valueOf(100.0), money.amount());
    }
    
    @Test
    void shouldCreateZeroMoney() {
        Money money = Money.zero();
        
        assertEquals(BigDecimal.ZERO, money.amount());
    }
    
    @Test
    void shouldCreateMoneyFromBigDecimal() {
        BigDecimal amount = new BigDecimal("50.50");
        Money money = Money.of(amount);
        
        assertEquals(amount, money.amount());
    }
    
    @Test
    void shouldNotCreateMoneyWithNullAmount() {
        assertThrows(NullPointerException.class, () -> Money.of((BigDecimal) null));
    }
    
    @Test
    void shouldNotCreateMoneyWithNegativeAmount() {
        assertThrows(IllegalArgumentException.class, () -> Money.of(-10.0));
    }
    
    @Test
    void shouldAddTwoMoneyValues() {
        Money money1 = Money.of(50.0);
        Money money2 = Money.of(30.0);
        
        Money result = money1.add(money2);
        
        assertEquals(BigDecimal.valueOf(80.0), result.amount());
    }
    
    @Test
    void shouldSubtractTwoMoneyValues() throws DomainException {
        Money money1 = Money.of(100.0);
        Money money2 = Money.of(30.0);
        
        Money result = money1.subtract(money2);
        
        assertEquals(BigDecimal.valueOf(70.0), result.amount());
    }
    
    @Test
    void shouldNotSubtractWhenResultIsNegative() {
        Money money1 = Money.of(30.0);
        Money money2 = Money.of(100.0);
        
        assertThrows(DomainException.class, () -> money1.subtract(money2));
    }
    
    @Test
    void shouldMultiplyMoneyByInteger() {
        Money money = Money.of(25.0);
        
        Money result = money.multiply(4);
        
        assertEquals(BigDecimal.valueOf(100.0), result.amount());
    }
    
    @Test
    void shouldMultiplyByPercentage() {
        Money money = Money.of(100.0);
        
        Money result = money.multiplyByPercentage(50);
        
        assertEquals(BigDecimal.valueOf(50.0), result.amount());
    }
    
    @Test
    void shouldCompareMoneyValues() {
        Money small = Money.of(50.0);
        Money large = Money.of(100.0);
        Money equal = Money.of(50.0);
        
        assertTrue(large.isGreaterThan(small));
        assertFalse(small.isGreaterThan(large));
        assertFalse(small.isGreaterThan(equal));
        
        assertTrue(large.isGreaterThanOrEqual(small));
        assertTrue(small.isGreaterThanOrEqual(equal));
        assertFalse(small.isGreaterThanOrEqual(large));
        
        assertTrue(small.isLessThan(large));
        assertFalse(large.isLessThan(small));
        assertFalse(small.isLessThan(equal));
    }
    
    @Test
    void shouldBeImmutable() {
        Money original = Money.of(100.0);
        Money added = original.add(Money.of(50.0));
        
        assertEquals(BigDecimal.valueOf(100.0), original.amount());
        assertEquals(BigDecimal.valueOf(150.0), added.amount());
    }
    
    @Test
    void shouldHaveEqualityBasedOnAmount() {
        Money money1 = Money.of(100.0);
        Money money2 = Money.of(100.0);
        Money money3 = Money.of(50.0);
        
        assertEquals(money1, money2);
        assertNotEquals(money1, money3);
    }
}
