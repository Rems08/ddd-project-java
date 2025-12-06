#!/bin/bash

# Script de test du CLI - Scénario complet
# Ce script exécute une série de commandes pour tester toutes les fonctionnalités

echo "========================================="
echo "  TEST CLI - Scénario Complet"
echo "========================================="
echo ""

# Fonction pour exécuter une commande CLI et afficher le résultat
run_cli() {
    echo ""
    echo "🔵 Commande: $1"
    echo "-----------------------------------------"
    SPRING_PROFILES_ACTIVE=cli mvn spring-boot:run -Dspring-boot.run.arguments="$1" -q 2>&1 | grep -v "WARNING"
    echo ""
}

echo "1️⃣  Lister les chambres disponibles"
run_cli "room:list"

echo "2️⃣  Créer un nouveau compte"
run_cli "account:create Alice Martin alice.martin@example.com +33698765432"

echo "3️⃣  Lister tous les comptes"
run_cli "account:list"

echo "📝 Note: Copiez l'ID du compte créé ci-dessus pour les prochaines commandes"
echo "Appuyez sur Entrée pour continuer..."
read

echo "4️⃣  Créditer le portefeuille (remplacez ACCOUNT_ID)"
echo "Entrez l'ID du compte:"
read ACCOUNT_ID
run_cli "wallet:credit $ACCOUNT_ID 1000"

echo "5️⃣  Vérifier le solde"
run_cli "wallet:balance $ACCOUNT_ID"

echo "6️⃣  Afficher les détails du compte"
run_cli "account:get $ACCOUNT_ID"

echo "7️⃣  Créer une réservation"
run_cli "booking:create $ACCOUNT_ID 2025-12-20 3 STANDARD:1"

echo "📝 Note: Copiez l'ID de la réservation ci-dessus"
echo "Entrez l'ID de la réservation:"
read BOOKING_ID

echo "8️⃣  Afficher les détails de la réservation"
run_cli "booking:get $BOOKING_ID"

echo "9️⃣  Lister les réservations du compte"
run_cli "booking:list $ACCOUNT_ID"

echo "🔟 Confirmer la réservation"
run_cli "booking:confirm $BOOKING_ID"

echo "1️⃣1️⃣  Vérifier le solde après confirmation"
run_cli "wallet:balance $ACCOUNT_ID"

echo "1️⃣2️⃣  Statistiques des chambres (admin)"
run_cli "admin:stats"

echo ""
echo "========================================="
echo "  ✅ Tests terminés!"
echo "========================================="
