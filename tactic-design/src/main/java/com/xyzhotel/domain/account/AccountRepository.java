package com.xyzhotel.domain.account;

import com.xyzhotel.domain.shared.EntityId;
import java.util.List;
import java.util.Optional;

/**
 * Port pour la persistence des comptes clients
 */
public interface AccountRepository {
    
    /**
     * Sauvegarde un compte
     * @param account Le compte à sauvegarder
     * @return Le compte sauvegardé
     */
    Account save(Account account);
    
    /**
     * Trouve un compte par son identifiant
     * @param id L'identifiant du compte
     * @return Le compte s'il existe
     */
    Optional<Account> findById(EntityId id);
    
    /**
     * Trouve un compte par son email
     * @param email L'email du compte
     * @return Le compte s'il existe
     */
    Optional<Account> findByEmail(Email email);
    
    /**
     * Vérifie si un email est déjà utilisé
     * @param email L'email à vérifier
     * @return true si l'email existe déjà
     */
    boolean existsByEmail(Email email);
    
    /**
     * Trouve tous les comptes
     * @return La liste de tous les comptes
     */
    List<Account> findAll();
}
