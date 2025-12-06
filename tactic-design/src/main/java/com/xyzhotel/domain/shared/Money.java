package com.xyzhotel.domain.shared;

import java.math.BigDecimal;
import java.util.Objects;

/**
 * Value Object représentant un montant en euros
 */
public record Money(BigDecimal amount) implements ValueObject {
    
    public Money {
        Objects.requireNonNull(amount, "Le montant ne peut pas être null");
        if (amount.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Le montant ne peut pas être négatif");
        }
    }
    
    public static Money zero() {
        return new Money(BigDecimal.ZERO);
    }
    
    public static Money of(BigDecimal amount) {
        return new Money(amount);
    }
    
    public static Money of(double amount) {
        return new Money(BigDecimal.valueOf(amount));
    }
    
    public Money add(Money other) {
        return new Money(this.amount.add(other.amount));
    }
    
    public Money subtract(Money other) throws DomainException {
        BigDecimal result = this.amount.subtract(other.amount);
        if (result.compareTo(BigDecimal.ZERO) < 0) {
            throw new DomainException("Le résultat de la soustraction ne peut pas être négatif");
        }
        return new Money(result);
    }
    
    public Money multiply(int multiplier) {
        return new Money(this.amount.multiply(BigDecimal.valueOf(multiplier)));
    }
    
    public Money multiplyByPercentage(int percentage) {
        return new Money(this.amount.multiply(BigDecimal.valueOf(percentage)).divide(BigDecimal.valueOf(100)));
    }
    
    public boolean isGreaterThan(Money other) {
        return this.amount.compareTo(other.amount) > 0;
    }
    
    public boolean isGreaterThanOrEqual(Money other) {
        return this.amount.compareTo(other.amount) >= 0;
    }
    
    public boolean isLessThan(Money other) {
        return this.amount.compareTo(other.amount) < 0;
    }
}
