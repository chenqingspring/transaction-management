# API Documentation

## Endpoints

### Create Transaction

- **URL**: `POST /transactions`
- **Request Body**:
  ```json
  {
    "amount": 100.00,
    "type": "DEPOSIT",
    "description": "Test deposit",
    "category": "Test"
  }
  ```
- **Response**: 200 OK
  ```json
  {
    "id": "uuid",
    "amount": 100.00,
    "type": "DEPOSIT",
    "description": "Test deposit",
    "category": "Test",
    "timestamp": "2023-01-01T00:00:00Z"
  }
  ```
- **Error**: 400 Bad Request
  ```json
  {
    "timestamp": "2024-03-21T10:30:00",
    "status": 400,
    "error": "Transaction Error",
    "errorType": "INVALID_AMOUNT",
    "message": "Invalid transaction amount"
  }
  ```

### Get Transaction

- **URL**: `GET /transactions/{id}`
- **Response**: 200 OK
  ```json
  {
    "id": "uuid",
    "amount": 100.00,
    "type": "DEPOSIT",
    "description": "Test deposit",
    "category": "Test",
    "timestamp": "2023-01-01T00:00:00Z"
  }
  ```
- **Error**: 404 Not Found (if transaction not found)

### List Transactions

- **URL**: `GET /transactions?page=0&size=10`
- **Response**: 200 OK
  ```json
  [
    {
      "id": "uuid1",
      "amount": 100.00,
      "type": "DEPOSIT",
      "description": "Test deposit 1",
      "category": "Test",
      "timestamp": "2023-01-01T00:00:00Z"
    },
    {
      "id": "uuid2",
      "amount": 200.00,
      "type": "WITHDRAWAL",
      "description": "Test withdrawal",
      "category": "Test",
      "timestamp": "2023-01-01T00:00:00Z"
    }
  ]
  ```

### Update Transaction

- **URL**: `PUT /transactions/{id}`
- **Request Body**:
  ```json
  {
    "amount": 150.00,
    "type": "DEPOSIT",
    "description": "Updated deposit",
    "category": "Test"
  }
  ```
- **Response**: 200 OK
  ```json
  {
    "id": "uuid",
    "amount": 150.00,
    "type": "DEPOSIT",
    "description": "Updated deposit",
    "category": "Test",
    "timestamp": "2023-01-01T00:00:00Z"
  }
  ```
- **Error**: 404 Not Found (if transaction not found)

### Delete Transaction

- **URL**: `DELETE /transactions/{id}`
- **Response**: 200 OK
- **Error**: 404 Not Found (if transaction not found)

## Error Handling

All API endpoints may return errors in the following JSON format:

```json
{
  "timestamp": "2024-03-21T10:30:00",
  "status": 400,
  "error": "Transaction Error",
  "errorType": "ERROR_TYPE",
  "message": "Detailed error message"
}
```

### Error Types

| Error Type | Description | HTTP Status |
|------------|-------------|-------------|
| INVALID_AMOUNT | Transaction amount is missing or invalid | 400 |
| INVALID_TYPE | Transaction type is missing or invalid | 400 |
| MISSING_DESCRIPTION | Transaction description is required | 400 |
| MISSING_CATEGORY | Transaction category is required | 400 |
| NEGATIVE_AMOUNT | Transaction amount cannot be negative | 400 |
| ZERO_AMOUNT | Transaction amount cannot be zero | 400 |

### Validation Rules

1. **Amount**:
   - Required
   - Must be greater than 0
   - Cannot be negative
   - Cannot be zero
   - Must be a valid decimal number

2. **Type**:
   - Required
   - Must be one of: DEPOSIT, WITHDRAWAL, TRANSFER
   - Case sensitive

3. **Description**:
   - Required
   - Cannot be empty or null
   - Cannot contain only whitespace

4. **Category**:
   - Required
   - Cannot be empty or null
   - Cannot contain only whitespace

### Example Error Responses

1. **Invalid Amount**:
```json
{
  "timestamp": "2024-03-21T10:30:00",
  "status": 400,
  "error": "Transaction Error",
  "errorType": "INVALID_AMOUNT",
  "message": "Invalid transaction amount"
}
```

2. **Missing Description**:
```json
{
  "timestamp": "2024-03-21T10:30:00",
  "status": 400,
  "error": "Transaction Error",
  "errorType": "MISSING_DESCRIPTION",
  "message": "Transaction description is required"
}
```

3. **Invalid Type**:
```json
{
  "timestamp": "2024-03-21T10:30:00",
  "status": 400,
  "error": "Transaction Error",
  "errorType": "INVALID_TYPE",
  "message": "Invalid transaction type"
}
```

### Field Validation Errors

When multiple fields fail validation, the response will include all field errors:

```json
{
  "timestamp": "2024-03-21T10:30:00",
  "status": 400,
  "error": "Validation Error",
  "fieldErrors": {
    "amount": "Amount must be greater than 0",
    "description": "Description is required",
    "category": "Category is required"
  }
}
``` 