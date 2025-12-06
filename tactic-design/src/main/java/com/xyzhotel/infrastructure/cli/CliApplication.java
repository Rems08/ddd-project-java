package com.xyzhotel.infrastructure.cli;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import java.util.Scanner;

/**
 * Service CLI qui gère le mode interactif en ligne de commande
 * Activé uniquement si spring.main.web-application-type=none
 */
@Component
@ConditionalOnProperty(name = "spring.main.web-application-type", havingValue = "none")
public class CliApplication implements CommandLineRunner {
    
    private final CommandExecutor commandExecutor;
    private final CommandRegistry commandRegistry;
    
    public CliApplication(CommandExecutor commandExecutor, CommandRegistry commandRegistry) {
        this.commandExecutor = commandExecutor;
        this.commandRegistry = commandRegistry;
    }
    
    @Override
    public void run(String... args) throws Exception {
        printWelcome();
        
        // Si des arguments sont passés, exécuter la commande et quitter
        if (args.length > 0) {
            executeSingleCommand(args);
            return;
        }
        
        // Sinon, démarrer le mode interactif
        runInteractiveMode();
    }
    
    private void printWelcome() {
        System.out.println("╔═══════════════════════════════════════╗");
        System.out.println("║   HOTEL BOOKING SYSTEM - CLI MODE    ║");
        System.out.println("╚═══════════════════════════════════════╝");
        System.out.println();
        System.out.println("Tapez 'help' pour voir toutes les commandes disponibles");
        System.out.println("Tapez 'exit' ou 'quit' pour quitter");
        System.out.println();
    }
    
    private void executeSingleCommand(String[] args) {
        try {
            String commandLine = String.join(" ", args);
            commandExecutor.execute(commandLine);
        } catch (Exception e) {
            System.err.println("Erreur: " + e.getMessage());
            System.exit(1);
        }
    }
    
    private void runInteractiveMode() {
        Scanner scanner = new Scanner(System.in);
        
        while (true) {
            System.out.print("> ");
            String input = scanner.nextLine().trim();
            
            if (input.isEmpty()) {
                continue;
            }
            
            if (input.equalsIgnoreCase("exit") || input.equalsIgnoreCase("quit")) {
                System.out.println("Au revoir!");
                break;
            }
            
            if (input.equalsIgnoreCase("help")) {
                commandRegistry.printHelp();
                continue;
            }
            
            try {
                String commandLine = input;
                commandExecutor.execute(commandLine);
            } catch (Exception e) {
                System.err.println("❌ Erreur: " + e.getMessage());
            }
            
            System.out.println(); // Ligne vide pour la lisibilité
        }
        
        scanner.close();
    }
}
