# Visa Payment Service - Java Implementation

A robust payment processing service built with Spring Boot that handles account management and transaction processing for Visa's payment system.

## 🚀 Features

- **Account Management**: Create and retrieve customer accounts
- **Transaction Processing**: Handle various transaction types (purchase, withdrawal, credit voucher, installments)
- **Business Logic**: Automatic amount sign handling based on transaction type
- **Validation**: Comprehensive input validation and error handling
- **Documentation**: Swagger/OpenAPI documentation
- **Testing**: Comprehensive unit and integration tests
- **Docker Support**: Containerized deployment

## 📋 API Endpoints

### Account Management

#### Create Account
```http
POST /accounts
Content-Type: application/json

{
  "document_number": "12345678900"
}
```

#### Get Account
```http
GET /accounts/{accountId}
```

### Transaction Management

#### Create Transaction
```http
POST /transactions
Content-Type: application/json

{
  "account_id": 1,
  "operation_type_id": 4,
  "amount": 123.45
}
```

## 🏗️ Architecture

### Data Model

- **Account**: Customer account information
- **OperationType**: Transaction type definitions
- **Transaction**: Individual transaction records

### Business Rules

- Purchase and withdrawal transactions are stored with negative amounts
- Credit voucher transactions are stored with positive amounts
- Document numbers must be exactly 11 digits
- Amounts must be positive in requests (sign is applied automatically)

## 🛠️ Technology Stack

- **Java 17**
- **Spring Boot 3.2.0**
- **Spring Data JPA**
- **H2 Database** (in-memory for development)
- **Spring Validation**
- **Swagger/OpenAPI**
- **JUnit 5**
- **MockMvc**

## 🚀 Quick Start

### Prerequisites

- Java 17 or higher
- Maven 3.6 or higher

### Running the Application

#### Option 1: Using the run script
```bash
./run
```

#### Option 2: Using Maven
```bash
mvn clean package
java -jar target/visa-payment-service-0.0.1-SNAPSHOT.jar
```

#### Option 3: Using Docker
```bash
docker-compose up --build
```

### Access Points

- **Application**: http://localhost:8080
- **API Documentation**: http://localhost:8080/swagger-ui.html
- **H2 Console**: http://localhost:8080/h2-console
- **Health Check**: http://localhost:8080/actuator/health

## 🧪 Testing

### Run Tests
```bash
mvn test
```

### Test Coverage
The project includes comprehensive tests covering:
- Controller layer tests
- Service layer tests
- Integration tests
- Validation tests

## 📊 Database Schema

### Accounts Table
- `account_id` (Primary Key)
- `document_number` (Unique, 11 digits)

### Operation Types Table
- `operation_type_id` (Primary Key)
- `description` (Operation description)

### Transactions Table
- `transaction_id` (Primary Key)
- `account_id` (Foreign Key)
- `operation_type_id` (Foreign Key)
- `amount` (Decimal with business logic)
- `event_date` (Timestamp)

## 🔧 Configuration

### Application Properties
- Server port: 8080
- Database: H2 in-memory
- JPA: Hibernate with DDL auto-create
- Logging: SQL queries enabled in development

### Environment Variables
- `SPRING_PROFILES_ACTIVE`: Active profile (default: default)
- `SERVER_PORT`: Server port (default: 8080)

## 🐳 Docker Support

### Build and Run with Docker
```bash
# Build the image
docker build -t visa-payment-service .

# Run the container
docker run -p 8080:8080 visa-payment-service
```

### Docker Compose
```bash
# Start all services (app + PostgreSQL)
docker-compose up --build

# Stop all services
docker-compose down
```

## 📈 Monitoring

### Actuator Endpoints
- `/actuator/health` - Health check
- `/actuator/info` - Application info
- `/actuator/metrics` - Application metrics

## 🔒 Security

- Input validation on all endpoints
- SQL injection protection via JPA
- XSS protection via Spring Security
- CORS configuration

## 📝 API Examples

### Create Account
```bash
curl -X POST http://localhost:8080/accounts \
  -H "Content-Type: application/json" \
  -d '{"document_number": "12345678900"}'
```

### Get Account
```bash
curl http://localhost:8080/accounts/1
```

### Create Transaction
```bash
curl -X POST http://localhost:8080/transactions \
  -H "Content-Type: application/json" \
  -d '{
    "account_id": 1,
    "operation_type_id": 1,
    "amount": 50.00
  }'
```

## 🚨 Error Handling

The service provides comprehensive error handling:
- **400 Bad Request**: Invalid input data
- **404 Not Found**: Resource not found
- **500 Internal Server Error**: Server errors

## 📚 Documentation

- **Swagger UI**: http://localhost:8080/swagger-ui.html
- **OpenAPI Spec**: http://localhost:8080/api-docs

## 🤝 Contributing

1. Fork the repository
2. Create a feature branch
3. Make your changes
4. Add tests
5. Submit a pull request

## 📄 License

This project is licensed under the MIT License.

## 🆘 Support

For questions or issues, please contact the development team.

---

**Built with ❤️ for Visa Payment Processing**
