# Collection Bruno - XYZ Hotel API

Collection de requêtes API pour le système de réservation d'hôtel XYZ.

## Structure

```
xyz-hotel/
├── accounts/          # Gestion des comptes clients
├── wallets/           # Gestion des portefeuilles
├── rooms/             # Informations sur les chambres
├── bookings/          # Gestion des réservations
├── admin/             # Fonctionnalités d'administration
└── environments/      # Variables d'environnement
```

## Variables d'environnement

Les variables suivantes sont disponibles dans `environments/local.bru` :

- `accountId` : ID du compte client (à remplir après création d'un compte)
- `bookingId` : ID de la réservation (à remplir après création d'une réservation)
- `roomId` : ID d'une chambre (défaut: STD-001)

## Workflow typique

### 1. Créer un compte client
**Requête** : `accounts/create-account.bru`
- Copier l'`accountId` retourné et le mettre dans les variables d'environnement

### 2. Créditer le portefeuille
**Requête** : `wallets/credit-wallet.bru`
- Le montant sera converti en euros si nécessaire

### 3. Consulter le solde
**Requête** : `wallets/get-balance.bru`

### 4. Voir les informations des chambres
**Requête** : `rooms/get-rooms-info.bru`
- Affiche les types de chambres disponibles avec leurs prix et équipements

### 5. Créer une réservation
**Requête** : `bookings/create-booking.bru`
- 50% du montant total sera débité automatiquement
- Copier le `bookingId` retourné

### 6. Confirmer la réservation
**Requête** : `bookings/confirm-booking.bru`
- Débite les 50% restants du montant total

### 7. (Optionnel) Annuler une réservation
**Requête** : `bookings/cancel-booking.bru`
- Rembourse le montant déjà payé

## Fonctionnalités Admin

### Statistiques des chambres
**Requête** : `admin/rooms-statistics.bru`
- Affiche le nombre total de chambres, disponibles et occupées

### Historique des réservations par chambre
**Requête** : `admin/room-booking-history.bru`
- Affiche toutes les réservations pour une chambre spécifique

## Types de chambres disponibles

- `STANDARD` : 50€/nuit - Lit 1 place, Wifi, TV
- `SUPERIOR` : 100€/nuit - Lit 2 places, Wifi, TV écran plat, Minibar, Climatiseur
- `SUITE` : 200€/nuit - Lit 2 places, Wifi, TV écran plat, Minibar, Climatiseur, Baignoire, Terrasse

## Devises supportées

- EUR (Euro)
- USD (Dollar américain) - Taux : 1.1
- GBP (Livre sterling) - Taux : 0.85

## Exemples de données

### Créer un compte
```json
{
  "fullName": "Jean Dupont",
  "email": "jean.dupont@example.com",
  "phoneNumber": "+33612345678"
}
```

### Créditer un portefeuille
```json
{
  "amount": 500.00,
  "currency": "EUR"
}
```

### Créer une réservation
```json
{
  "accountId": "ACC-123456",
  "checkInDate": "2025-12-15",
  "numberOfNights": 3,
  "rooms": {
    "STANDARD": 1,
    "SUPERIOR": 1
  }
}
```

## Notes

- L'API écoute sur `http://localhost:8080`
- Tous les montants sont en euros dans la base de données
- Les conversions de devises sont automatiques
- Les réservations débitent 50% à la création et 50% à la confirmation
- L'annulation d'une réservation rembourse le montant déjà payé
