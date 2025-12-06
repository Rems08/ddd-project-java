package com.xyzhotel.infrastructure.persistence.wallet;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.util.Objects;

/**
 * Entity JPA pour la persistence des portefeuilles
 */
@Entity
@Table(name = "wallets")
public class WalletEntity {
    
    @Id
    @Column(name = "account_id", nullable = false, length = 50)
    private String accountId;
    
    @Column(name = "balance", nullable = false, precision = 19, scale = 2)
    private BigDecimal balance;
    
    public WalletEntity() {
    }
    
    public WalletEntity(String accountId, BigDecimal balance) {
        this.accountId = accountId;
        this.balance = balance;
    }
    
    public String getAccountId() {
        return accountId;
    }
    
    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }
    
    public BigDecimal getBalance() {
        return balance;
    }
    
    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        WalletEntity that = (WalletEntity) o;
        return Objects.equals(accountId, that.accountId);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(accountId);
    }
}
