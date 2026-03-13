# Event-Driven Microservices with Kafka

A Spring Boot microservices project demonstrating event-driven architecture using Apache Kafka for asynchronous communication between services.

## 📋 Project Overview

This project implements a simple e-commerce order processing system using microservices architecture where services communicate through Kafka events rather than direct HTTP calls.

### Architecture Diagram

```
┌─────────────┐
│   Client    │
│  (Postman)  │
└──────┬──────┘
       │ HTTP POST
       ▼
┌─────────────────┐
│  API Gateway    │  Port 8086
│   (Proxy)       │
└────────┬────────┘
         │ HTTP
         ▼
┌─────────────────┐
│ Order Service   │  Port 8089
│  (Producer)     │
└────────┬────────┘
         │ Kafka Event
         ▼
┌──────────────────────┐
│   Kafka (KRaft)      │  Port 9092
│   Topic: order-topic │
└─────────┬────────────┘
          │
    ┌─────┴─────┐
    ▼           ▼
┌─────────┐  ┌─────────┐
│Inventory│  │ Billing │
│Service  │  │ Service │
│8088     │  │  8087   │
└─────────┘  └─────────┘
(Consumer)   (Consumer)
```

## 🚀 Technologies Used

- **Java 21**
- **Spring Boot 4.0.3**
- **Spring Kafka**
- **Apache Kafka (KRaft mode - no ZooKeeper)**
- **Docker & Docker Compose**
- **Maven**

## 📦 Services

| Service | Port | Type | Description |
|---------|------|------|-------------|
| **API Gateway** | 8086 | Entry Point | Routes requests to microservices |
| **Order Service** | 8089 | Producer | Creates orders and publishes events |
| **Inventory Service** | 8088 | Consumer | Updates stock based on orders |
| **Billing Service** | 8087 | Consumer | Generates invoices for orders |
| **Kafka** | 9092 | Message Broker | Event streaming platform |

## 🏗️ Project Structure

```
microservices-kafka-lab/
├── api-gateway/                  # API Gateway Service
│   └── src/main/
│       ├── java/com/example/gateway/
│       │   ├── ApiGatewayApplication.java
│       │   └── controller/
│       │       └── OrderProxyController.java
│       └── resources/
│           └── application.properties
│
├── order-service/                # Order Service (Producer)
│   └── src/main/
│       ├── java/com/example/order/
│       │   ├── OrderServiceApplication.java
│       │   ├── controller/
│       │   │   └── OrderController.java
│       │   ├── model/
│       │   │   └── Order.java
│       │   ├── event/
│       │   │   └── OrderEvent.java
│       │   └── service/
│       │       └── OrderProducer.java
│       └── resources/
│           └── application.properties
│
├── inventory-service/            # Inventory Service (Consumer)
│   └── src/main/
│       ├── java/com/example/inventory/
│       │   ├── InventoryServiceApplication.java
│       │   ├── event/
│       │   │   └── OrderEvent.java
│       │   └── service/
│       │       └── InventoryConsumer.java
│       └── resources/
│           └── application.properties
│
├── billing-service/              # Billing Service (Consumer)
│   └── src/main/
│       ├── java/com/example/billing/
│       │   ├── BillingServiceApplication.java
│       │   ├── event/
│       │   │   └── OrderEvent.java
│       │   └── service/
│       │       └── BillingConsumer.java
│       └── resources/
│           └── application.properties
│
└── docker-compose.yml            # Kafka setup
```

## 🔧 Prerequisites

Before running this project, ensure you have:

- **Java 21** installed
- **Maven** installed (or use included Maven wrapper)
- **Docker Desktop** installed and running
- **Git** (optional, for version control)
- **Postman** or **curl** for testing APIs

## 🛠️ Setup & Installation

### Step 1: Clone/Download the Project

```bash
cd c:/Users/vihan/OneDrive/Desktop/Projects/microservices-kafka-lab
```

### Step 2: Start Kafka

Start Kafka using Docker Compose:

```bash
docker-compose up -d
```

Verify Kafka is running:

```bash
docker ps
```

You should see `kafka-kraft` container running.

### Step 3: Start All Microservices

Open **4 separate terminals** and run each service:

