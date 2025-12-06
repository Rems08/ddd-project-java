package com.xyzhotel.domain.account;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests unitaires pour le Value Object PhoneNumber
 */
class PhoneNumberTest {
    
    @Test
    void shouldCreateValidPhoneNumber() {
        PhoneNumber phone = PhoneNumber.of("0612345678");
        
        assertEquals("0612345678", phone.value());
    }
    
    @Test
    void shouldCreatePhoneNumberWithCountryCode() {
        PhoneNumber phone = PhoneNumber.of("+33612345678");
        
        assertEquals("+33612345678", phone.value());
    }
    
    @Test
    void shouldCreateInternationalPhoneNumber() {
        PhoneNumber phone = PhoneNumber.of("+14155552671");
        
        assertEquals("+14155552671", phone.value());
    }
    
    @Test
    void shouldAcceptPhoneNumberWithSpaces() {
        PhoneNumber phone = PhoneNumber.of("06 12 34 56 78");
        
        assertEquals("06 12 34 56 78", phone.value());
    }
    
    @Test
    void shouldAcceptPhoneNumberWithDashes() {
        PhoneNumber phone = PhoneNumber.of("06-12-34-56-78");
        
        assertEquals("06-12-34-56-78", phone.value());
    }
    
    @Test
    void shouldNotCreatePhoneNumberWithNull() {
        assertThrows(NullPointerException.class, () -> PhoneNumber.of(null));
    }
    
    @Test
    void shouldNotCreatePhoneNumberTooShort() {
        assertThrows(IllegalArgumentException.class, () -> PhoneNumber.of("123"));
    }
    
    @Test
    void shouldNotCreatePhoneNumberTooLong() {
        assertThrows(IllegalArgumentException.class, () -> PhoneNumber.of("12345678901234567890"));
    }
    
    @Test
    void shouldNotCreatePhoneNumberWithLetters() {
        assertThrows(IllegalArgumentException.class, () -> PhoneNumber.of("06abc12345"));
    }
    
    @Test
    void shouldNotCreatePhoneNumberWithSpecialCharacters() {
        assertThrows(IllegalArgumentException.class, () -> PhoneNumber.of("06@12#34"));
    }
    
    @Test
    void shouldHaveEqualityBasedOnValue() {
        PhoneNumber phone1 = PhoneNumber.of("0612345678");
        PhoneNumber phone2 = PhoneNumber.of("0612345678");
        PhoneNumber phone3 = PhoneNumber.of("0698765432");
        
        assertEquals(phone1, phone2);
        assertNotEquals(phone1, phone3);
    }
}
