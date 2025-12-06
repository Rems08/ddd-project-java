# XYZ Hotel - Système de Gestion de Réservations

## 🚀 Démarrage rapide
### Requierments
- Maven(cli) > 3.9.9
- Java(cli) == 21


### Mode Web (API REST)
```bash
# Démarrer PostgreSQL et l'API
docker-compose up -d

# API disponible sur http://localhost:8080
```
Vous pouvez tester l'intégralité des routes de l'API via la collection bruno.

Elle se trouve dans le dossier appelé xyz-hotel à la racine du projet

### Mode CLI (Command Line)
```bash
# Mode interactif
./cli.sh

# Commande unique
./cli.sh account:list

# Aide
./cli.sh help

# J'ai crée pour vous un script qui va tester toutes les fonctionalitées du cli
./test-cli.sh 
```

### Run unit tests
```bash
mvn test
```

### Rémy MASSIET ESGI 5 AL
