# Visa Payment Service - Monorepo

This repository contains two reference implementations of a simple Visa Payment Service:

- `visa-payment-go`: Go (Gin) + SQLite
- `visa-payment-java`: Java (Spring Boot) + H2 (in-memory)

Both implementations expose similar REST APIs for Accounts and Transactions, with the same business rules and test flows.

## Repository Structure

- `visa-payment-go/`
  - Go service, Swagger UI at `/swagger/index.html`
  - SQLite database file `payment.db` (created on first run)
  - Postman collection: `Visa-Payment-Service-Go.postman_collection.json`
  - Quick run script: `./run`
- `visa-payment-java/`
  - Spring Boot service, Swagger UI at `/swagger-ui.html`
  - H2 console at `/h2-console` (in-memory DB)
  - Postman collection: `Visa_Payment_Service_Java.postman_collection.json`
  - Quick run script: `./run`

## Quick Start

Prerequisites:
- Go 1.21+ (for the Go service)
- JDK 17+ and Maven (for the Java service)
- Postman (optional, for manual testing)

### Start the Go Service

```bash
cd visa-payment-go
./run
```

Service runs on `http://localhost:8080`.

- Swagger: `http://localhost:8080/swagger/index.html`
- Health: `http://localhost:8080/health`

SQLite database file: `visa-payment-go/payment.db`.

### Start the Java Service

```bash
cd visa-payment-java
./run
```

Service runs on `http://localhost:8080`.

- Swagger: `http://localhost:8080/swagger-ui.html`
- Health (Actuator): `http://localhost:8080/actuator/health`
- H2 console: `http://localhost:8080/h2-console` (JDBC URL: `jdbc:h2:mem:testdb`, user: `sa`, no password)

## API Overview (both services)

- Accounts
  - `POST /accounts` – create account
  - `GET /accounts/{id}` – get account by id
- Transactions
  - `POST /transactions` – create transaction

Business rules:
- Purchases and withdrawals are stored as negative amounts
- Credit vouchers are stored as positive amounts
- Document numbers must be exactly 11 digits
- Request amounts must be positive; sign is derived by operation type

## Testing with Postman

Import the collections in Postman:
- Go: `visa-payment-go/Visa-Payment-Service-Go.postman_collection.json`
- Java: `visa-payment-java/Visa_Payment_Service_Java.postman_collection.json`

Each collection includes a recommended sequence: health check, create account, get account, and create transactions (normal purchase, credit voucher, withdrawal, purchase with installments), plus validation/error cases.

## Troubleshooting

- Port already in use: ensure nothing else is bound to `8080`.
- Go deps: `go mod download && go mod tidy`.
- Go build: `go clean && go build`.
- Java deps: `mvn dependency:resolve`.
- Java build: `mvn clean compile`.

## Notes and Differences

- Endpoint prefix:
  - Go uses `/api/v1/...` (e.g., `/api/v1/accounts`)
  - Java uses `/accounts` without version prefix
- Storage:
  - Go persists to `SQLite` (file)
  - Java uses `H2` in-memory (volatile)

## License

For tech-study use. Adapt as needed.


