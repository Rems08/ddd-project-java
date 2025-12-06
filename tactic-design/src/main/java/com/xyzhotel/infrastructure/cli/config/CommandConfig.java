package com.xyzhotel.infrastructure.cli.config;

import com.xyzhotel.infrastructure.cli.Command;
import com.xyzhotel.infrastructure.cli.CommandRegistry;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

/**
 * Configuration pour enregistrer toutes les commandes CLI
 */
@Configuration
public class CommandConfig {
    
    /**
     * Enregistre automatiquement toutes les commandes Spring dans le registry
     * Toutes les classes annotées avec @Component qui implémentent Command
     * seront automatiquement injectées dans la liste
     */
    @Bean
    public CommandRegistry commandRegistry(List<Command> commands) {
        CommandRegistry registry = new CommandRegistry();
        
        // Enregistrer toutes les commandes automatiquement
        for (Command command : commands) {
            registry.register(command);
        }
        
        return registry;
    }
}
