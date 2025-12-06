package com.xyzhotel.domain.booking;

import com.xyzhotel.domain.shared.DomainException;
import com.xyzhotel.domain.shared.EntityId;
import com.xyzhotel.domain.shared.Money;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * Aggregate Root représentant une réservation
 */
public class Booking {
    
    private final EntityId id;
    private final EntityId accountId;
    private final StayPeriod stayPeriod;
    private final List<BookingItem> items;
    private final Money totalAmount;
    private Money paidAmount;
    private BookingStatus status;
    private final LocalDateTime createdAt;
    
    private Booking(EntityId id, EntityId accountId, StayPeriod stayPeriod, 
                   List<BookingItem> items, Money totalAmount, Money paidAmount,
                   BookingStatus status, LocalDateTime createdAt) {
        this.id = Objects.requireNonNull(id, "L'identifiant de la réservation ne peut pas être null");
        this.accountId = Objects.requireNonNull(accountId, "L'identifiant du compte ne peut pas être null");
        this.stayPeriod = Objects.requireNonNull(stayPeriod, "La période de séjour ne peut pas être null");
        this.items = new ArrayList<>(Objects.requireNonNull(items, "Les items de réservation ne peuvent pas être null"));
        this.totalAmount = Objects.requireNonNull(totalAmount, "Le montant total ne peut pas être null");
        this.paidAmount = Objects.requireNonNull(paidAmount, "Le montant payé ne peut pas être null");
        this.status = Objects.requireNonNull(status, "Le statut ne peut pas être null");
        this.createdAt = Objects.requireNonNull(createdAt, "La date de création ne peut pas être null");
        
        if (items.isEmpty()) {
            throw new IllegalArgumentException("La réservation doit contenir au moins une chambre");
        }
    }
    
    /**
     * Crée une nouvelle réservation
     * Le client paie 50% à la création
     * @param accountId L'identifiant du compte client
     * @param stayPeriod La période de séjour
     * @param items Les chambres réservées
     * @return Une nouvelle réservation
     */
    public static Booking create(EntityId accountId, StayPeriod stayPeriod, List<BookingItem> items) {
        EntityId id = EntityId.generate();
        
        // Calculer le montant total
        Money totalAmount = Money.zero();
        for (BookingItem item : items) {
            totalAmount = totalAmount.add(item.calculateTotalPrice(stayPeriod.numberOfNights()));
        }
        
        // 50% du montant total à payer à la création
        Money depositAmount = totalAmount.multiplyByPercentage(50);
        
        return new Booking(
            id,
            accountId,
            stayPeriod,
            items,
            totalAmount,
            depositAmount,
            BookingStatus.PENDING,
            LocalDateTime.now()
        );
    }
    
    /**
     * Reconstitue une réservation existante depuis la persistence
     */
    public static Booking reconstitute(EntityId id, EntityId accountId, StayPeriod stayPeriod,
                                      List<BookingItem> items, Money totalAmount, Money paidAmount,
                                      BookingStatus status, LocalDateTime createdAt) {
        return new Booking(id, accountId, stayPeriod, items, totalAmount, paidAmount, status, createdAt);
    }
    
    /**
     * Confirme la réservation en payant les 50% restants
     * @throws BookingException Si la réservation n'est pas en attente ou si déjà confirmée
     */
    public Money confirm() throws BookingException {
        if (status == BookingStatus.CONFIRMED) {
            throw new BookingException("La réservation est déjà confirmée");
        }
        if (status == BookingStatus.CANCELLED) {
            throw new BookingException("La réservation a été annulée, elle ne peut pas être confirmée");
        }
        
        try {
            Money remainingAmount = totalAmount.subtract(paidAmount);
            this.paidAmount = totalAmount;
            this.status = BookingStatus.CONFIRMED;
            
            return remainingAmount;
        } catch (DomainException e) {
            throw new BookingException("Erreur lors de la confirmation: " + e.getMessage());
        }
    }
    
    /**
     * Annule la réservation
     * Pas de remboursement
     * @throws BookingException Si la réservation est déjà annulée
     */
    public void cancel() throws BookingException {
        if (status == BookingStatus.CANCELLED) {
            throw new BookingException("La réservation est déjà annulée");
        }
        
        this.status = BookingStatus.CANCELLED;
    }
    
    /**
     * Calcule le montant du dépôt initial (50%)
     * @return Le montant du dépôt
     */
    public Money getDepositAmount() {
        return totalAmount.multiplyByPercentage(50);
    }
    
    /**
     * Calcule le montant restant à payer
     * @return Le montant restant
     */
    public Money getRemainingAmount() throws BookingException {
        try {
            return totalAmount.subtract(paidAmount);
        } catch (DomainException e) {
            throw new BookingException("Erreur lors du calcul du montant restant: " + e.getMessage());
        }
    }
    
    public EntityId getId() {
        return id;
    }
    
    public EntityId getAccountId() {
        return accountId;
    }
    
    public StayPeriod getStayPeriod() {
        return stayPeriod;
    }
    
    public List<BookingItem> getItems() {
        return Collections.unmodifiableList(items);
    }
    
    public Money getTotalAmount() {
        return totalAmount;
    }
    
    public Money getPaidAmount() {
        return paidAmount;
    }
    
    public BookingStatus getStatus() {
        return status;
    }
    
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Booking booking = (Booking) o;
        return Objects.equals(id, booking.id);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