#### Terminal 1 - Order Service
```bash
cd order-service
./mvnw.cmd spring-boot:run
# On Mac/Linux: ./mvnw spring-boot:run
```

#### Terminal 2 - Inventory Service
```bash
cd inventory-service
./mvnw.cmd spring-boot:run
```

#### Terminal 3 - Billing Service
```bash
cd billing-service
./mvnw.cmd spring-boot:run
```

#### Terminal 4 - API Gateway
```bash
cd api-gateway
./mvnw.cmd spring-boot:run
```

Wait for all services to display: `Started <ServiceName>Application`

## 🧪 Testing the Application

### Create an Order via API Gateway

**Using Postman or curl:**

```bash
POST http://localhost:8086/orders
Content-Type: application/json

{
  "product": "Laptop",
  "quantity": 2
}
```

**Expected Response:**

```
Order created with ID: <uuid>
```

### Verify Event Flow

After creating an order, check the console logs:

**Order Service (8089):**
```
Order created: Order{orderId='...', product='Laptop', quantity=2}
Publishing OrderEvent to Kafka: OrderEvent{...}
```

**Inventory Service (8088):**
```
Inventory Service received OrderEvent: OrderEvent{...}
Updating inventory for product: Laptop, quantity: 2
Stock updated successfully for order: ...
```

**Billing Service (8087):**
```
Billing Service received OrderEvent: OrderEvent{...}
Generating invoice for order: ...
Invoice generated - Amount: $200.0
```

### Alternative: Direct Order Service Call

You can also call the Order Service directly:

```bash
POST http://localhost:8089/orders
Content-Type: application/json

{
  "product": "Phone",
  "quantity": 1
}
```

## 🎯 Key Features

### Event-Driven Architecture
- **Asynchronous Communication**: Services don't wait for each other
- **Loose Coupling**: Services are independent and can be deployed separately
- **Scalability**: Easy to add new consumers or scale existing ones
- **Reliability**: Kafka ensures message delivery and persistence

### Kafka Configuration
- **KRaft Mode**: No ZooKeeper dependency (modern Kafka)
- **Topic**: `order-topic` for order events
- **Consumer Groups**:
  - `inventory-group` (Inventory Service)
  - `billing-group` (Billing Service)

### Spring Boot Features
- **Spring Kafka**: Easy Kafka integration
- **JSON Serialization**: Automatic event serialization/deserialization
- **REST Controllers**: Clean API design
- **DevTools**: Hot reload during development

## 📊 Event Flow Explained

1. **Client** sends POST request to API Gateway (`/orders`)
2. **API Gateway** forwards request to Order Service
3. **Order Service**:
   - Creates order with unique ID
   - Publishes `OrderEvent` to Kafka topic `order-topic`
   - Returns response immediately (non-blocking)
4. **Kafka** stores the event and distributes to all consumers
5. **Inventory Service** consumes event and updates stock
6. **Billing Service** consumes the same event and generates invoice

**Both consumers process the event in parallel and independently!**

## 🔍 Configuration Details

### Kafka Producer (Order Service)

```properties
spring.kafka.bootstrap-servers=localhost:9092
spring.kafka.producer.key-serializer=org.apache.kafka.common.serialization.StringSerializer
spring.kafka.producer.value-serializer=org.springframework.kafka.support.serializer.JsonSerializer
```

### Kafka Consumers (Inventory & Billing Services)

```properties
spring.kafka.bootstrap-servers=localhost:9092
spring.kafka.consumer.group-id=inventory-group  # or billing-group
spring.kafka.consumer.key-deserializer=org.apache.kafka.common.serialization.StringDeserializer
spring.kafka.consumer.value-deserializer=org.springframework.kafka.support.serializer.JsonDeserializer
spring.kafka.consumer.properties.spring.json.trusted.packages=*
```

## 🛑 Stopping the Application

### Stop All Services
Press `Ctrl+C` in each terminal running a service.

### Stop Kafka
```bash
docker-compose down
```

### Stop and Remove Volumes (Clean Reset)
```bash
docker-compose down -v
```

## 📄 License

This project is for educational purposes.

## 👨‍💻 Author

Created as part of SE4010 - Current Trends in Software Engineering course.

---

**Happy Coding! 🎉**
