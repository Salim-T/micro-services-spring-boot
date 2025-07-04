# Kafka Setup for Microservices Project

## Overview
This document describes the complete Kafka integration setup for the Spring Boot microservices project. Kafka has been successfully configured to enable event-driven communication between microservices.

## Architecture

### Components Added
1. **Apache Kafka** - Message broker for event streaming
2. **Zookeeper** - Coordination service for Kafka
3. **Kafka UI** - Web interface for monitoring Kafka topics and messages
4. **Event Producer** - Spring Boot service to publish events
5. **Event Models** - Data structures for client events

## Infrastructure Setup

### Docker Services
The following services were added to `docker-compose.yml`:

```yaml
# Zookeeper - Required for Kafka coordination
zookeeper:
  image: confluentinc/cp-zookeeper:7.4.0
  environment:
    ZOOKEEPER_CLIENT_PORT: 2181
    ZOOKEEPER_TICK_TIME: 2000

# Kafka Broker
kafka:
  image: confluentinc/cp-kafka:7.4.0
  depends_on: [zookeeper]
  ports: ["9092:9092", "9101:9101"]
  environment:
    KAFKA_BROKER_ID: 1
    KAFKA_ZOOKEEPER_CONNECT: 'zookeeper:2181'
    KAFKA_LISTENER_SECURITY_PROTOCOL_MAP: PLAINTEXT:PLAINTEXT,PLAINTEXT_HOST:PLAINTEXT
    KAFKA_ADVERTISED_LISTENERS: PLAINTEXT://kafka:29092,PLAINTEXT_HOST://localhost:9092
    KAFKA_OFFSETS_TOPIC_REPLICATION_FACTOR: 1
    KAFKA_TRANSACTION_STATE_LOG_MIN_ISR: 1
    KAFKA_TRANSACTION_STATE_LOG_REPLICATION_FACTOR: 1

# Kafka UI for monitoring
kafka-ui:
  image: provectuslabs/kafka-ui:latest
  depends_on: [kafka]
  ports: ["8090:8080"]
  environment:
    KAFKA_CLUSTERS_0_NAME: local
    KAFKA_CLUSTERS_0_BOOTSTRAPSERVERS: kafka:29092
```

### Network Configuration
- **Kafka Broker**: `localhost:9092`
- **Kafka UI**: `http://localhost:8090`
- **Zookeeper**: `localhost:2181`

## Application Integration

### Dependencies Added
Added to `microcommerce-clients/pom.xml`:
```xml
<dependency>
    <groupId>org.springframework.kafka</groupId>
    <artifactId>spring-kafka</artifactId>
</dependency>
```

### Configuration Files

#### Kafka Configuration (`KafkaConfig.java`)
```java
@Configuration
@EnableKafka
public class KafkaConfig {
    
    @Bean
    public ProducerFactory<String, Object> producerFactory() {
        Map<String, Object> configProps = new HashMap<>();
        configProps.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        configProps.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        configProps.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);
        configProps.put(ProducerConfig.ACKS_CONFIG, "all");
        configProps.put(ProducerConfig.RETRIES_CONFIG, 3);
        configProps.put(ProducerConfig.ENABLE_IDEMPOTENCE_CONFIG, true);
        return new DefaultKafkaProducerFactory<>(configProps);
    }

    @Bean
    public KafkaTemplate<String, Object> kafkaTemplate() {
        return new KafkaTemplate<>(producerFactory());
    }

    @Bean
    public KafkaAdmin kafkaAdmin() {
        Map<String, Object> configs = new HashMap<>();
        configs.put(AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        return new KafkaAdmin(configs);
    }

    @Bean
    public NewTopic clientEventsTopic() {
        return TopicBuilder.name("client-events")
                .partitions(3)
                .replicas(1)
                .build();
    }
}
```

#### Application Properties
```properties
# Kafka Configuration
spring.kafka.bootstrap-servers=localhost:9092
spring.kafka.producer.key-serializer=org.apache.kafka.common.serialization.StringSerializer
spring.kafka.producer.value-serializer=org.springframework.kafka.support.serializer.JsonSerializer
spring.kafka.producer.acks=all
spring.kafka.producer.retries=3
spring.kafka.producer.enable-idempotence=true
```

### Event Model (`ClientEvent.java`)
```java
public class ClientEvent {
    private String eventType; // CREATED, UPDATED, DELETED
    private int clientId;
    private String firstname;
    private String lastname;
    private String email;
    private String address;
    private String city;
    private String country;
    private String phone;
    private LocalDateTime timestamp;
    
    // Constructors, getters, setters...
}
```

### Event Producer (`ClientEventProducer.java`)
```java
@Service
public class ClientEventProducer {
    
    private final KafkaTemplate<String, Object> kafkaTemplate;
    private static final String TOPIC = "client-events";
    
    public void publishClientEvent(ClientEvent event) {
        System.out.println("Publishing client event: " + event);
        kafkaTemplate.send(TOPIC, event.getClientId().toString(), event)
            .whenComplete((result, ex) -> {
                if (ex == null) {
                    System.out.println("Client event published successfully");
                } else {
                    System.err.println("Failed to publish client event: " + ex.getMessage());
                }
            });
    }
}
```

