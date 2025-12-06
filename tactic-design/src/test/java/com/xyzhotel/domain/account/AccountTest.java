package com.xyzhotel.domain.account;

import com.xyzhotel.domain.shared.EntityId;
import org.junit.jupiter.api.Test;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests unitaires pour l'entité Account
 */
class AccountTest {
    
    @Test
    void shouldCreateAccountWithValidData() {
        FullName fullName = FullName.of("John Doe");
        Email email = Email.of("john.doe@example.com");
        PhoneNumber phone = PhoneNumber.of("0612345678");
        
        Account account = Account.create(fullName, email, phone);
        
        assertNotNull(account);
        assertNotNull(account.getId());
        assertEquals(fullName, account.getFullName());
        assertEquals(email, account.getEmail());
        assertEquals(phone, account.getPhoneNumber());
        assertNotNull(account.getWallet());
        assertNotNull(account.getCreatedAt());
    }
    
    @Test
    void shouldGenerateUniqueIdForEachAccount() {
        FullName fullName = FullName.of("John Doe");
        Email email = Email.of("john.doe@example.com");
        PhoneNumber phone = PhoneNumber.of("0612345678");
        
        Account account1 = Account.create(fullName, email, phone);
        Account account2 = Account.create(fullName, email, phone);
        
        assertNotEquals(account1.getId(), account2.getId());
    }
    
    @Test
    void shouldInitializeWalletWithAccountId() {
        FullName fullName = FullName.of("John Doe");
        Email email = Email.of("john.doe@example.com");
        PhoneNumber phone = PhoneNumber.of("0612345678");
        
        Account account = Account.create(fullName, email, phone);
        
        assertEquals(account.getId(), account.getWallet().getAccountId());
    }
    
    @Test
    void shouldNotCreateAccountWithNullFullName() {
        Email email = Email.of("john.doe@example.com");
        PhoneNumber phone = PhoneNumber.of("0612345678");
        
        assertThrows(NullPointerException.class, 
            () -> Account.create(null, email, phone));
    }
    
    @Test
    void shouldNotCreateAccountWithNullEmail() {
        FullName fullName = FullName.of("John Doe");
        PhoneNumber phone = PhoneNumber.of("0612345678");
        
        assertThrows(NullPointerException.class, 
            () -> Account.create(fullName, null, phone));
    }
    
    @Test
    void shouldNotCreateAccountWithNullPhoneNumber() {
        FullName fullName = FullName.of("John Doe");
        Email email = Email.of("john.doe@example.com");
        
        assertThrows(NullPointerException.class, 
            () -> Account.create(fullName, email, null));
    }
    
    @Test
    void shouldReconstituteAccountFromPersistence() {
        EntityId id = EntityId.generate();
        FullName fullName = FullName.of("John Doe");
        Email email = Email.of("john.doe@example.com");
        PhoneNumber phone = PhoneNumber.of("0612345678");
        LocalDateTime createdAt = LocalDateTime.now().minusDays(1);
        
        Account account = Account.reconstitute(id, fullName, email, phone, createdAt);
        
        assertNotNull(account);
        assertEquals(id, account.getId());
        assertEquals(fullName, account.getFullName());
        assertEquals(email, account.getEmail());
        assertEquals(phone, account.getPhoneNumber());
        assertEquals(createdAt, account.getCreatedAt());
    }
    
    @Test
    void shouldHaveEqualityBasedOnId() {
        EntityId id = EntityId.generate();
        FullName fullName = FullName.of("John Doe");
        Email email = Email.of("john.doe@example.com");
        PhoneNumber phone = PhoneNumber.of("0612345678");
        LocalDateTime createdAt = LocalDateTime.now();
        
        Account account1 = Account.reconstitute(id, fullName, email, phone, createdAt);
        Account account2 = Account.reconstitute(id, fullName, email, phone, createdAt);
        
        assertEquals(account1, account2);
        assertEquals(account1.hashCode(), account2.hashCode());
    }
    
    @Test
    void shouldNotBeEqualWithDifferentIds() {
        FullName fullName = FullName.of("John Doe");
        Email email = Email.of("john.doe@example.com");
        PhoneNumber phone = PhoneNumber.of("0612345678");
        
        Account account1 = Account.create(fullName, email, phone);
        Account account2 = Account.create(fullName, email, phone);
        
        assertNotEquals(account1, account2);
    }
}
