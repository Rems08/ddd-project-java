package com.xyzhotel.infrastructure.cli;

import org.springframework.stereotype.Component;
import java.util.ArrayList;
import java.util.List;

/**
 * Exécuteur de commandes CLI
 * Suit le pattern Command Handler du DDD
 */
@Component
public class CommandExecutor {
    
    private final CommandRegistry registry;
    
    public CommandExecutor(CommandRegistry registry) {
        this.registry = registry;
    }
    
    /**
     * Exécute une commande à partir d'une ligne de texte
     * @param commandLine La ligne de commande complète
     */
    public void execute(String commandLine) {
        if (commandLine == null || commandLine.trim().isEmpty()) {
            registry.printHelp();
            return;
        }
        
        // Parser la ligne de commande en arguments
        String[] args = parseCommandLine(commandLine.trim());

        if (args.length == 0) {
            registry.printHelp();
            return;
        }
        
        String commandName = args[0];
        
        // Commande help
        if ("help".equalsIgnoreCase(commandName)) {
            if (args.length > 1) {
                registry.printCommandHelp(args[1]);
            } else {
                registry.printHelp();
            }
            return;
        }
        
        // Exécuter la commande
        Command command = registry.getCommand(commandName);
        if (command == null) {
            System.err.println("Erreur: Commande inconnue '" + commandName + "'");
            System.err.println("Utilisez 'help' pour voir les commandes disponibles.");
            return;
        }
        
        try {
            // Extraire les arguments de la commande (sans le nom de la commande)
            String[] commandArgs = new String[args.length - 1];
            System.arraycopy(args, 1, commandArgs, 0, commandArgs.length);
            
            command.execute(commandArgs);
        } catch (Exception e) {
            System.err.println("Erreur lors de l'exécution de la commande: " + e.getMessage());
            if (e.getCause() != null) {
                System.err.println("Cause: " + e.getCause().getMessage());
            }
            System.err.println("\nUsage: " + command.getUsage());
        }
    }

    /**
     * Parse une ligne de commande en arguments.
     * - Sépare sur les espaces
     * - Mais tout ce qui est entre apostrophes '...' est considéré comme un seul argument
     *   ex:  create-customer 'John Doe' john@example.com
     *   -> ["create-customer", "John Doe", "john@example.com"]
     */
    private String[] parseCommandLine(String commandLine) {
        List<String> args = new ArrayList<>();
        StringBuilder current = new StringBuilder();
        boolean inQuotes = false; // true quand on est entre 'xxx'

        for (int i = 0; i < commandLine.length(); i++) {
            char c = commandLine.charAt(i);

            if (c == '\'') {
                // On bascule le mode "dans les quotes" / "hors des quotes"
                inQuotes = !inQuotes;
            } else if (Character.isWhitespace(c) && !inQuotes) {
                // Fin d'argument (seulement si on n'est PAS dans des quotes)
                if (current.length() > 0) {
                    args.add(current.toString());
                    current.setLength(0);
                }
            } else {
                // Caractère normal
                current.append(c);
            }
        }

        // Ajoute le dernier argument si nécessaire
        if (current.length() > 0) {
            args.add(current.toString());
        }

        return args.toArray(new String[0]);
    }

}
