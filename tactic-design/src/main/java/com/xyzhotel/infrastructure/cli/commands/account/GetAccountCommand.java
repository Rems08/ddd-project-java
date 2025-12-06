package com.xyzhotel.infrastructure.cli.commands.account;

import com.xyzhotel.application.account.GetAccountUseCase;
import com.xyzhotel.infrastructure.cli.AbstractCommand;
import org.springframework.stereotype.Component;

import java.time.format.DateTimeFormatter;

/**
 * Commande CLI pour afficher les détails d'un compte
 */
@Component
public class GetAccountCommand extends AbstractCommand {
    
    private final GetAccountUseCase getAccountUseCase;
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
    
    public GetAccountCommand(GetAccountUseCase getAccountUseCase) {
        this.getAccountUseCase = getAccountUseCase;
    }
    
    @Override
    public void execute(String[] args) throws Exception {
        validateArguments(args, 1);
        
        String accountId = args[0];
        GetAccountUseCase.AccountDetailsResult result = getAccountUseCase.execute(accountId);
        
        printSeparator();
        System.out.println("DÉTAILS DU COMPTE");
        printSeparator();
        System.out.println("ID:           " + result.accountId());
        System.out.println("Nom complet:  " + result.fullName());
        System.out.println("Email:        " + result.email());
        System.out.println("Téléphone:    " + result.phoneNumber());
        System.out.println("Créé le:      " + result.createdAt().format(DATE_FORMATTER));
        printSeparator();
    }
    
    @Override
    public String getName() {
        return "account:get";
    }
    
    @Override
    public String getDescription() {
        return "Afficher les détails d'un compte";
    }
    
    @Override
    public String getUsage() {
        return "account:get <account_id>";
    }
}
