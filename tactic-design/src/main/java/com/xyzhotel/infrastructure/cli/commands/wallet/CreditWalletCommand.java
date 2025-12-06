package com.xyzhotel.infrastructure.cli.commands.wallet;

import com.xyzhotel.application.wallet.CreditWalletUseCase;
import com.xyzhotel.infrastructure.cli.AbstractCommand;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

/**
 * Commande CLI pour créditer un portefeuille
 */
@Component
public class CreditWalletCommand extends AbstractCommand {
    
    private final CreditWalletUseCase creditWalletUseCase;
    
    public CreditWalletCommand(CreditWalletUseCase creditWalletUseCase) {
        this.creditWalletUseCase = creditWalletUseCase;
    }
    
    @Override
    public void execute(String[] args) throws Exception {
        validateArguments(args, 3);
        
        String accountId = args[0];
        BigDecimal amount = new BigDecimal(args[1]);
        String currency = args[2].toUpperCase();
        
        CreditWalletUseCase.CreditWalletCommand command = 
            new CreditWalletUseCase.CreditWalletCommand(accountId, amount, currency);
        
        CreditWalletUseCase.CreditWalletResult result = creditWalletUseCase.execute(command);
        
        printSuccess("Portefeuille crédité avec succès!");
        printSeparator();
        System.out.println("Compte:             " + result.accountId());
        System.out.println("Montant crédité:    " + formatAmount(result.creditedAmountInEuros()));
        System.out.println("Nouveau solde:      " + formatAmount(result.newBalance()));
        printSeparator();
    }
    
    @Override
    public String getName() {
        return "wallet:credit";
    }
    
    @Override
    public String getDescription() {
        return "Créditer un portefeuille";
    }
    
    @Override
    public String getUsage() {
        return "wallet:credit <account_id> <montant> <devise>\n" +
               "       Devises supportées: EUR, USD, GBP";
    }
}
