package com.xyzhotel.domain.account;

import com.xyzhotel.domain.shared.EntityId;
import com.xyzhotel.domain.wallet.Wallet;
import java.time.LocalDateTime;
import java.util.Objects;

/**
 * Aggregate Root représentant un compte client
 * Un client doit avoir un compte pour réserver
 */
public class Account {
    
    private final EntityId id;
    private final FullName fullName;
    private final Email email;
    private final PhoneNumber phoneNumber;
    private final Wallet wallet;
    private final LocalDateTime createdAt;
    
    private Account(EntityId id, FullName fullName, Email email, PhoneNumber phoneNumber, LocalDateTime createdAt) {
        this.id = Objects.requireNonNull(id, "L'identifiant du compte ne peut pas être null");
        this.fullName = Objects.requireNonNull(fullName, "Le nom complet ne peut pas être null");
        this.email = Objects.requireNonNull(email, "L'email ne peut pas être null");
        this.phoneNumber = Objects.requireNonNull(phoneNumber, "Le numéro de téléphone ne peut pas être null");
        this.wallet = new Wallet(id);
        this.createdAt = Objects.requireNonNull(createdAt, "La date de création ne peut pas être null");
    }
    
    /**
     * Crée un nouveau compte client
     * @param fullName Le nom complet du client
     * @param email L'adresse email du client (doit être unique)
     * @param phoneNumber Le numéro de téléphone du client
     * @return Un nouveau compte avec un identifiant généré
     */
    public static Account create(FullName fullName, Email email, PhoneNumber phoneNumber) {
        EntityId id = EntityId.generate();
        return new Account(id, fullName, email, phoneNumber, LocalDateTime.now());
    }
    
    /**
     * Reconstitue un compte existant depuis la persistence
     */
    public static Account reconstitute(EntityId id, FullName fullName, Email email, PhoneNumber phoneNumber, LocalDateTime createdAt) {
        return new Account(id, fullName, email, phoneNumber, createdAt);
    }
    
    public EntityId getId() {
        return id;
    }
    
    public FullName getFullName() {
        return fullName;
    }
    
    public Email getEmail() {
        return email;
    }
    
    public PhoneNumber getPhoneNumber() {
        return phoneNumber;
    }
    
    public Wallet getWallet() {
        return wallet;
    }
    
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Account account = (Account) o;
        return Objects.equals(id, account.id);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
