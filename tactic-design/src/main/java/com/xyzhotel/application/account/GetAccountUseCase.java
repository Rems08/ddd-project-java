package com.xyzhotel.application.account;

import com.xyzhotel.domain.account.Account;
import com.xyzhotel.domain.account.AccountException;
import com.xyzhotel.domain.account.AccountRepository;
import com.xyzhotel.domain.shared.EntityId;

import java.time.LocalDateTime;

/**
 * Use case pour récupérer les détails d'un compte client
 */
public class GetAccountUseCase {
    
    private final AccountRepository accountRepository;
    
    public GetAccountUseCase(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }
    
    /**
     * Récupère les détails d'un compte par son identifiant
     * @param accountId L'identifiant du compte
     * @return Les détails du compte
     * @throws AccountException Si le compte n'existe pas
     */
    public AccountDetailsResult execute(String accountId) throws AccountException {
        EntityId id = new EntityId(accountId);
        
        Account account = accountRepository.findById(id)
            .orElseThrow(() -> new AccountException("Compte non trouvé avec l'identifiant: " + accountId));
        
        return new AccountDetailsResult(
            account.getId().value(),
            account.getFullName().value(),
            account.getEmail().value(),
            account.getPhoneNumber().value(),
            account.getCreatedAt()
        );
    }
    
    public record AccountDetailsResult(
        String accountId,
        String fullName,
        String email,
        String phoneNumber,
        LocalDateTime createdAt
    ) {}
}
