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