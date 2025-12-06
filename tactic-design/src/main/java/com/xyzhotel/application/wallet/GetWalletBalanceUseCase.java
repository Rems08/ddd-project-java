package com.xyzhotel.application.wallet;

import com.xyzhotel.domain.account.Account;
import com.xyzhotel.domain.account.AccountException;
import com.xyzhotel.domain.account.AccountRepository;
import com.xyzhotel.domain.shared.EntityId;
import com.xyzhotel.domain.wallet.Wallet;
import com.xyzhotel.domain.wallet.WalletRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;

/**
 * Cas d'usage : Consulter le solde du portefeuille
 */
@Service
public class GetWalletBalanceUseCase {
    
    private final AccountRepository accountRepository;
    private final WalletRepository walletRepository;
    
    public GetWalletBalanceUseCase(AccountRepository accountRepository, WalletRepository walletRepository) {
        this.accountRepository = accountRepository;
        this.walletRepository = walletRepository;
    }
    
    @Transactional(readOnly = true)
    public WalletBalanceResult execute(String accountId) throws AccountException {
        EntityId id = EntityId.of(accountId);
        Account account = accountRepository.findById(id)
            .orElseThrow(() -> new AccountException("Compte non trouvé: " + accountId));
        
        // Récupérer le portefeuille depuis la base de données
        Wallet wallet = walletRepository.findByAccountId(id)
            .orElse(new Wallet(id));
        
        return new WalletBalanceResult(
            wallet.getAccountId().value(),
            wallet.getBalance().amount()
        );
    }
    
    public record WalletBalanceResult(
        String accountId,
        BigDecimal balance
    ) {}
}
