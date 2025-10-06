# 🧪 How to Test the Java Service

## 📋 Prerequisites

1. **Java service running** on port 8080
2. **Postman** installed
3. **Collection** imported

## 🚀 Steps to Test

### 1. Start the Java Service
```bash
cd visa-payment-java
./run
```

### 2. Import Collection into Postman
1. Open Postman
2. Click "Import"
3. Select the file `Visa-Payment-Service.postman_collection.json`
4. The collection will be imported with all endpoints

### 3. Recommended Test Sequence

#### ✅ **Test 1: Health Check**
- Run: `Health Check`
- **Expected result**: Status 200, `{"status":"UP"}`

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

### H2 Database Console
1. Go to: http://localhost:8080/h2-console
2. **JDBC URL**: `jdbc:h2:mem:testdb`
3. **User Name**: `sa`
4. **Password**: (leave empty)
5. Click "Connect"

### SQL Queries to Verify
```sql
-- List all accounts
SELECT * FROM ACCOUNTS;

-- List all operation types
SELECT * FROM OPERATION_TYPES;

-- List all transactions
SELECT * FROM TRANSACTIONS;

-- List transactions with details
SELECT 
    t.TRANSACTION_ID,
    t.AMOUNT,
    t.EVENT_DATE,
    a.DOCUMENT_NUMBER,
    ot.DESCRIPTION
FROM TRANSACTIONS t
JOIN ACCOUNTS a ON t.ACCOUNT_ID = a.ACCOUNT_ID
JOIN OPERATION_TYPES ot ON t.OPERATION_TYPE_ID = ot.OPERATION_TYPE_ID;
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
- Go to: http://localhost:8080/swagger-ui.html
- Explore all endpoints interactively
- Test directly in the interface

## 🚨 Troubleshooting

### Service does not start
```bash
# Check if port 8080 is free
netstat -an | grep 8080

# Kill process on port 8080 (if needed)
# Windows: netstat -ano | findstr :8080
# Linux/Mac: lsof -ti:8080 | xargs kill -9
```

### Compilation error
```bash
# Clean and recompile
mvn clean compile
```

### Dependency error
```bash
# Resolve dependencies
mvn dependency:resolve
```

## 📝 Service Logs

The Java service prints detailed logs to the console:
- **Startup**: Table creation
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
- [ ] Access H2 console
- [ ] Access Swagger UI

---

**🎉 Congratulations! If all tests passed, the Java service is working perfectly!**
