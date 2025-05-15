# RewardCalucatorSystem

Spring boot application to calculate the points on their transactions.

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

Example:

GET /api/rewards/1?from=2025-01-01&to=2025-04-01

- if FROM date is not provided, application will consider the last 3 months from TO date if provided
- FROM date cannot be after TO date

### Tech Stack
- Java 17
- Spring Boot 3.3.11
- Spring Web
- Spring Data JPA
- H2 Database (in-memory for demo)
- JUnit 5, Mockito (for testing)
- Maven


