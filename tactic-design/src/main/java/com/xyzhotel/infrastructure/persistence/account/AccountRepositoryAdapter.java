package com.xyzhotel.infrastructure.persistence.account;

import com.xyzhotel.domain.account.*;
import com.xyzhotel.domain.shared.EntityId;
import org.springframework.stereotype.Component;
import java.util.List;
import java.util.Optional;

/**
 * Adapter pour la persistence des comptes avec PostgreSQL
 */
@Component
public class AccountRepositoryAdapter implements AccountRepository {
    
    private final JpaAccountRepository jpaRepository;
    
    public AccountRepositoryAdapter(JpaAccountRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }
    
    @Override
    public Account save(Account account) {
        AccountEntity entity = toEntity(account);
        jpaRepository.save(entity);
        return account;
    }
    
    @Override
    public Optional<Account> findById(EntityId id) {
        return jpaRepository.findById(id.value())
            .map(this::toDomain);
    }
    
    @Override
    public Optional<Account> findByEmail(Email email) {
        return jpaRepository.findByEmail(email.value())
            .map(this::toDomain);
    }
    
    @Override
    public boolean existsByEmail(Email email) {
        return jpaRepository.existsByEmail(email.value());
    }
    
    @Override
    public List<Account> findAll() {
        return jpaRepository.findAll().stream()
            .map(this::toDomain)
            .toList();
    }
    
    private AccountEntity toEntity(Account account) {
        return new AccountEntity(
            account.getId().value(),
            account.getFullName().value(),
            account.getEmail().value(),
            account.getPhoneNumber().value(),
            account.getCreatedAt()
        );
    }
    
    private Account toDomain(AccountEntity entity) {
        return Account.reconstitute(
            EntityId.of(entity.getId()),
            FullName.of(entity.getFullName()),
            Email.of(entity.getEmail()),
            PhoneNumber.of(entity.getPhoneNumber()),
            entity.getCreatedAt()
        );
    }
}
