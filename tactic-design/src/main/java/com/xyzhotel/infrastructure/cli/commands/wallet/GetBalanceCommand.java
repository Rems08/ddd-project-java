package com.xyzhotel.infrastructure.cli.commands.wallet;

import com.xyzhotel.application.wallet.GetWalletBalanceUseCase;
import com.xyzhotel.infrastructure.cli.AbstractCommand;
import org.springframework.stereotype.Component;

/**
 * Commande CLI pour consulter le solde d'un portefeuille
 */
@Component
public class GetBalanceCommand extends AbstractCommand {
    
    private final GetWalletBalanceUseCase getWalletBalanceUseCase;
    
    public GetBalanceCommand(GetWalletBalanceUseCase getWalletBalanceUseCase) {
        this.getWalletBalanceUseCase = getWalletBalanceUseCase;
    }
    
    @Override
    public void execute(String[] args) throws Exception {
        validateArguments(args, 1);
        
        String accountId = args[0];
        GetWalletBalanceUseCase.WalletBalanceResult result = getWalletBalanceUseCase.execute(accountId);
        
        printSeparator();
        System.out.println("SOLDE DU PORTEFEUILLE");
        printSeparator();
        System.out.println("Compte: " + result.accountId());
        System.out.println("Solde:  " + formatAmount(result.balance()));
        printSeparator();
    }
    
    @Override
    public String getName() {
        return "wallet:balance";
    }
    
    @Override
    public String getDescription() {
        return "Consulter le solde d'un portefeuille";
    }
    
    @Override
    public String getUsage() {
        return "wallet:balance <account_id>";
    }
}
