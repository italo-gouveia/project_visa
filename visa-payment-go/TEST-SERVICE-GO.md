# 🧪 How to Test the Go Service

## 📋 Prerequisites

1. **Go 1.21+** installed
2. **Go service running** on port 8080
3. **Postman** installed
4. **Collection** imported

## 🚀 Steps to Test

### 1. Start the Go Service
```bash
cd visa-payment-go
./run
```

### 2. Import Collection into Postman
1. Open Postman
2. Click "Import"
3. Select the file `Visa-Payment-Service-Go.postman_collection.json`
4. The collection will be imported with all endpoints

### 3. Recommended Test Sequence

#### ✅ **Test 1: Health Check**
- Run: `Health Check`
- **Expected result**: Status 200, `{"status":"ok"}`

#### ✅ **Test 2: Create Account**
- Run: `Create Account - Success`
- **Expected result**: Status 201, `{"account_id":1,"document_number":"12345678900"}`
- **Write down the returned account_id**

#### ✅ **Test 3: Get Account**
- Run: `Get Account - Success`
- **Expected result**: Status 200, account data

#### ✅ **Test 4: Create Transaction - Normal Purchase**
- Run: `Create Transaction - Normal Purchase`
- **Expected result**: Status 201, `amount: -50.00` (negative)

#### ✅ **Test 5: Create Transaction - Credit Voucher**
- Run: `Create Transaction - Credit Voucher`
- **Expected result**: Status 201, `amount: 25.00` (positive)

#### ✅ **Test 6: Create Transaction - Withdrawal**
- Run: `Create Transaction - Withdrawal`
- **Expected result**: Status 201, `amount: -100.00` (negative)

#### ✅ **Test 7: Create Transaction - Purchase with Installments**
- Run: `Create Transaction - Purchase with Installments`
- **Expected result**: Status 201, `amount: -200.00` (negative)

### 4. Validation Tests (Should Fail)

#### ❌ **Test 8: Invalid Document**
- Run: `Create Account - Invalid Document`
- **Expected result**: Status 400

#### ❌ **Test 9: Duplicate Document**
- Run: `Create Account - Duplicate Document`
- **Expected result**: Status 400

#### ❌ **Test 10: Nonexistent Account**
- Run: `Get Account - Not Found`
- **Expected result**: Status 404

#### ❌ **Test 11: Transaction with Nonexistent Account**
- Run: `Create Transaction - Invalid Account`
- **Expected result**: Status 400

#### ❌ **Test 12: Transaction with Invalid Operation Type**
- Run: `Create Transaction - Invalid Operation Type`
- **Expected result**: Status 400

#### ❌ **Test 13: Transaction with Negative Amount**
- Run: `Create Transaction - Negative Amount`
- **Expected result**: Status 400

## 🔍 Data Verification

### SQLite Database
The SQLite database is created automatically as `payment.db` in the project directory.

### SQL Queries to Verify
```sql
-- List all accounts
SELECT * FROM accounts;

-- List all operation types
SELECT * FROM operation_types;

-- List all transactions
SELECT * FROM transactions;

-- List transactions with details
SELECT 
    t.id,
    t.amount,
    t.event_date,
    a.document_number,
    ot.description
FROM transactions t
JOIN accounts a ON t.account_id = a.id
JOIN operation_types ot ON t.operation_type_id = ot.id;
```

## 📊 Expected Results

### Initialized Operation Types
- **ID 1**: Normal Purchase
- **ID 2**: Purchase with installments  
- **ID 3**: Withdrawal
- **ID 4**: Credit Voucher

### Business Rules
- **Purchases and Withdrawals**: Negative values
- **Credit Vouchers**: Positive values
- **Documents**: Exactly 11 digits
- **Amounts**: Always positive in the request

## 🎯 API Documentation

### Swagger UI
- Go to: http://localhost:8080/swagger/index.html
- Explore all endpoints interactively
- Test directly in the interface

## 🚨 Troubleshooting

### Service does not start
```bash
# Check if port 8080 is free
netstat -an | grep 8080

# Check if Go is installed
go version
```

### Dependency error
```bash
# Download dependencies
go mod download

# Verify dependencies
go mod tidy
```

### Compilation error
```bash
# Clean and rebuild
go clean
go build
```

## 📝 Service Logs

The Go service prints logs to the console:
- **Startup**: Database and tables creation
- **Operations**: Data insertion
- **Errors**: Validations and exceptions

## ✅ Test Checklist

- [ ] Health check working
- [ ] Account creation with valid document
- [ ] Get account by ID
- [ ] Create purchase transaction (negative amount)
- [ ] Create voucher transaction (positive amount)
- [ ] Create withdrawal transaction (negative amount)
- [ ] Invalid document validation
- [ ] Duplicate document validation
- [ ] Nonexistent account validation
- [ ] Invalid operation type validation
- [ ] Negative amount validation
- [ ] Access Swagger UI

## 🔄 Differences from the Java Service

### Endpoint URLs
- **Go**: `/api/v1/accounts` (with version prefix)
- **Java**: `/accounts` (no prefix)

### Database
- **Go**: SQLite (persistent file)
- **Java**: H2 in-memory (volatile)

### Performance
- **Go**: Faster startup (~100ms)
- **Java**: Slower startup (~3-5s)

---

**🎉 Congratulations! If all tests passed, the Go service is working perfectly!**
