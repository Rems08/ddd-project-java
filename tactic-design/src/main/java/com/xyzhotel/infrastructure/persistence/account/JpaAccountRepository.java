package com.xyzhotel.infrastructure.persistence.account;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

/**
 * Repository JPA pour les comptes
 */
@Repository
public interface JpaAccountRepository extends JpaRepository<AccountEntity, String> {
    
    Optional<AccountEntity> findByEmail(String email);
    
    boolean existsByEmail(String email);
}
