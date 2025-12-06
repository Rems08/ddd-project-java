package com.xyzhotel.infrastructure.persistence.wallet;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repository JPA pour les portefeuilles
 */
@Repository
public interface JpaWalletRepository extends JpaRepository<WalletEntity, String> {
}
