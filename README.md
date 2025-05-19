# RewardCalucatorSystem

Spring boot application to calculate the reward points on their transactions.

## Problem description
A retailer offers a rewards program to its customers, awarding points based on each recorded purchase.

A customer receives 2 points for every dollar spent over $100 in each transaction, plus 1 point for every dollar spent between $50 and $100 in each transaction.

#### Example: $120 → 2×20 + 1×50 = 90 points
- 1 point for every dollar spent over $50
- 2 points for every dollar spent over $100
  

## Rest API
### GET /api/rewards/{customerId}

Optional Query Params:
- fromDate: yyyy-MM-dd
- toDate: yyyy-MM-dd

### Conditions and Assumptions
- Dates must follow ISO format: yyyy-MM-dd (e.g., 2025-05-14).
- fromDate must not be after toDate
- Neither fromDate nor toDate can be in the future
- Both fromDate and toDate are optional query parameters
  - if both are null, application will take the today date as toDate and (today - 3 months) as fromDate
  - if fromDate is not provided, fromDate is (ToDate - 3 months)
  - if toDate is not provided, toDate is today

#### Example:
##### Request:
```
GET http://localhost:8080/api/rewards/1?fromDate=2025-03-15&toDate=2025-05-14
```
##### Response:
```
{
    "customerId": 1,
    "customerName": "Ram",
    "fromDate": "2025-03-15",
    "toDate": "2025-05-14",
    "monthlyRewards": {
        "APRIL-2025": 90,
        "MARCH-2025": 25
    },
    "totalRewards": 115,
    "transactions": [
    {
        "transactionId": 1,
        "amount": 120.0,
        "date": "2025-04-14"
    },
    {
        "transactionId": 2,
        "amount": 75.0,
        "date": "2025-03-25"
    },
    {
       "transactionId": 3,
        "amount": 45.0,
        "date": "2025-04-25"
    }
    ]
}
```

### Tech Stack
- Java 21
- Spring Boot 3.3.11
- H2 Database (in-memory for demo)
- JUnit 5, Mockito (for testing)
- Maven


