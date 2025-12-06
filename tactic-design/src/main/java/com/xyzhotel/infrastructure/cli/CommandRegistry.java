package com.xyzhotel.infrastructure.cli;

import java.util.HashMap;
import java.util.Map;

/**
 * Registry pour gérer toutes les commandes CLI disponibles
 * Pattern Registry du DDD
 */
public class CommandRegistry {
    
    private final Map<String, Command> commands = new HashMap<>();
    
    /**
     * Enregistre une nouvelle commande
     * @param command La commande à enregistrer
     */
    public void register(Command command) {
        commands.put(command.getName().toLowerCase(), command);
    }
    
    /**
     * Récupère une commande par son nom
     * @param name Le nom de la commande
     * @return La commande si elle existe
     */
    public Command getCommand(String name) {
        return commands.get(name.toLowerCase());
    }
    
    /**
     * Vérifie si une commande existe
     * @param name Le nom de la commande
     * @return true si la commande existe
     */
    public boolean hasCommand(String name) {
        return commands.containsKey(name.toLowerCase());
    }
    
    /**
     * Affiche toutes les commandes disponibles
     */
    public void printHelp() {
        System.out.println("\n=== XYZ Hotel - Système de Réservation ===\n");
        System.out.println("Commandes disponibles:\n");
        
        commands.values().stream()
            .sorted((a, b) -> a.getName().compareTo(b.getName()))
            .forEach(cmd -> {
                System.out.printf("  %-25s %s%n", cmd.getName(), cmd.getDescription());
            });
        
        System.out.println("\nUtilisez 'help <commande>' pour plus de détails sur une commande.");
    }
    
    /**
     * Affiche l'aide pour une commande spécifique
     * @param commandName Le nom de la commande
     */
    public void printCommandHelp(String commandName) {
        Command command = getCommand(commandName);
        if (command != null) {
            System.out.println("\n" + command.getName());
            System.out.println(command.getDescription());
            System.out.println("\nUsage:");
            System.out.println("  " + command.getUsage());
        } else {
            System.out.println("Commande inconnue: " + commandName);
        }
    }
}
