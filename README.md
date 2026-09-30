# PrimeHaven starter workspace

This is a beginner-friendly starting point for the PrimeHaven real-estate platform. It is a Java 21 / Spring Boot backend and a React frontend.

## Before running

Install Java 21, Maven, MySQL 8 and Node.js LTS. Create these databases:

```sql
CREATE DATABASE primehaven_property;
CREATE DATABASE primehaven_visit;
CREATE DATABASE primehaven_inquiry;
```

Set the optional environment variables `PRIMEHAVEN_DB_USER` and `PRIMEHAVEN_DB_PASSWORD` if your MySQL credentials are not `root` / empty.

## Run order

1. Open `backend` in VS Code and run `mvn clean package`.
2. Start `eureka-server` (port 8761).
3. Start `property-service` (8081), `inquiry-service` (8083), and `visit-service` (8082).
4. Check that all services appear at http://localhost:8761.
5. Start `api-gateway` (8080).
6. From `frontend`, run `npm install` once, then `npm run dev`.

The frontend calls only the gateway at `http://localhost:8080`. See `docs/NEXT-STEPS.md` for the JWT/authentication milestone.
