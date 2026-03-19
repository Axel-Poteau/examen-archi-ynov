# Plateforme de Réservation de Salles de Coworking

Architecture microservices pour la gestion de salles de coworking, développée avec Spring Boot, Spring Cloud et Apache Kafka.

## Architecture

```
                        API Gateway (:8080)
                            │
              ┌─────────────┼──────────────┐
              │             │              │
       Room Service   Member Service  Reservation Service
         (:8081)        (:8082)          (:8083)
              │             │              │
              └─────────────┼──────────────┘
                            │
                     Apache Kafka (:9092)
```

**Infrastructure :**
- **Config Server** (:8888) — configuration centralisée
- **Discovery Server** (:8761) — Eureka (registre de services)
- **API Gateway** (:8080) — point d'entrée unique

## Prérequis

- Java 21
- Maven 3.8+
- Apache Kafka (avec Zookeeper)

## Lancement

### 1. Démarrer Kafka

```bash
# Démarrer Zookeeper
bin/zookeeper-server-start.sh config/zookeeper.properties

# Démarrer Kafka
bin/kafka-server-start.sh config/server.properties
```

### 2. Démarrer les services (dans cet ordre)

```bash
# 1. Config Server
cd config-server
mvn spring-boot:run

# 2. Discovery Server (Eureka)
cd discovery-server
mvn spring-boot:run

# 3. API Gateway
cd api-gateway
mvn spring-boot:run

# 4. Microservices métier (dans n'importe quel ordre)
cd room-service
mvn spring-boot:run

cd member-service
mvn spring-boot:run

cd reservation-service
mvn spring-boot:run
```

## Endpoints principaux

Tous les services sont accessibles via l'API Gateway sur `http://localhost:8080` :

| Service | Endpoint | Description |
|---------|----------|-------------|
| Room | `GET /api/rooms` | Lister les salles |
| Room | `POST /api/rooms` | Créer une salle |
| Room | `GET /api/rooms/available` | Salles disponibles |
| Member | `GET /api/members` | Lister les membres |
| Member | `POST /api/members` | Créer un membre |
| Reservation | `GET /api/reservations` | Lister les réservations |
| Reservation | `POST /api/reservations` | Créer une réservation |

## Documentation Swagger

Chaque microservice expose sa documentation Swagger UI :

- Room Service : http://localhost:8081/swagger-ui.html
- Member Service : http://localhost:8082/swagger-ui.html
- Reservation Service : http://localhost:8083/swagger-ui.html

## Kafka — Evénements asynchrones

| Evénement | Producteur | Consommateur | Action |
|-----------|------------|--------------|--------|
| Suppression d'une salle | Room Service | Reservation Service | Annule les réservations CONFIRMED de la salle |
| Suppression d'un membre | Member Service | Reservation Service | Supprime les réservations du membre |
| Quota atteint | Reservation Service | Member Service | Passe `suspended` à `true` |
| Quota libéré | Reservation Service | Member Service | Passe `suspended` à `false` |

## Design Pattern

Le **State Pattern** est utilisé dans le Reservation Service pour gérer le cycle de vie des réservations (CONFIRMED → COMPLETED / CANCELLED). Voir [DESIGN_PATTERN.md](DESIGN_PATTERN.md) pour la justification complète.

## Technologies

- Spring Boot 3.2.5
- Spring Cloud (Config, Eureka, Gateway)
- Apache Kafka
- H2 Database (base en mémoire)
- springdoc-openapi (Swagger)
- Java 21 / Maven
