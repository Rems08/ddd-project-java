#!/bin/bash

# Script pour lancer l'application en mode CLI
# Usage: ./cli.sh [commande]
# Si aucune commande n'est fournie, démarre le mode interactif

if [ $# -eq 0 ]; then
    echo "Démarrage du mode CLI interactif..."
    export 
    SPRING_PROFILES_ACTIVE=cli mvn spring-boot:run -q
else
    echo "Exécution de la commande: $*"
    SPRING_PROFILES_ACTIVE=cli mvn spring-boot:run -Dspring-boot.run.arguments="$*" -q
fi
