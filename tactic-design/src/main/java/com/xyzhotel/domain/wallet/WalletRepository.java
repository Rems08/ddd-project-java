package com.xyzhotel.domain.wallet;

import com.xyzhotel.domain.shared.EntityId;
import java.util.Optional;

/**
 * Port pour la persistence des portefeuilles
 */
public interface WalletRepository {
    
    /**
     * Sauvegarde un portefeuille
     * @param wallet Le portefeuille à sauvegarder
     * @return Le portefeuille sauvegardé
     */
    Wallet save(Wallet wallet);
    
    /**
     * Trouve un portefeuille par l'identifiant du compte
     * @param accountId L'identifiant du compte
     * @return Le portefeuille s'il existe
     */
    Optional<Wallet> findByAccountId(EntityId accountId);
}
