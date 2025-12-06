package com.xyzhotel.domain.wallet;

import com.xyzhotel.domain.shared.EntityId;
import com.xyzhotel.domain.shared.Money;
import java.math.BigDecimal;
import java.util.Objects;

/**
 * Entity représentant le portefeuille électronique d'un client
 * Le solde est toujours en euros
 */
public class Wallet {
    
    private final EntityId accountId;
    private Money balance;
    
    public Wallet(EntityId accountId) {
        this.accountId = Objects.requireNonNull(accountId, "L'identifiant du compte ne peut pas être null");
        this.balance = Money.zero();
    }
    
    public Wallet(EntityId accountId, Money balance) {
        this.accountId = Objects.requireNonNull(accountId, "L'identifiant du compte ne peut pas être null");
        this.balance = Objects.requireNonNull(balance, "Le solde ne peut pas être null");
    }
    
    /**
     * Alimente le portefeuille avec un montant dans une devise donnée
     * Le montant est automatiquement converti en euros
     * @param amount Le montant à ajouter
     * @param currency La devise du montant
     * @throws WalletException Si le montant est négatif ou nul
     */
    public void credit(BigDecimal amount, Currency currency) throws WalletException {
        Objects.requireNonNull(amount, "Le montant ne peut pas être null");
        Objects.requireNonNull(currency, "La devise ne peut pas être null");
        
        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new WalletException("Le montant à créditer doit être positif");
        }
        
        BigDecimal amountInEuros = currency.convertToEuro(amount);
        this.balance = this.balance.add(Money.of(amountInEuros));
    }
    
    /**
     * Débite le portefeuille d'un montant
     * @param amount Le montant à débiter (en euros)
     * @throws WalletException Si le solde est insuffisant
     */
    public void debit(Money amount) throws WalletException {
        Objects.requireNonNull(amount, "Le montant ne peut pas être null");
        
        if (this.balance.isLessThan(amount)) {
            throw new WalletException(
                String.format("Solde insuffisant. Solde actuel: %.2f €, Montant demandé: %.2f €",
                    this.balance.amount().doubleValue(),
                    amount.amount().doubleValue())
            );
        }
        
        try {
            this.balance = this.balance.subtract(amount);
        } catch (Exception e) {
            throw new WalletException("Erreur lors du débit du portefeuille: " + e.getMessage());
        }
    }
    
    /**
     * Vérifie si le portefeuille a suffisamment de fonds
     * @param amount Le montant à vérifier
     * @return true si le solde est suffisant
     */
    public boolean hasSufficientFunds(Money amount) {
        return this.balance.isGreaterThanOrEqual(amount);
    }
    
    public EntityId getAccountId() {
        return accountId;
    }
    
    public Money getBalance() {
        return balance;
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Wallet wallet = (Wallet) o;
        return Objects.equals(accountId, wallet.accountId);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(accountId);
    }
}
