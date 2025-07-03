# 🐳 Guide Docker pour Microcommerce

## Configuration Docker avec PostgreSQL

Ce guide vous explique comment lancer l'environnement de développement avec Docker et PostgreSQL.

## Prérequis

- **Docker Desktop** installé et en cours d'exécution
- **Docker Compose** (inclus avec Docker Desktop)

### Vérification des prérequis

```powershell
# Vérifier Docker
docker --version

# Vérifier Docker Compose
docker-compose --version
```

## 🚀 Démarrage rapide

### 1. Lancer les services Docker

```powershell
# Démarrer PostgreSQL et Adminer
docker-compose up -d

# Vérifier que les services sont bien lancés
docker-compose ps
```

### 2. Vérifier les logs

```powershell
# Voir les logs de PostgreSQL
docker-compose logs postgres

# Voir les logs de Adminer
docker-compose logs adminer

# Suivre les logs en temps réel
docker-compose logs -f
```

## 📊 Services disponibles

### PostgreSQL

- **Host** : `localhost`
- **Port** : `5432`
- **Database** : `microcommerce`
- **Username** : `microcommerce_user`
- **Password** : `microcommerce_password`

### Adminer (Interface web)

- **URL** : http://localhost:8081
- **Système** : PostgreSQL
- **Serveur** : `postgres`
- **Utilisateur** : `microcommerce_user`
- **Mot de passe** : `microcommerce_password`
- **Base de données** : `microcommerce`

## 🔧 Configuration d'Adminer

1. Ouvrez http://localhost:8081 dans votre navigateur
2. Sélectionnez le système : **PostgreSQL**
3. Remplissez les champs de connexion :
   - **Serveur** : `postgres`
   - **Utilisateur** : `microcommerce_user`
   - **Mot de passe** : `microcommerce_password`
   - **Base de données** : `microcommerce`
4. Cliquez sur **Connexion**

## 📝 Données d'exemple

La base de données est automatiquement initialisée avec :

- Table `products` avec des champs : `id`, `name`, `price`, `created_at`, `updated_at`
- 10 produits d'exemple
- Triggers automatiques pour `updated_at`

## 🛠️ Commandes utiles

### Gestion des services

```powershell
# Démarrer les services
docker-compose up -d

# Arrêter les services
docker-compose down

# Redémarrer les services
docker-compose restart

# Voir l'état des services
docker-compose ps

# Voir les logs
docker-compose logs -f
```

### Gestion des données

```powershell
# Sauvegarder la base de données
docker-compose exec postgres pg_dump -U microcommerce_user microcommerce > backup.sql

# Restaurer la base de données
docker-compose exec -T postgres psql -U microcommerce_user microcommerce < backup.sql

# Se connecter à PostgreSQL en ligne de commande
docker-compose exec postgres psql -U microcommerce_user -d microcommerce
```

### Nettoyage

```powershell
# Arrêter et supprimer les conteneurs
docker-compose down

# Supprimer également les volumes (ATTENTION: supprime les données!)
docker-compose down -v

# Nettoyer les images Docker inutilisées
docker system prune
```

## 🔒 Sécurité

⚠️ **Important** : Les mots de passe dans ce fichier sont pour le développement uniquement.
En production, utilisez des mots de passe sécurisés et des variables d'environnement.

## 🐛 Résolution de problèmes

### Port déjà utilisé

Si le port 5432 est déjà utilisé :

```powershell
# Voir quel processus utilise le port
netstat -ano | findstr :5432

# Modifier le port dans docker-compose.yml
ports:
  - "5433:5432"  # Utiliser le port 5433 à la place
```

### Problèmes de permission

```powershell
# Recréer les volumes avec les bonnes permissions
docker-compose down -v
docker-compose up -d
```

### Base de données corrompue

```powershell
# Supprimer le volume et recréer
docker-compose down
docker volume rm microcommerce_postgres_data
docker-compose up -d
```

## 📁 Structure des fichiers Docker

```
microcommerce/
├── docker-compose.yml          # Configuration des services
├── .env                       # Variables d'environnement
└── docker/
    └── init-db.sql            # Script d'initialisation de la DB
```

## 🔗 Intégration avec Spring Boot

Pour connecter votre application Spring Boot à PostgreSQL, ajoutez dans `application.properties` :

```properties
# Configuration PostgreSQL
spring.datasource.url=jdbc:postgresql://localhost:5432/microcommerce
spring.datasource.username=microcommerce_user
spring.datasource.password=microcommerce_password
spring.datasource.driver-class-name=org.postgresql.Driver

# Configuration JPA/Hibernate
spring.jpa.hibernate.ddl-auto=validate
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.PostgreSQLDialect
```

## 📚 Ressources supplémentaires

- [Documentation Docker Compose](https://docs.docker.com/compose/)
- [Documentation PostgreSQL](https://www.postgresql.org/docs/)
- [Documentation Adminer](https://www.adminer.org/)
