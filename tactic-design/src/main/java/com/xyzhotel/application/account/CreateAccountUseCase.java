package com.xyzhotel.application.account;

import com.xyzhotel.domain.account.*;
import com.xyzhotel.domain.shared.EntityId;
import com.xyzhotel.domain.wallet.Wallet;
import com.xyzhotel.domain.wallet.WalletRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Cas d'usage : Créer un compte client
 */
@Service
public class CreateAccountUseCase {
    
    private final AccountRepository accountRepository;
    private final WalletRepository walletRepository;
    
    public CreateAccountUseCase(AccountRepository accountRepository, WalletRepository walletRepository) {
        this.accountRepository = accountRepository;
        this.walletRepository = walletRepository;
    }
    
    @Transactional
    public CreateAccountResult execute(CreateAccountCommand command) throws AccountException {
        // Valider que l'email n'est pas déjà utilisé
        Email email = Email.of(command.email());
        if (accountRepository.existsByEmail(email)) {
            throw new AccountException("Cet email est déjà utilisé: " + command.email());
        }
        
        // Créer le compte
        FullName fullName = FullName.of(command.fullName());
        PhoneNumber phoneNumber = PhoneNumber.of(command.phoneNumber());
        
        Account account = Account.create(fullName, email, phoneNumber);
        
        // Sauvegarder le compte
        account = accountRepository.save(account);
        
        // Créer et sauvegarder le portefeuille initial
        Wallet wallet = new Wallet(account.getId());
        walletRepository.save(wallet);
        
        return new CreateAccountResult(
            account.getId().value(),
            account.getFullName().value(),
            account.getEmail().value(),
            account.getPhoneNumber().value()
        );
    }
    
    public record CreateAccountCommand(
        String fullName,
        String email,
        String phoneNumber
    ) {}
    
    public record CreateAccountResult(
        String accountId,
        String fullName,
        String email,
        String phoneNumber
    ) {}
}
