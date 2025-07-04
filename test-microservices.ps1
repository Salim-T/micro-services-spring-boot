# Script de test automatisé pour les microservices
# Ce script teste l'intégration complète des microservices avec Kafka

Write-Host "🚀 Démarrage des tests des microservices..." -ForegroundColor Green

# Configuration
$ClientsUrl = "http://localhost:8081"
$ProductsUrl = "http://localhost:8082"
$OrdersUrl = "http://localhost:8083"

# Fonction pour faire un appel API
function Invoke-ApiCall {
    param(
        [string]$Url,
        [string]$Method = "GET",
        [string]$Body = $null,
        [string]$Description
    )
    
    Write-Host "📡 $Description" -ForegroundColor Yellow
    
    try {
        $headers = @{
            "Content-Type" = "application/json"
        }
        
        if ($Body) {
            $response = Invoke-RestMethod -Uri $Url -Method $Method -Body $Body -Headers $headers
        } else {
            $response = Invoke-RestMethod -Uri $Url -Method $Method -Headers $headers
        }
        
        Write-Host "✅ Succès:" -ForegroundColor Green
        $response | ConvertTo-Json -Depth 3 | Write-Host
        return $response
    }
    catch {
        Write-Host "❌ Erreur: $($_.Exception.Message)" -ForegroundColor Red
        return $null
    }
    
    Write-Host ""
}

# Attendre que les services soient prêts
Write-Host "⏳ Attente du démarrage des services..." -ForegroundColor Blue
Start-Sleep -Seconds 10

# Test 1: Vérifier les clients
Write-Host "`n=== TEST 1: Vérification des clients ===" -ForegroundColor Cyan
$clients = Invoke-ApiCall -Url "$ClientsUrl/api/clients" -Description "Récupération de la liste des clients"

if ($clients -and $clients.Count -gt 0) {
    $firstClientId = $clients[0].id
    Write-Host "🆔 Premier client ID: $firstClientId" -ForegroundColor Magenta
    Invoke-ApiCall -Url "$ClientsUrl/api/clients/$firstClientId" -Description "Récupération du client $firstClientId"
} else {
    Write-Host "⚠️ Aucun client trouvé" -ForegroundColor Yellow
}

# Test 2: Vérifier les produits
Write-Host "`n=== TEST 2: Vérification des produits ===" -ForegroundColor Cyan
$products = Invoke-ApiCall -Url "$ProductsUrl/api/products" -Description "Récupération de la liste des produits"

if ($products -and $products.Count -gt 0) {
    $firstProductId = $products[0].id
    $secondProductId = if ($products.Count -gt 1) { $products[1].id } else { $firstProductId }
    Write-Host "🆔 Premier produit ID: $firstProductId" -ForegroundColor Magenta
    Write-Host "🆔 Deuxième produit ID: $secondProductId" -ForegroundColor Magenta
    Invoke-ApiCall -Url "$ProductsUrl/api/products/$firstProductId" -Description "Récupération du produit $firstProductId"
} else {
    Write-Host "⚠️ Aucun produit trouvé" -ForegroundColor Yellow
}

# Test 3: Créer une commande
Write-Host "`n=== TEST 3: Création d'une commande ===" -ForegroundColor Cyan

if ($clients -and $clients.Count -gt 0 -and $products -and $products.Count -gt 0) {
    $orderRequest = @{
        clientId = $firstClientId
        description = "Commande test automatisée $(Get-Date -Format 'yyyy-MM-dd HH:mm:ss')"
        products = @(
            @{
                productId = $firstProductId
                quantity = 2
            }
        )
    }
    
    if ($products.Count -gt 1) {
        $orderRequest.products += @{
            productId = $secondProductId
            quantity = 1
        }
    }
    
    $orderJson = $orderRequest | ConvertTo-Json -Depth 3
    $newOrder = Invoke-ApiCall -Url "$OrdersUrl/api/orders" -Method "POST" -Body $orderJson -Description "Création d'une nouvelle commande"
    
    if ($newOrder) {
        Write-Host "🎉 Commande créée avec succès! ID: $($newOrder.id)" -ForegroundColor Green
    }
} else {
    Write-Host "❌ Impossible de créer une commande - clients ou produits manquants" -ForegroundColor Red
}

# Test 4: Vérifier les commandes
Write-Host "`n=== TEST 4: Vérification des commandes ===" -ForegroundColor Cyan
$orders = Invoke-ApiCall -Url "$OrdersUrl/api/orders" -Description "Récupération de la liste des commandes"

if ($orders -and $orders.Count -gt 0) {
    $firstOrderId = $orders[0].id
    Write-Host "🆔 Première commande ID: $firstOrderId" -ForegroundColor Magenta
    Invoke-ApiCall -Url "$OrdersUrl/api/orders/$firstOrderId" -Description "Récupération de la commande $firstOrderId"
    
    if ($clients -and $clients.Count -gt 0) {
        Invoke-ApiCall -Url "$OrdersUrl/api/orders/client/$firstClientId" -Description "Récupération des commandes du client $firstClientId"
    }
} else {
    Write-Host "⚠️ Aucune commande trouvée" -ForegroundColor Yellow
}

# Test 5: Tests de validation d'erreur
Write-Host "`n=== TEST 5: Tests de validation d'erreur ===" -ForegroundColor Cyan

# Test avec client inexistant
$invalidOrderRequest = @{
    clientId = 999999
    description = "Test client inexistant"
    products = @(@{productId = $firstProductId; quantity = 1})
} | ConvertTo-Json -Depth 3

Invoke-ApiCall -Url "$OrdersUrl/api/orders" -Method "POST" -Body $invalidOrderRequest -Description "Test avec client inexistant (doit échouer)"

# Test avec produit inexistant
if ($clients -and $clients.Count -gt 0) {
    $invalidProductRequest = @{
        clientId = $firstClientId
        description = "Test produit inexistant"
        products = @(@{productId = 999999; quantity = 1})
    } | ConvertTo-Json -Depth 3
    
    Invoke-ApiCall -Url "$OrdersUrl/api/orders" -Method "POST" -Body $invalidProductRequest -Description "Test avec produit inexistant (doit échouer)"
}

Write-Host "`n🏁 Tests terminés!" -ForegroundColor Green
Write-Host "📊 Consultez Kafka UI à l'adresse http://localhost:8080 pour voir les événements Kafka" -ForegroundColor Blue
Write-Host "📋 Vérifiez les logs des containers Docker pour plus de détails" -ForegroundColor Blue
