# JWT Authentication with Quarkus

This project demonstrates a secure JWT-based authentication system built with Quarkus, featuring user registration and login functionality.

## Features

- User registration and authentication
- JWT token-based security
- H2 database integration
- OpenAPI documentation
- Health checks and metrics
- Docker support

## Prerequisites

- JDK 17 or later
- Maven 3.8.1 or later
- Docker (optional)

## Getting Started

### Running the application in dev mode

```bash
./mvnw compile quarkus:dev
```

### Running with Docker Compose

```bash
docker-compose up --build
```

## API Documentation

Once the application is running, you can access the OpenAPI documentation at:
- Swagger UI: http://localhost:8080/q/swagger-ui
- OpenAPI spec: http://localhost:8080/q/openapi

## Health and Metrics

- Health check endpoint: http://localhost:8080/q/health
- Metrics endpoint: http://localhost:8080/q/metrics

## API Endpoints

### Authentication

- POST `/auth/signup` - Register a new user
- POST `/auth/signin` - Authenticate and get JWT token

### Protected Resources

- GET `/api/protected` - Access protected resource (requires JWT token)

## Testing

Run the tests using:

```bash
./mvnw test
```

## Security

The application uses:
- JWT tokens for authentication
- Password hashing for secure storage
- Input validation
- Role-based access control

## Contributing

1. Fork the repository
2. Create a feature branch
3. Commit your changes
4. Push to the branch
5. Create a Pull Request
