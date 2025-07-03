# Kafka Setup Guide

## Quick Start

1. **Start services**
   ```bash
   docker compose up -d
   ```

2. **Run microservices**
   ```bash
   # Terminal 1
   cd microcommerce-clients && ./mvnw spring-boot:run

   # Terminal 2  
   cd microcommerce-orders && ./mvnw spring-boot:run

   # Terminal 3
   cd microcommerce-products && ./mvnw spring-boot:run
   ```

3. **Access UIs**
   - Kafka UI: http://localhost:8090
   - Database Admin: http://localhost:8080

## What's Running

- **Kafka**: Message broker on port 9092
- **Zookeeper**: Kafka coordinator on port 2181
- **PostgreSQL**: 3 databases (ports 5432, 5433, 5435)
- **Kafka UI**: Web interface on port 8090
- **Adminer**: Database admin on port 8080

## Stop Services

```bash
docker compose down
