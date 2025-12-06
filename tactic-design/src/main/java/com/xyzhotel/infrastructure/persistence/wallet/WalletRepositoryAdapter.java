package com.xyzhotel.infrastructure.persistence.wallet;

import com.xyzhotel.domain.shared.EntityId;
import com.xyzhotel.domain.shared.Money;
import com.xyzhotel.domain.wallet.Wallet;
import com.xyzhotel.domain.wallet.WalletRepository;
import org.springframework.stereotype.Component;
import java.util.Optional;

/**
 * Adapter pour la persistence des portefeuilles avec PostgreSQL
 */
@Component
public class WalletRepositoryAdapter implements WalletRepository {
    
    private final JpaWalletRepository jpaRepository;
    
    public WalletRepositoryAdapter(JpaWalletRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }
    
    @Override
    public Wallet save(Wallet wallet) {
        WalletEntity entity = toEntity(wallet);
        jpaRepository.save(entity);
        return wallet;
    }
    
    @Override
    public Optional<Wallet> findByAccountId(EntityId accountId) {
        return jpaRepository.findById(accountId.value())
            .map(this::toDomain);
    }
    
    private WalletEntity toEntity(Wallet wallet) {
        return new WalletEntity(
            wallet.getAccountId().value(),
            wallet.getBalance().amount()
        );
    }
    
    private Wallet toDomain(WalletEntity entity) {
        return new Wallet(
            EntityId.of(entity.getAccountId()),
            Money.of(entity.getBalance())
        );
    }
}
