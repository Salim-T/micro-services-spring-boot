# Architecture Microservices avec Kafka - Documentation Complète

## 🏗️ Architecture

Cette solution implémente une architecture microservices avec communication asynchrone via Apache Kafka.

### Services

1. **microcommerce-clients** (Port 8081)

   - Gestion des clients
   - Consumer Kafka pour les événements de commandes

2. **microcommerce-products** (Port 8082)

   - Gestion des produits
   - Consumer Kafka pour les événements de commandes

3. **microcommerce-orders** (Port 8083)
   - Gestion des commandes
   - Producer Kafka pour les événements de création de commandes
   - Communication avec les autres services via WebClient

### Infrastructure

- **PostgreSQL** : Base de données pour chaque microservice
- **Apache Kafka** : Messagerie asynchrone entre services
- **Zookeeper** : Coordination pour Kafka
- **Kafka UI** : Interface web pour monitoring Kafka

## 📋 Modèle de Données

### Service Orders

#### Table `orders`

- `id` : Identifiant unique
- `description` : Description de la commande
- `total` : Montant total (BigDecimal)
- `client_id` : Référence vers le client
- `created_at` : Date de création

#### Table `order_products` (Lignes de commande)

- `id` : Identifiant unique
- `order_id` : Référence vers la commande
- `product_id` : Référence vers le produit
- `quantity` : Quantité commandée
- `unit_price` : Prix unitaire au moment de la commande
- `subtotal` : Sous-total (quantity × unit_price)

## 🔧 Configuration

### Kafka Topics

- `order-events` : Événements de création de commandes

### Variables d'environnement

```bash
# Base de données
POSTGRES_HOST=postgres
POSTGRES_PORT=5432
POSTGRES_DB=microcommerce
POSTGRES_USER=admin
POSTGRES_PASSWORD=admin123

# Kafka
KAFKA_BOOTSTRAP_SERVERS=kafka:9092

# URLs des services
CLIENTS_SERVICE_URL=http://microcommerce-clients:8081
PRODUCTS_SERVICE_URL=http://microcommerce-products:8082
```

## 🚀 Démarrage

### Prérequis

- Docker et Docker Compose
- PowerShell (pour les scripts Windows)

### Démarrage complet

```powershell
.\start-all-services.ps1
```

### Démarrage manuel

```powershell
# Infrastructure (Kafka, PostgreSQL)
docker-compose up -d kafka zookeeper postgres kafka-ui

# Attendre que Kafka soit prêt
Start-Sleep -Seconds 30

# Services
docker-compose up -d microcommerce-clients microcommerce-products microcommerce-orders
```

## 📡 API Reference

### Clients Service (Port 8081)

```bash
GET    /api/clients           # Liste des clients
GET    /api/clients/{id}      # Client par ID
POST   /api/clients           # Créer un client
PUT    /api/clients/{id}      # Mettre à jour un client
DELETE /api/clients/{id}      # Supprimer un client
```

### Products Service (Port 8082)

```bash
GET    /api/products          # Liste des produits
GET    /api/products/{id}     # Produit par ID
POST   /api/products          # Créer un produit
PUT    /api/products/{id}     # Mettre à jour un produit
DELETE /api/products/{id}     # Supprimer un produit
```

### Orders Service (Port 8083)

```bash
GET    /api/orders                    # Liste des commandes
GET    /api/orders/{id}               # Commande par ID
POST   /api/orders                    # Créer une commande
PUT    /api/orders/{id}               # Mettre à jour une commande
DELETE /api/orders/{id}               # Supprimer une commande
GET    /api/orders/client/{clientId}  # Commandes d'un client
GET    /api/orders/search?keyword=X   # Recherche de commandes
```

## 📝 Création d'une Commande

### Format de la requête

```json
{
  "clientId": 1,
  "description": "Ma commande",
  "products": [
    {
      "productId": 1,
      "quantity": 2
    },
    {
      "productId": 2,
      "quantity": 1
    }
  ]
}
```

### Processus de création

1. **Validation** : Vérification de l'existence du client et des produits
2. **Calcul** : Récupération des prix et calcul du total
3. **Persistance** : Sauvegarde de la commande et des lignes de commande
4. **Événement Kafka** : Publication de l'événement `OrderCreatedEvent`

### Événement Kafka généré

```json
{
  "orderId": 1,
  "clientId": 1,
  "productIds": [1, 2],
  "totalAmount": 150.0,
  "description": "Ma commande"
}
```

## 🧪 Tests

### Tests automatisés

```powershell
.\test-microservices.ps1
```

### Tests manuels

Voir `README-TESTS.md` pour les détails des tests manuels.

### Validation

- ✅ Création de commandes avec validation métier
- ✅ Communication inter-services via WebClient
- ✅ Événements Kafka asynchrones
- ✅ Gestion des erreurs (client/produit inexistant)
- ✅ Calcul automatique des totaux

## 🔍 Monitoring

### Kafka UI

- URL : http://localhost:8080
- Visualisation des topics, messages, consumers

### Logs des services

```powershell
# Voir les logs d'un service
docker logs microcommerce-orders

# Suivre les logs en temps réel
docker logs -f microcommerce-orders
```

### Bases de données

Chaque service a sa propre base PostgreSQL accessible via :

- Host : localhost
- Port : 5432 (clients), 5433 (products), 5434 (orders)
- User : admin
- Password : admin123

## 🛠️ Développement

### Structure du projet

```
micro_services_spring-boot/
├── docker-compose.yml              # Configuration Docker complète
├── microcommerce-clients/          # Service de gestion des clients
├── microcommerce-products/         # Service de gestion des produits
├── microcommerce-orders/           # Service de gestion des commandes
├── start-all-services.ps1          # Script de démarrage
├── test-microservices.ps1          # Script de tests
├── README-KAFKA.md                 # Documentation Kafka
├── README-TESTS.md                 # Guide de tests
└── README-COMPLETE.md              # Cette documentation
```

### Technologies utilisées

- **Spring Boot 3.5.3** : Framework principal
- **Spring Data JPA** : Persistance
- **Spring Kafka** : Intégration Kafka
- **WebFlux** : Communication HTTP asynchrone
- **PostgreSQL** : Base de données
- **Docker** : Conteneurisation

## 🔧 Dépannage

### Problèmes courants

1. **Services ne démarrent pas**

   - Vérifier que les ports ne sont pas occupés
   - Attendre le démarrage complet de Kafka

2. **Erreurs de communication entre services**

   - Vérifier les URLs dans `application.properties`
   - S'assurer que tous les services sont démarrés

3. **Problèmes Kafka**
   - Vérifier l'état de Kafka : `docker logs kafka`
   - Redémarrer Kafka si nécessaire : `docker-compose restart kafka`

### Reset complet

```powershell
# Arrêter tous les services
docker-compose down

# Nettoyer les volumes (⚠️ perte de données)
docker-compose down -v

# Redémarrer
.\start-all-services.ps1
```

## 📈 Extensions possibles

1. **Sécurité** : Ajout d'OAuth2/JWT
2. **Monitoring** : Intégration Prometheus/Grafana
3. **Tracing** : Ajout de Zipkin/Jaeger
4. **Circuit Breaker** : Resilience4j
5. **API Gateway** : Spring Cloud Gateway
6. **Service Discovery** : Eureka
7. **Configuration centralisée** : Spring Cloud Config
