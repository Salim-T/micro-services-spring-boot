# Microcommerce - API REST Spring Boot

## Description

Microcommerce est une API REST développée avec Spring Boot pour gérer des produits dans un contexte de microservices e-commerce. Cette application propose des endpoints pour consulter la liste des produits et récupérer un produit spécifique.

## Prérequis

Avant de lancer le projet, assurez-vous d'avoir installé :

- **Java 17** ou version supérieure
- **Maven 3.6+** (ou utilisez le wrapper Maven inclus)
- **Git** (optionnel, pour cloner le projet)

### Vérification des prérequis

```powershell
# Vérifier la version de Java
java -version

# Vérifier la version de Maven
mvn -version
```

## Installation et lancement

### 1. Cloner le projet (si nécessaire)

```powershell
git clone <url-du-repository>
cd microcommerce
```

### 2. Compiler le projet

Utilisez le wrapper Maven fourni pour compiler le projet :

```powershell
# Sur Windows
.\mvnw clean compile
```

### 3. Lancer l'application

Vous avez plusieurs options pour lancer l'application :

#### Option 1 : Avec Maven (recommandé)

```powershell
.\mvnw spring-boot:run
```

#### Option 2 : Avec le JAR compilé

```powershell
# Compiler et packager l'application
.\mvnw clean package

# Lancer le JAR
java -jar target/microcommerce-0.0.1-SNAPSHOT.jar
```

#### Option 3 : Directement depuis l'IDE

Lancez la classe principale `MicrocommerceApplication.java` depuis votre IDE favori (IntelliJ IDEA, Eclipse, VS Code).

## Configuration

L'application utilise les paramètres suivants (définis dans `src/main/resources/application.properties`) :

- **Port** : 8080
- **Nom de l'application** : microcommerce

## Endpoints disponibles

Une fois l'application lancée, vous pouvez accéder aux endpoints suivants :

### API Produits

| Méthode | Endpoint         | Description                               |
| ------- | ---------------- | ----------------------------------------- |
| GET     | `/products`      | Récupère la liste de tous les produits    |
| GET     | `/products/{id}` | Récupère un produit spécifique par son ID |

### Endpoints Spring Boot Actuator

| Méthode | Endpoint           | Description                               |
| ------- | ------------------ | ----------------------------------------- |
| GET     | `/actuator/health` | Vérifier l'état de santé de l'application |
| GET     | `/actuator/info`   | Informations sur l'application            |

## Exemples d'utilisation

### Récupérer tous les produits

```bash
curl http://localhost:8080/products
```

### Récupérer un produit spécifique

```bash
curl http://localhost:8080/products/1
```

### Vérifier l'état de l'application

```bash
curl http://localhost:8080/actuator/health
```

## Tests

Pour lancer les tests unitaires :

```powershell
.\mvnw test
```

## Structure du projet

```
src/
├── main/
│   ├── java/
│   │   └── com/ecommerce/microcommerce/
│   │       ├── MicrocommerceApplication.java    # Classe principale
│   │       ├── controller/
│   │       │   └── ProductController.java       # Contrôleur REST
│   │       ├── dao/
│   │       │   ├── ProductDao.java              # Interface DAO
│   │       │   └── ProductDaoImpl.java          # Implémentation DAO
│   │       └── model/
│   │           └── Product.java                 # Modèle Product
│   └── resources/
│       └── application.properties               # Configuration
└── test/
    └── java/
        └── com/ecommerce/microcommerce/
            └── MicrocommerceApplicationTests.java
```

## Technologies utilisées

- **Spring Boot 3.5.3**
- **Spring Web** - Pour les endpoints REST
- **Spring Boot Actuator** - Pour le monitoring
- **Java 17**
- **Maven** - Gestionnaire de dépendances

## Développement

### Ajouter de nouveaux produits

Actuellement, l'application utilise une implémentation DAO en mémoire. Pour ajouter de nouveaux produits, vous pouvez :

1. Modifier la classe `ProductDaoImpl.java`
2. Ajouter de nouveaux endpoints dans `ProductController.java`
3. Intégrer une base de données (H2, MySQL, PostgreSQL...)

### Hot reload

Pour le développement, vous pouvez utiliser Spring Boot DevTools pour le rechargement automatique :

```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-devtools</artifactId>
    <scope>runtime</scope>
    <optional>true</optional>
</dependency>
```

## Troubleshooting

### Port déjà utilisé

Si le port 8080 est déjà utilisé, vous pouvez :

1. Modifier le port dans `application.properties` :

   ```properties
   server.port=8081
   ```

2. Ou passer le port en paramètre :
   ```powershell
   .\mvnw spring-boot:run -Dspring-boot.run.arguments=--server.port=8081
   ```

### Problèmes de compilation

Si vous rencontrez des problèmes de compilation :

```powershell
# Nettoyer le cache Maven
.\mvnw clean

# Forcer la recompilation
.\mvnw clean compile
```

## Contribuer

1. Forkez le projet
2. Créez une branche pour votre fonctionnalité (`git checkout -b feature/nouvelle-fonctionnalite`)
3. Committez vos changements (`git commit -am 'Ajouter une nouvelle fonctionnalité'`)
4. Poussez vers la branche (`git push origin feature/nouvelle-fonctionnalite`)
5. Créez une Pull Request

## Licence

Ce projet est développé dans un cadre éducatif.
