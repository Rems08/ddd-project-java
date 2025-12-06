package com.xyzhotel.domain.wallet;

import com.xyzhotel.domain.shared.EntityId;
import com.xyzhotel.domain.shared.Money;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests unitaires pour l'entité Wallet
 */
class WalletTest {
    
    private EntityId accountId;
    
    @BeforeEach
    void setUp() {
        accountId = EntityId.generate();
    }
    
    @Test
    void shouldCreateWalletWithZeroBalance() {
        Wallet wallet = new Wallet(accountId);
        
        assertEquals(Money.zero(), wallet.getBalance());
        assertEquals(accountId, wallet.getAccountId());
    }
    
    @Test
    void shouldCreateWalletWithInitialBalance() {
        Money initialBalance = Money.of(100.0);
        Wallet wallet = new Wallet(accountId, initialBalance);
        
        assertEquals(initialBalance, wallet.getBalance());
    }
    
    @Test
    void shouldNotCreateWalletWithNullAccountId() {
        assertThrows(NullPointerException.class, () -> new Wallet(null));
    }
    
    @Test
    void shouldCreditWalletInEuro() throws WalletException {
        Wallet wallet = new Wallet(accountId);
        
        wallet.credit(BigDecimal.valueOf(50.0), Currency.EUR);
        
        assertEquals(Money.of(50.0), wallet.getBalance());
    }
    
    @Test
    void shouldCreditWalletInUsd() throws WalletException {
        Wallet wallet = new Wallet(accountId);
        
        wallet.credit(BigDecimal.valueOf(100.0), Currency.USD);
        
        // 100 USD * 0.92 = 92 EUR
        assertEquals(0, Money.of(92.0).amount().compareTo(wallet.getBalance().amount()));
    }
    
    @Test
    void shouldCreditWalletInGbp() throws WalletException {
        Wallet wallet = new Wallet(accountId);
        
        wallet.credit(BigDecimal.valueOf(100.0), Currency.GBP);
        
        // 100 GBP * 1.17 = 117 EUR
        assertEquals(0, Money.of(117.0).amount().compareTo(wallet.getBalance().amount()));
    }
    
    @Test
    void shouldCreditWalletMultipleTimes() throws WalletException {
        Wallet wallet = new Wallet(accountId);
        
        wallet.credit(BigDecimal.valueOf(50.0), Currency.EUR);
        wallet.credit(BigDecimal.valueOf(30.0), Currency.EUR);
        
        assertEquals(Money.of(80.0), wallet.getBalance());
    }
    
    @Test
    void shouldNotCreditWithNullAmount() {
        Wallet wallet = new Wallet(accountId);
        
        assertThrows(NullPointerException.class, 
            () -> wallet.credit(null, Currency.EUR));
    }
    
    @Test
    void shouldNotCreditWithNullCurrency() {
        Wallet wallet = new Wallet(accountId);
        
        assertThrows(NullPointerException.class, 
            () -> wallet.credit(BigDecimal.valueOf(50.0), null));
    }
    
    @Test
    void shouldNotCreditWithZeroAmount() {
        Wallet wallet = new Wallet(accountId);
        
        assertThrows(WalletException.class, 
            () -> wallet.credit(BigDecimal.ZERO, Currency.EUR));
    }
    
    @Test
    void shouldNotCreditWithNegativeAmount() {
        Wallet wallet = new Wallet(accountId);
        
        assertThrows(WalletException.class, 
            () -> wallet.credit(BigDecimal.valueOf(-10.0), Currency.EUR));
    }
    
    @Test
    void shouldDebitWalletWhenSufficientFunds() throws WalletException {
        Wallet wallet = new Wallet(accountId, Money.of(100.0));
        
        wallet.debit(Money.of(30.0));
        
        assertEquals(Money.of(70.0), wallet.getBalance());
    }
    
    @Test
    void shouldNotDebitWhenInsufficientFunds() {
        Wallet wallet = new Wallet(accountId, Money.of(50.0));
        
        WalletException exception = assertThrows(WalletException.class, 
            () -> wallet.debit(Money.of(100.0)));
        
        assertTrue(exception.getMessage().contains("Solde insuffisant"));
        assertEquals(Money.of(50.0), wallet.getBalance());
    }
    
    @Test
    void shouldNotDebitWithNullAmount() {
        Wallet wallet = new Wallet(accountId, Money.of(100.0));
        
        assertThrows(NullPointerException.class, 
            () -> wallet.debit(null));
    }
    
    @Test
    void shouldCheckSufficientFunds() {
        Wallet wallet = new Wallet(accountId, Money.of(100.0));
        
        assertTrue(wallet.hasSufficientFunds(Money.of(50.0)));
        assertTrue(wallet.hasSufficientFunds(Money.of(100.0)));
        assertFalse(wallet.hasSufficientFunds(Money.of(150.0)));
    }
    
    @Test
    void shouldHaveEqualityBasedOnAccountId() {
        Wallet wallet1 = new Wallet(accountId);
        Wallet wallet2 = new Wallet(accountId);
        Wallet wallet3 = new Wallet(EntityId.generate());
        
        assertEquals(wallet1, wallet2);
        assertNotEquals(wallet1, wallet3);
        assertEquals(wallet1.hashCode(), wallet2.hashCode());
    }
}
