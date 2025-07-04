# Tests API Microservices avec Kafka

Ce document décrit comment tester l'intégration complète des microservices avec Kafka.

## Démarrage des services

1. **Démarrer tous les services avec Docker Compose :**

```powershell
cd "C:\Users\tizi-\Desktop\ESGI\Cours\M2\micro-services\micro_services_spring-boot"
.\start-all-services.ps1
```

2. **Attendre que tous les services soient démarrés :**
   - Kafka : http://localhost:9092
   - Kafka UI : http://localhost:8080
   - Clients Service : http://localhost:8081
   - Products Service : http://localhost:8082
   - Orders Service : http://localhost:8083

## Tests API

### 1. Vérifier les clients

```bash
# Lister tous les clients
curl -X GET http://localhost:8081/api/clients

# Obtenir un client spécifique
curl -X GET http://localhost:8081/api/clients/1
```

### 2. Vérifier les produits

```bash
# Lister tous les produits
curl -X GET http://localhost:8082/api/products

# Obtenir un produit spécifique
curl -X GET http://localhost:8082/api/products/1
```

### 3. Créer une commande (avec événement Kafka)

```bash
# Créer une commande avec plusieurs produits et quantités
curl -X POST http://localhost:8083/api/orders \
  -H "Content-Type: application/json" \
  -d '{
    "clientId": 1,
    "description": "Commande test avec Kafka",
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
  }'
```

### 4. Vérifier les commandes

```bash
# Lister toutes les commandes
curl -X GET http://localhost:8083/api/orders

# Obtenir une commande spécifique
curl -X GET http://localhost:8083/api/orders/1

# Obtenir les commandes d'un client
curl -X GET http://localhost:8083/api/orders/client/1
```

## Vérification Kafka

1. **Accéder à Kafka UI :** http://localhost:8080
2. **Vérifier les topics :**
   - `order-events` : Contient les événements de création de commandes
3. **Vérifier les messages :** Les événements `OrderCreatedEvent` devraient apparaître après chaque création de commande

## Structure de l'événement Kafka

```json
{
  "orderId": 1,
  "clientId": 1,
  "productIds": [1, 2],
  "totalAmount": 150.0,
  "description": "Commande test avec Kafka"
}
```

## Tests de validation

### Test 1: Client inexistant

```bash
curl -X POST http://localhost:8083/api/orders \
  -H "Content-Type: application/json" \
  -d '{
    "clientId": 999,
    "description": "Test client inexistant",
    "products": [{"productId": 1, "quantity": 1}]
  }'
```

**Résultat attendu :** Erreur 400 "Client with ID 999 does not exist"

### Test 2: Produit inexistant

```bash
curl -X POST http://localhost:8083/api/orders \
  -H "Content-Type: application/json" \
  -d '{
    "clientId": 1,
    "description": "Test produit inexistant",
    "products": [{"productId": 999, "quantity": 1}]
  }'
```

**Résultat attendu :** Erreur 400 "One or more products do not exist"

### Test 3: Commande vide

```bash
curl -X POST http://localhost:8083/api/orders \
  -H "Content-Type: application/json" \
  -d '{
    "clientId": 1,
    "description": "Test commande vide",
    "products": []
  }'
```

## Logs à surveiller

1. **Microservice Orders :** Messages de validation et création de commandes
2. **Microservice Clients :** Messages de réception d'événements Kafka
3. **Microservice Products :** Messages de réception d'événements Kafka
4. **Kafka :** Production et consommation de messages

## Dépannage

1. **Vérifier les containers Docker :**

```powershell
docker ps
```

2. **Vérifier les logs des services :**

```powershell
docker logs microcommerce-orders
docker logs microcommerce-clients
docker logs microcommerce-products
docker logs kafka
```

3. **Redémarrer un service spécifique :**

```powershell
docker-compose restart microcommerce-orders
```
