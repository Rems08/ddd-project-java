package com.xyzhotel.domain.account;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests unitaires pour le Value Object FullName
 */
class FullNameTest {
    
    @Test
    void shouldCreateValidFullName() {
        FullName fullName = FullName.of("John Doe");
        
        assertEquals("John Doe", fullName.value());
    }
    
    @Test
    void shouldTrimWhitespace() {
        FullName fullName = FullName.of("  John Doe  ");
        
        assertEquals("John Doe", fullName.value());
    }
    
    @Test
    void shouldAcceptNameWithSpecialCharacters() {
        FullName fullName = FullName.of("Jean-Pierre O'Connor");
        
        assertEquals("Jean-Pierre O'Connor", fullName.value());
    }
    
    @Test
    void shouldAcceptLongName() {
        String longName = "Jean-Baptiste Marie François de La Tour d'Auvergne";
        FullName fullName = FullName.of(longName);
        
        assertEquals(longName, fullName.value());
    }
    
    @Test
    void shouldNotCreateFullNameWithNull() {
        assertThrows(NullPointerException.class, () -> FullName.of(null));
    }
    
    @Test
    void shouldNotCreateBlankFullName() {
        assertThrows(IllegalArgumentException.class, () -> FullName.of(""));
        assertThrows(IllegalArgumentException.class, () -> FullName.of("   "));
    }
    
    @Test
    void shouldNotCreateFullNameWithOnlyOneCharacter() {
        assertThrows(IllegalArgumentException.class, () -> FullName.of("A"));
    }
    
    @Test
    void shouldAcceptTwoCharacterName() {
        FullName fullName = FullName.of("Li");
        
        assertEquals("Li", fullName.value());
    }
    
    @Test
    void shouldHaveEqualityBasedOnValue() {
        FullName name1 = FullName.of("John Doe");
        FullName name2 = FullName.of("John Doe");
        FullName name3 = FullName.of("Jane Doe");
        
        assertEquals(name1, name2);
        assertNotEquals(name1, name3);
    }
}