### Service Integration
Updated `ClientService.java` to publish events on client creation and deletion:

```java
public Client saveClient(Client client) {
    Client savedClient = clientRepository.save(client);
    
    // Publish Kafka event
    ClientEvent event = new ClientEvent(
        "CLIENT_CREATED",
        savedClient.getId(),
        savedClient.getFirstname(),
        savedClient.getLastname(),
        savedClient.getEmail(),
        savedClient.getAddress(),
        savedClient.getCity(),
        savedClient.getCountry(),
        savedClient.getPhone()
    );
    clientEventProducer.publishClientEvent(event);
    
    return savedClient;
}

public void deleteClient(int id) {
    Optional<Client> clientOpt = clientRepository.findById(id);
    if (clientOpt.isPresent()) {
        Client client = clientOpt.get();
        clientRepository.deleteById(id);
        
        // Publish Kafka event
        ClientEvent event = new ClientEvent(
            "CLIENT_DELETED",
            client.getId(),
            client.getFirstname(),
            client.getLastname(),
            client.getEmail(),
            client.getAddress(),
            client.getCity(),
            client.getCountry(),
            client.getPhone()
        );
        clientEventProducer.publishClientEvent(event);
    }
}
```

## Topics Created

### client-events
- **Purpose**: Publishes client lifecycle events (CREATE, DELETE)
- **Partitions**: 3
- **Replication Factor**: 1
- **Message Format**: JSON

#### Sample Messages
```json
{
  "eventType": "CLIENT_CREATED",
  "clientId": 8,
  "firstname": "Bob",
  "lastname": "Wilson",
  "email": "bob.wilson@example.com",
  "address": "321 Elm St",
  "city": "Nice",
  "country": "France",
  "phone": "0444555666",
  "timestamp": "2025-07-04T10:59:13"
}

{
  "eventType": "CLIENT_DELETED",
  "clientId": 9,
  "firstname": "Carol",
  "lastname": "Davis",
  "email": "carol.davis@example.com",
  "address": "654 Oak Blvd",
  "city": "Toulouse",
  "country": "France",
  "phone": "0333444555",
  "timestamp": "2025-07-04T10:59:49"
}
```

## Testing and Verification

### Starting the Services
```bash
# Start all services including Kafka
docker-compose up -d

# Start the microservices
cd microcommerce-clients && ./mvnw spring-boot:run
cd microcommerce-products && ./mvnw spring-boot:run
```

### Testing Event Publishing
```bash
# Create a client (triggers CLIENT_CREATED event)
curl -X POST http://localhost:8081/api/clients \
  -H "Content-Type: application/json" \
  -d '{"firstname":"Bob","lastname":"Wilson","email":"bob.wilson@example.com","address":"321 Elm St","city":"Nice","country":"France","phone":"0444555666"}'

# Delete a client (triggers CLIENT_DELETED event)
curl -X DELETE http://localhost:8081/api/clients/8
```

### Monitoring Events
```bash
# View all messages in the client-events topic
docker exec microcommerce-kafka kafka-console-consumer \
  --bootstrap-server localhost:9092 \
  --topic client-events \
  --from-beginning

# List all topics
docker exec microcommerce-kafka kafka-topics \
  --bootstrap-server localhost:9092 \
  --list
```

### Kafka UI Access
- **URL**: http://localhost:8090
- **Features**: 
  - View topics and partitions
  - Monitor message flow
  - Browse message content
  - View consumer groups

## Benefits Achieved

1. **Event-Driven Architecture**: Microservices can now communicate asynchronously through events
2. **Decoupling**: Services are loosely coupled and can evolve independently
3. **Scalability**: Kafka provides horizontal scaling capabilities
4. **Reliability**: Built-in replication and fault tolerance
5. **Monitoring**: Kafka UI provides real-time monitoring of message flow
6. **Audit Trail**: All client operations are logged as events for auditing

## Next Steps

1. **Add Consumers**: Create consumer services in other microservices to react to client events
2. **Error Handling**: Implement dead letter queues for failed message processing
3. **Schema Registry**: Add schema validation for message formats
4. **Security**: Implement authentication and authorization for Kafka
5. **Monitoring**: Add metrics and alerting for Kafka performance
6. **Additional Events**: Extend to other entities (products, orders, etc.)

## Troubleshooting

### Common Issues
1. **Connection Refused**: Ensure Kafka container is running and accessible on port 9092
2. **Topic Not Found**: Verify topic creation in KafkaConfig
3. **Serialization Errors**: Check JsonSerializer configuration
4. **Import Issues**: Ensure correct package imports in Java files

### Useful Commands
```bash
# Check Kafka container logs
docker logs microcommerce-kafka

# Check if Kafka is responding
docker exec microcommerce-kafka kafka-broker-api-versions --bootstrap-server localhost:9092

# Reset consumer group (if needed)
docker exec microcommerce-kafka kafka-consumer-groups \
  --bootstrap-server localhost:9092 \
  --group your-group \
  --reset-offsets \
  --to-earliest \
  --topic client-events \
  --execute
```

## Conclusion

Kafka has been successfully integrated into the microservices architecture, providing a robust foundation for event-driven communication. The system is now capable of publishing and consuming events, enabling better scalability and maintainability of the overall application.
