package com.xyzhotel.application.wallet;

import com.xyzhotel.domain.account.Account;
import com.xyzhotel.domain.account.AccountException;
import com.xyzhotel.domain.account.AccountRepository;
import com.xyzhotel.domain.shared.EntityId;
import com.xyzhotel.domain.wallet.Currency;
import com.xyzhotel.domain.wallet.Wallet;
import com.xyzhotel.domain.wallet.WalletException;
import com.xyzhotel.domain.wallet.WalletRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;

/**
 * Cas d'usage : Alimenter le portefeuille d'un client
 */
@Service
public class CreditWalletUseCase {
    
    private final AccountRepository accountRepository;
    private final WalletRepository walletRepository;
    
    public CreditWalletUseCase(AccountRepository accountRepository, WalletRepository walletRepository) {
        this.accountRepository = accountRepository;
        this.walletRepository = walletRepository;
    }
    
    @Transactional
    public CreditWalletResult execute(CreditWalletCommand command) throws AccountException, WalletException {
        // Vérifier que le compte existe
        EntityId accountId = EntityId.of(command.accountId());
        Account account = accountRepository.findById(accountId)
            .orElseThrow(() -> new AccountException("Compte non trouvé: " + command.accountId()));
        
        // Récupérer ou créer le portefeuille
        Wallet wallet = walletRepository.findByAccountId(accountId)
            .orElse(new Wallet(accountId));
        
        // Alimenter le portefeuille
        Currency currency = Currency.valueOf(command.currency());
        wallet.credit(command.amount(), currency);
        
        // Sauvegarder
        walletRepository.save(wallet);
        
        return new CreditWalletResult(
            wallet.getAccountId().value(),
            wallet.getBalance().amount(),
            currency.convertToEuro(command.amount())
        );
    }
    
    public record CreditWalletCommand(
        String accountId,
        BigDecimal amount,
        String currency
    ) {}
    
    public record CreditWalletResult(
        String accountId,
        BigDecimal newBalance,
        BigDecimal creditedAmountInEuros
    ) {}
}
