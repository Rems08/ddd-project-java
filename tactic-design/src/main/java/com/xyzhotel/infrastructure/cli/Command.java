package com.xyzhotel.infrastructure.cli;

/**
 * Interface de base pour toutes les commandes CLI
 * Suit le pattern Command du DDD
 */
public interface Command {
    
    /**
     * Exécute la commande
     * @param args Les arguments de la commande
     * @throws Exception Si une erreur survient lors de l'exécution
     */
    void execute(String[] args) throws Exception;
    
    /**
     * Retourne le nom de la commande
     * @return Le nom de la commande
     */
    String getName();
    
    /**
     * Retourne la description de la commande
     * @return La description de la commande
     */
    String getDescription();
    
    /**
     * Retourne l'usage de la commande
     * @return L'usage de la commande
     */
    String getUsage();
}
