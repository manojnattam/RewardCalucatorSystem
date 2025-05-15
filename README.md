# RewardCalucatorSystem

Spring boot application to calculate the points on their transactions.

## Rules 
- 1 point for every dollar spent over $50
- 2 points for every dollar spent over $100
  Example: $120 → 2×20 + 1×50 = 90 points

## Rest API
### GET /api/rewards/{customerId}

Optional Query Params:
- from: yyyy-MM-dd
- to: yyyy-MM-dd

Example:

GET /api/rewards/1?from=2025-01-01&to=2025-04-01

- if FROM date is not provided application will consider the date of last 3 months from TO date if provided
- FROM date cannot be after TO date
- 


