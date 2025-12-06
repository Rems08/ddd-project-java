package com.xyzhotel.infrastructure.cli.commands.account;

import com.xyzhotel.application.account.GetAllAccountsUseCase;
import com.xyzhotel.infrastructure.cli.AbstractCommand;
import org.springframework.stereotype.Component;

import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * Commande CLI pour lister tous les comptes
 */
@Component
public class ListAccountsCommand extends AbstractCommand {
    
    private final GetAllAccountsUseCase getAllAccountsUseCase;
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    
    public ListAccountsCommand(GetAllAccountsUseCase getAllAccountsUseCase) {
        this.getAllAccountsUseCase = getAllAccountsUseCase;
    }
    
    @Override
    public void execute(String[] args) throws Exception {
        List<GetAllAccountsUseCase.AccountSummaryResult> results = getAllAccountsUseCase.execute();
        
        if (results.isEmpty()) {
            printInfo("Aucun compte trouvé.");
            return;
        }
        
        printSeparator();
        System.out.println("LISTE DES COMPTES (" + results.size() + " compte(s))");
        printSeparator();
        System.out.printf("%-40s %-25s %-30s %-15s%n", "ID", "Nom", "Email", "Créé le");
        printSeparator();
        
        for (GetAllAccountsUseCase.AccountSummaryResult result : results) {
            System.out.printf("%-40s %-25s %-30s %-15s%n",
                result.accountId(),
                truncate(result.fullName(), 25),
                truncate(result.email(), 30),
                result.createdAt().format(DATE_FORMATTER)
            );
        }
        printSeparator();
    }
    
    private String truncate(String str, int maxLength) {
        if (str.length() <= maxLength) {
            return str;
        }
        return str.substring(0, maxLength - 3) + "...";
    }
    
    @Override
    public String getName() {
        return "account:list";
    }
    
    @Override
    public String getDescription() {
        return "Lister tous les comptes";
    }
    
    @Override
    public String getUsage() {
        return "account:list";
    }
}
