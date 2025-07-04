# Microservices avec Apache Kafka

Ce projet démontre une architecture de microservices utilisant Spring Boot et Apache Kafka pour la communication entre les services.

## Architecture

- **microcommerce-clients** (Port 8081) - Gestion des clients
- **microcommerce-products** (Port 8082) - Gestion des produits
- **microcommerce-orders** (Port 8083) - Gestion des commandes
- **Apache Kafka** (Port 9092) - Broker de messages
- **Kafka UI** (Port 8090) - Interface web pour monitorer Kafka
- **PostgreSQL** - Bases de données (clients: 5432, orders: 5433, products: 5435)
- **Adminer** (Port 8080) - Interface web pour les bases de données

## Prérequis

- Docker et Docker Compose
- Java 17+
- Maven 3.6+

## Installation et démarrage

### 1. Démarrer l'infrastructure (Kafka + PostgreSQL)

```powershell
docker-compose up -d
```

### 2. Compiler et démarrer les microservices

#### Service Clients (Port 8081)

```powershell
cd microcommerce-clients
.\mvnw clean package -DskipTests
$env:POSTGRES_HOST="localhost"; $env:POSTGRES_PORT="5432"; $env:POSTGRES_DB="clients_db"; $env:POSTGRES_USER="postgres"; $env:POSTGRES_PASSWORD="password"; $env:KAFKA_BOOTSTRAP_SERVERS="localhost:9092"; java -jar target/microcommerce-0.0.1-SNAPSHOT.jar
```

#### Service Products (Port 8082)

```powershell
cd microcommerce-products
.\mvnw clean package -DskipTests
$env:POSTGRES_HOST="localhost"; $env:POSTGRES_PORT="5435"; $env:POSTGRES_DB="products_db"; $env:POSTGRES_USER="postgres"; $env:POSTGRES_PASSWORD="password"; $env:KAFKA_BOOTSTRAP_SERVERS="localhost:9092"; java -jar target/microcommerce-0.0.1-SNAPSHOT.jar
```

#### Service Orders (Port 8083)

```powershell
cd microcommerce-orders
.\mvnw clean package -DskipTests
$env:POSTGRES_HOST="localhost"; $env:POSTGRES_PORT="5433"; $env:POSTGRES_DB="orders_db"; $env:POSTGRES_USER="postgres"; $env:POSTGRES_PASSWORD="password"; $env:KAFKA_BOOTSTRAP_SERVERS="localhost:9092"; $env:CLIENTS_SERVICE_URL="http://localhost:8081"; $env:PRODUCTS_SERVICE_URL="http://localhost:8082"; java -jar target/microcommerce-0.0.1-SNAPSHOT.jar
```

## Endpoints API

### Clients Service (localhost:8081)

- `GET /api/clients` - Liste tous les clients
- `GET /api/clients/{id}` - Récupère un client par ID
- `POST /api/clients` - Crée un nouveau client
- `PUT /api/clients/{id}` - Met à jour un client
- `DELETE /api/clients/{id}` - Supprime un client

### Products Service (localhost:8082)

- `GET /api/products` - Liste tous les produits
- `GET /api/products/{id}` - Récupère un produit par ID
- `POST /api/products` - Crée un nouveau produit
- `PUT /api/products/{id}` - Met à jour un produit
- `DELETE /api/products/{id}` - Supprime un produit

### Orders Service (localhost:8083)

- `GET /api/orders` - Liste toutes les commandes
- `GET /api/orders/{id}` - Récupère une commande par ID
- `POST /api/orders` - Crée une nouvelle commande (avec Kafka)
- `GET /api/orders/client/{clientId}` - Commandes d'un client

## Exemple d'utilisation avec Kafka

### 1. Créer un client

```powershell
curl -X POST http://localhost:8081/api/clients -H "Content-Type: application/json" -d '{
  "firstname": "John",
  "lastname": "Doe",
  "email": "john.doe@example.com",
  "address": "123 Main St",
  "city": "Paris",
  "country": "France",
  "phone": "0123456789"
}'
```

### 2. Créer des produits

```powershell
curl -X POST http://localhost:8082/api/products -H "Content-Type: application/json" -d '{
  "name": "Laptop",
  "price": 999.99
}'

curl -X POST http://localhost:8082/api/products -H "Content-Type: application/json" -d '{
  "name": "Mouse",
  "price": 29.99
}'
```

### 3. Créer une commande (déclenche un événement Kafka)

```powershell
curl -X POST http://localhost:8083/api/orders -H "Content-Type: application/json" -d '{
  "clientId": 1,
  "productIds": [1, 2],
  "description": "Commande ordinateur et souris"
}'
```

## Communication Kafka

Lorsqu'une commande est créée:

1. **Orders Service** valide que le client et les produits existent
2. **Orders Service** calcule le prix total en appelant Products Service
3. **Orders Service** sauvegarde la commande en base
4. **Orders Service** publie un événement `OrderCreatedEvent` sur Kafka
5. **Clients Service** et **Products Service** consomment l'événement pour leurs traitements

## Monitoring

- **Kafka UI**: http://localhost:8090 - Monitorer les topics, messages, consommateurs
- **Adminer**: http://localhost:8080 - Interface base de données
- **Health Checks**: `/actuator/health` sur chaque service

## Structure des événements Kafka

### Topic: `order-created`

```json
{
  "orderId": 1,
  "clientId": 1,
  "productIds": [1, 2],
  "total": 1029.98,
  "description": "Commande ordinateur et souris",
  "timestamp": "2025-07-03T10:30:00"
}
```

## Arrêter les services

```powershell
# Arrêter l'infrastructure
docker-compose down

# Ou avec suppression des volumes (ATTENTION: supprime les données)
docker-compose down -v
```

## Troubleshooting

### Vérifier que Kafka est prêt

```powershell
docker logs microcommerce-kafka
```

### Vérifier les bases de données

```powershell
docker logs microcommerce-clients-db
docker logs microcommerce-products-db
docker logs microcommerce-orders-db
```

### Reconstruire les services

```powershell
# Dans chaque dossier de microservice
.\mvnw clean package -DskipTests
```
