package com.xyzhotel.application.account;

import com.xyzhotel.domain.account.Account;
import com.xyzhotel.domain.account.AccountRepository;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Use case pour récupérer tous les comptes clients
 */
public class GetAllAccountsUseCase {
    
    private final AccountRepository accountRepository;
    
    public GetAllAccountsUseCase(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }
    
    /**
     * Récupère la liste de tous les comptes
     * @return La liste des comptes
     */
    public List<AccountSummaryResult> execute() {
        List<Account> accounts = accountRepository.findAll();
        
        return accounts.stream()
            .map(account -> new AccountSummaryResult(
                account.getId().value(),
                account.getFullName().value(),
                account.getEmail().value(),
                account.getPhoneNumber().value(),
                account.getCreatedAt()
            ))
            .toList();
    }
    
    public record AccountSummaryResult(
        String accountId,
        String fullName,
        String email,
        String phoneNumber,
        LocalDateTime createdAt
    ) {}
}
