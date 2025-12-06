package com.xyzhotel.infrastructure.cli;

/**
 * Classe de base abstraite pour les commandes CLI
 * Fournit des utilitaires communs
 */
public abstract class AbstractCommand implements Command {
    
    /**
     * Valide le nombre d'arguments minimum requis
     * @param args Les arguments fournis
     * @param minArgs Le nombre minimum d'arguments requis
     * @throws IllegalArgumentException Si le nombre d'arguments est insuffisant
     */
    protected void validateArguments(String[] args, int minArgs) {
        if (args.length < minArgs) {
            throw new IllegalArgumentException(
                String.format("Nombre d'arguments insuffisant. Attendu: %d, Reçu: %d", minArgs, args.length)
            );
        }
    }
    
    /**
     * Affiche un message de succès
     * @param message Le message à afficher
     */
    protected void printSuccess(String message) {
        System.out.println("✓ " + message);
    }
    
    /**
     * Affiche un message d'erreur
     * @param message Le message à afficher
     */
    protected void printError(String message) {
        System.err.println("✗ " + message);
    }
    
    /**
     * Affiche un message d'information
     * @param message Le message à afficher
     */
    protected void printInfo(String message) {
        System.out.println("ℹ " + message);
    }
    
    /**
     * Affiche une ligne de séparation
     */
    protected void printSeparator() {
        System.out.println("─".repeat(80));
    }
    
    /**
     * Formate un montant en euros
     * @param amount Le montant
     * @return Le montant formaté
     */
    protected String formatAmount(java.math.BigDecimal amount) {
        return String.format("%.2f €", amount);
    }
}
