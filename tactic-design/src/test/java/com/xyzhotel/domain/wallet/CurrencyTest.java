package com.xyzhotel.domain.wallet;

import org.junit.jupiter.api.Test;
import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests unitaires pour l'énumération Currency
 */
class CurrencyTest {
    
    @Test
    void shouldConvertEuroToEuro() {
        BigDecimal amount = new BigDecimal("100.00");
        BigDecimal converted = Currency.EUR.convertToEuro(amount);
        
        assertEquals(new BigDecimal("100.00"), converted);
    }
    
    @Test
    void shouldConvertUsdToEuro() {
        BigDecimal amount = new BigDecimal("100.00");
        BigDecimal converted = Currency.USD.convertToEuro(amount);
        
        // 100 USD * 0.92 = 92 EUR
        assertEquals(0, new BigDecimal("92.00").compareTo(converted));
    }
    
    @Test
    void shouldConvertGbpToEuro() {
        BigDecimal amount = new BigDecimal("100.00");
        BigDecimal converted = Currency.GBP.convertToEuro(amount);
        
        // 100 GBP * 1.17 = 117 EUR
        assertEquals(0, new BigDecimal("117.00").compareTo(converted));
    }
    
    @Test
    void shouldConvertJpyToEuro() {
        BigDecimal amount = new BigDecimal("1000.00");
        BigDecimal converted = Currency.JPY.convertToEuro(amount);
        
        // 1000 JPY * 0.0062 = 6.2 EUR
        assertEquals(0, new BigDecimal("6.20").compareTo(converted));
    }
    
    @Test
    void shouldConvertChfToEuro() {
        BigDecimal amount = new BigDecimal("100.00");
        BigDecimal converted = Currency.CHF.convertToEuro(amount);
        
        // 100 CHF * 1.05 = 105 EUR
        assertEquals(0, new BigDecimal("105.00").compareTo(converted));
    }
    
    @Test
    void shouldHaveAllExpectedCurrencies() {
        Currency[] currencies = Currency.values();
        
        assertEquals(5, currencies.length);
        assertArrayEquals(
            new Currency[]{Currency.EUR, Currency.USD, Currency.GBP, Currency.JPY, Currency.CHF},
            currencies
        );
    }
}
