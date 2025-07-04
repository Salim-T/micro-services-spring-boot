# Script PowerShell pour démarrer tous les microservices avec Kafka
# Utilisation: .\start-all-services.ps1

Write-Host "=== Démarrage de l'infrastructure Kafka et PostgreSQL ===" -ForegroundColor Green
docker-compose up -d

Write-Host "Attente du démarrage complet de l'infrastructure..." -ForegroundColor Yellow
Start-Sleep -Seconds 30

Write-Host "=== Compilation des microservices ===" -ForegroundColor Green

# Clients Service
Write-Host "Compilation du service Clients..." -ForegroundColor Cyan
Set-Location microcommerce-clients
& .\mvnw.cmd clean package -DskipTests
if ($LASTEXITCODE -ne 0) {
    Write-Host "Erreur lors de la compilation du service Clients" -ForegroundColor Red
    exit 1
}

# Products Service
Write-Host "Compilation du service Products..." -ForegroundColor Cyan
Set-Location ..\microcommerce-products
& .\mvnw.cmd clean package -DskipTests
if ($LASTEXITCODE -ne 0) {
    Write-Host "Erreur lors de la compilation du service Products" -ForegroundColor Red
    exit 1
}

# Orders Service
Write-Host "Compilation du service Orders..." -ForegroundColor Cyan
Set-Location ..\microcommerce-orders
& .\mvnw.cmd clean package -DskipTests
if ($LASTEXITCODE -ne 0) {
    Write-Host "Erreur lors de la compilation du service Orders" -ForegroundColor Red
    exit 1
}

Set-Location ..

Write-Host "=== Démarrage des microservices ===" -ForegroundColor Green

# Démarrage en arrière-plan de chaque service
Write-Host "Démarrage du service Clients (port 8081)..." -ForegroundColor Cyan
Start-Process PowerShell -ArgumentList @(
    "-Command", 
    "cd microcommerce-clients; `$env:POSTGRES_HOST='localhost'; `$env:POSTGRES_PORT='5432'; `$env:POSTGRES_DB='clients_db'; `$env:POSTGRES_USER='postgres'; `$env:POSTGRES_PASSWORD='password'; `$env:KAFKA_BOOTSTRAP_SERVERS='localhost:9092'; java -jar target/microcommerce-0.0.1-SNAPSHOT.jar"
) -WindowStyle Normal

Start-Sleep -Seconds 10

Write-Host "Démarrage du service Products (port 8082)..." -ForegroundColor Cyan
Start-Process PowerShell -ArgumentList @(
    "-Command", 
    "cd microcommerce-products; `$env:POSTGRES_HOST='localhost'; `$env:POSTGRES_PORT='5435'; `$env:POSTGRES_DB='products_db'; `$env:POSTGRES_USER='postgres'; `$env:POSTGRES_PASSWORD='password'; `$env:KAFKA_BOOTSTRAP_SERVERS='localhost:9092'; java -jar target/microcommerce-0.0.1-SNAPSHOT.jar"
) -WindowStyle Normal

Start-Sleep -Seconds 10

Write-Host "Démarrage du service Orders (port 8083)..." -ForegroundColor Cyan
Start-Process PowerShell -ArgumentList @(
    "-Command", 
    "cd microcommerce-orders; `$env:POSTGRES_HOST='localhost'; `$env:POSTGRES_PORT='5433'; `$env:POSTGRES_DB='orders_db'; `$env:POSTGRES_USER='postgres'; `$env:POSTGRES_PASSWORD='password'; `$env:KAFKA_BOOTSTRAP_SERVERS='localhost:9092'; `$env:CLIENTS_SERVICE_URL='http://localhost:8081'; `$env:PRODUCTS_SERVICE_URL='http://localhost:8082'; java -jar target/microcommerce-0.0.1-SNAPSHOT.jar"
) -WindowStyle Normal

Write-Host ""
Write-Host "=== Services en cours de démarrage ===" -ForegroundColor Green
Write-Host "Clients Service:    http://localhost:8081/api/clients" -ForegroundColor White
Write-Host "Products Service:   http://localhost:8082/api/products" -ForegroundColor White
Write-Host "Orders Service:     http://localhost:8083/api/orders" -ForegroundColor White
Write-Host ""
Write-Host "=== Interfaces de monitoring ===" -ForegroundColor Green
Write-Host "Kafka UI:           http://localhost:8090" -ForegroundColor White
Write-Host "Adminer (DB):       http://localhost:8080" -ForegroundColor White
Write-Host ""
Write-Host "Attendre quelques minutes pour que tous les services soient complètement démarrés..." -ForegroundColor Yellow
Write-Host ""
Write-Host "Pour arrêter tous les services, exécutez: docker-compose down" -ForegroundColor Red
