package com.xyzhotel.domain.account;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests unitaires pour le Value Object Email
 */
class EmailTest {
    
    @Test
    void shouldCreateValidEmail() {
        Email email = Email.of("john.doe@example.com");
        
        assertEquals("john.doe@example.com", email.value());
    }
    
    @Test
    void shouldAcceptEmailWithPlus() {
        Email email = Email.of("john+test@example.com");
        
        assertEquals("john+test@example.com", email.value());
    }
    
    @Test
    void shouldAcceptEmailWithUnderscore() {
        Email email = Email.of("john_doe@example.com");
        
        assertEquals("john_doe@example.com", email.value());
    }
    
    @Test
    void shouldAcceptEmailWithDash() {
        Email email = Email.of("john-doe@example.com");
        
        assertEquals("john-doe@example.com", email.value());
    }
    
    @Test
    void shouldAcceptEmailWithDot() {
        Email email = Email.of("john.doe@example.com");
        
        assertEquals("john.doe@example.com", email.value());
    }
    
    @Test
    void shouldNotCreateEmailWithNull() {
        assertThrows(NullPointerException.class, () -> Email.of(null));
    }
    
    @Test
    void shouldNotCreateBlankEmail() {
        assertThrows(IllegalArgumentException.class, () -> Email.of(""));
        assertThrows(IllegalArgumentException.class, () -> Email.of("   "));
    }
    
    @Test
    void shouldNotCreateEmailWithoutAtSign() {
        assertThrows(IllegalArgumentException.class, () -> Email.of("johndoe.example.com"));
    }
    
    @Test
    void shouldNotCreateEmailWithoutDomain() {
        assertThrows(IllegalArgumentException.class, () -> Email.of("john@"));
    }
    
    @Test
    void shouldNotCreateEmailWithoutLocalPart() {
        assertThrows(IllegalArgumentException.class, () -> Email.of("@example.com"));
    }
    
    @Test
    void shouldNotCreateEmailWithoutTopLevelDomain() {
        assertThrows(IllegalArgumentException.class, () -> Email.of("john@example"));
    }
    
    @Test
    void shouldNotCreateEmailWithSpaces() {
        assertThrows(IllegalArgumentException.class, () -> Email.of("john doe@example.com"));
    }
    
    @Test
    void shouldHaveEqualityBasedOnValue() {
        Email email1 = Email.of("john@example.com");
        Email email2 = Email.of("john@example.com");
        Email email3 = Email.of("jane@example.com");
        
        assertEquals(email1, email2);
        assertNotEquals(email1, email3);
    }
}
