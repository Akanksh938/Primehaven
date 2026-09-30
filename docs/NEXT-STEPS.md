# Next steps: authentication and production readiness

This starter deliberately runs without authentication so you can verify every service first. Add an `auth-service` on port 8084 next.

## Auth service API

```
POST /api/auth/register  { name, email, password, role }
POST /api/auth/login     { email, password }
```

Store users in a separate `primehaven_auth.users` table. Hash passwords with Spring Security's `BCryptPasswordEncoder`; never store plain passwords. Login returns a signed JWT with `sub` (user id) and `role` claims.

Then add a JWT filter at the API Gateway. It should allow unauthenticated property browsing and reject protected write requests without `Authorization: Bearer <token>`. Services should receive the user id and role as trusted headers added by the gateway, or validate the same JWT independently.

Before production, replace `ddl-auto: update` with Flyway migration scripts, move every password/JWT signing key to environment variables or a secrets manager, add request error handling, and test database access with Testcontainers.

## Sample records

Use this after starting the gateway:

```json
POST http://localhost:8080/api/properties
{
  "title": "Modern 3 BHK Apartment",
  "description": "Bright apartment near the metro.",
  "price": 8500000,
  "type": "APARTMENT",
  "status": "AVAILABLE",
  "address": "12 Palm Avenue",
  "city": "Bengaluru",
  "bedrooms": 3,
  "bathrooms": 2,
  "areaSqFt": 1450,
  "imageUrl": "https://placehold.co/640x400?text=3+BHK",
  "ownerId": 1
}
```
