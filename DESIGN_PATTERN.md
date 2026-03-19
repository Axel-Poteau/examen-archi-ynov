# Design Pattern — State Pattern

## Pattern choisi

**State** (pattern comportemental / behavioral)

## Localisation

`reservation-service/src/main/java/com/coworking/reservationservice/state/`

## Justification

Le cycle de vie d'une réservation suit un enchaînement d'états bien défini :

```
CONFIRMED ──→ COMPLETED
    │
    └────────→ CANCELLED
```

Les états `COMPLETED` et `CANCELLED` sont des **états terminaux** : aucune transition n'est possible à partir de ceux-ci. Le comportement des opérations `cancel()` et `complete()` dépend directement de l'état courant de la réservation :

| État courant | `cancel()`        | `complete()`      |
|--------------|--------------------|--------------------|
| CONFIRMED    | → CANCELLED        | → COMPLETED        |
| CANCELLED    | Erreur (déjà annulée) | Erreur (impossible) |
| COMPLETED    | Erreur (impossible)   | Erreur (déjà terminée) |

Sans le pattern State, cette logique serait implémentée avec des blocs `if/else` ou `switch` dans le service, mélangeant la logique métier de chaque état. Le pattern State permet de :

1. **Encapsuler le comportement propre à chaque état** dans une classe dédiée (`ConfirmedState`, `CancelledState`, `CompletedState`), respectant le principe de responsabilité unique (SRP).
2. **Éliminer les conditions complexes** dans le service. L'appel `state.cancel(reservation)` délègue directement à l'implémentation correspondante.
3. **Faciliter l'extension** : ajouter un nouvel état (ex. `PENDING`, `WAITING_LIST`) ne nécessite que la création d'une nouvelle classe implémentant `ReservationState`, sans modifier le code existant (principe Open/Closed).

## Structure

- **`ReservationState`** (interface) : définit le contrat avec les méthodes `cancel()` et `complete()`.
- **`ConfirmedState`** : autorise les transitions vers CANCELLED et COMPLETED.
- **`CancelledState`** : état terminal, toute opération lève une exception.
- **`CompletedState`** : état terminal, toute opération lève une exception.
- **`ReservationStateFactory`** : instancie l'état correspondant au `ReservationStatus` courant (pattern Factory associé).

## Utilisation dans le service

```java
// Dans ReservationService.cancel()
ReservationState state = ReservationStateFactory.getState(reservation.getStatus());
state.cancel(reservation);

// Dans ReservationService.complete()
ReservationState state = ReservationStateFactory.getState(reservation.getStatus());
state.complete(reservation);
```

Le service n'a aucune connaissance des règles de transition : il délègue entièrement cette responsabilité aux classes d'état.
