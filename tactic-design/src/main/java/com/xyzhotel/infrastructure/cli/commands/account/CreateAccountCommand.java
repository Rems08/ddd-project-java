package com.xyzhotel.infrastructure.cli.commands.account;

import com.xyzhotel.application.account.CreateAccountUseCase;
import com.xyzhotel.infrastructure.cli.AbstractCommand;
import org.springframework.stereotype.Component;

/**
 * Commande CLI pour créer un nouveau compte client
 */
@Component
public class CreateAccountCommand extends AbstractCommand {
    
    private final CreateAccountUseCase createAccountUseCase;
    
    public CreateAccountCommand(CreateAccountUseCase createAccountUseCase) {
        this.createAccountUseCase = createAccountUseCase;
    }
    
    @Override
    public void execute(String[] args) throws Exception {
        validateArguments(args, 3);
        
        String fullName = args[0];
        String email = args[1];
        String phoneNumber = args[2];
        
        CreateAccountUseCase.CreateAccountCommand command = 
            new CreateAccountUseCase.CreateAccountCommand(fullName, email, phoneNumber);
        
        CreateAccountUseCase.CreateAccountResult result = createAccountUseCase.execute(command);
        
        printSuccess("Compte créé avec succès!");
        printSeparator();
        System.out.println("ID du compte: " + result.accountId());
        System.out.println("Nom complet:  " + result.fullName());
        System.out.println("Email:        " + result.email());
        System.out.println("Téléphone:    " + result.phoneNumber());
        printSeparator();
        printInfo("Conservez cet ID pour vos futures opérations.");
    }
    
    @Override
    public String getName() {
        return "account:create";
    }
    
    @Override
    public String getDescription() {
        return "Créer un nouveau compte client";
    }
    
    @Override
    public String getUsage() {
        return "account:create <nom_complet> <email> <telephone>";
    }
}
