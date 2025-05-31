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
- **Error**: 400 Bad Request (if validation fails)

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

```
{
  "timestamp": "2024-06-09T12:34:56.789",
  "status": 400,
  "error": "Transaction Error",
  "message": "Transaction not found"
}
```

- `status` will be 400 for transaction-related errors, 500 for internal server errors.
- `error` describes the type of error.
- `message` provides details about the error.
- `timestamp` is the time the error occurred.

### Example Error Responses

- **Transaction Not Found**
  - Status: 400
  - Message: `Transaction not found`

- **Internal Server Error**
  - Status: 500
  - Message: `Detailed error message`

### Exception Handling

The application uses a global exception handler to catch and return errors in a consistent, RESTful format. Transaction-related errors throw a `TransactionException` and are handled with a 400 status code. All other exceptions return a 500 status code. 