# Employee Management API

Enterprise Employee Management Backend API built with Java 21, Spring Boot 3, and PostgreSQL.

## Tech Stack

- **Java 21** + **Spring Boot 3.3**
- **PostgreSQL 16** with JPA/Hibernate
- **Maven** build tool
- **Swagger/OpenAPI** documentation
- **JUnit 5** + **Mockito** + **Testcontainers**
- **JaCoCo** code coverage
- **Docker** multi-stage build

## API Endpoints

### Employee APIs
| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | `/api/employees` | Create employee |
| GET | `/api/employees` | Get all employees |
| GET | `/api/employees/{id}` | Get employee by ID |
| PUT | `/api/employees/{id}` | Update employee |
| DELETE | `/api/employees/{id}` | Delete employee |

### Department APIs
| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | `/api/departments` | Create department |
| GET | `/api/departments` | Get all departments |
| GET | `/api/departments/{id}` | Get department by ID |
| PUT | `/api/departments/{id}` | Update department |
| DELETE | `/api/departments/{id}` | Delete department |

### Actuator & Docs
- Health: `/actuator/health`
- Metrics: `/actuator/metrics`
- Swagger UI: `/swagger-ui`

## Local Development

```bash
# Start PostgreSQL
docker-compose up -d postgres

# Run application
./mvnw spring-boot:run

# Run unit tests
./mvnw test

# Run integration tests (requires Docker)
./mvnw verify -Pfailsafe
```

## CI/CD Pipeline

GitHub Actions pipeline with 8 jobs:
1. **Build** - Compile application
2. **Unit Tests** - JUnit 5 + JaCoCo coverage
3. **SonarQube** - Code quality analysis
4. **Docker Build** - Multi-stage container build
5. **Trivy Scan** - Container security scanning
6. **Docker Push** - Push to registry
7. **Integration Tests** - Testcontainers + PostgreSQL
8. **Update GitOps** - Update deployment image tag

## Required GitHub Secrets

| Secret | Description |
|--------|-------------|
| `DOCKER_USERNAME` | Docker Hub username |
| `DOCKER_PASSWORD` | Docker Hub password |
| `SONAR_TOKEN` | SonarQube authentication token |
| `SONAR_HOST_URL` | SonarQube server URL |
| `GITOPS_TOKEN` | PAT with write access to GitOps repo |
