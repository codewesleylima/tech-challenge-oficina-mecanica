# Oficina Mecânica - Gestão de Serviços

This project have how main objective to provide a automotive service management system that allows the
for the client to follow in real time the progress of the maintenance of his vehicle, authorize additional repairs
and receive notifications about the status of the service. As well as, provide to the mechanics an interface to
update the status of the services, register additional repairs and communicate with the clients efficiently.

This project born from a real need of auto repair shopt that was working in a disorganized way, operating with manual notes,
generating errors of prioritization, loss of history and inefficiency in the flow of budgets. 
Our solution aims to digitalize all this process.

## Principal Features
- Client identification by CPF/CNPJ
- Vehicle registration (license plate, brand, model, year)
- Inclusion of requested services (example: oil change, alignment)
- Possibility to include necessary parts and supplies
- Budget generated automatically based on services and parts
- Sending the budget to the client for approval
- Real-time updates on service progress
- Communication channel between mechanics and clients
- Notifications for clients about service status

## Tech Stack
- **Java 21** + **Spring Boot 3.2** (REST API, port `8080`)
- **PostgreSQL 15** (relational database, port `5432`)
- **Gradle** (build via wrapper `./gradlew`)
- **Docker** + **Docker Compose** (local orchestration)

## Requirements
- [Docker](https://docs.docker.com/get-docker/) and Docker Compose v2
- (Optional, only to run without containers) JDK 21

## Configuration
The application reads its database credentials and settings from environment
variables. A template is provided in `.env.example` — **never commit your real
`.env`** (it is already git-ignored).

```bash
cp .env.example .env      # Windows PowerShell: copy .env.example .env
```

Then edit `.env` and set a real value for `DB_PASSWORD` (and `JWT_SECRET` for
non-dev environments).

| Variable | Default | Description |
|---|---|---|
| `DB_NAME` | `oficina_db` | PostgreSQL database name |
| `DB_USER` | `oficina` | PostgreSQL user |
| `DB_PASSWORD` | — | PostgreSQL password (set your own) |
| `DB_PORT` | `5432` | Host port mapped to PostgreSQL |
| `SPRING_PROFILE` | `dev` | Active Spring profile |
| `SPRING_JPA_HIBERNATE_DDL_AUTO` | `none` | Hibernate DDL strategy |
| `JWT_SECRET` | dev default | JWT signing secret (override outside dev) |
| `JWT_EXPIRATION` | `3600` | JWT expiration in seconds |

## Running with Docker Compose
Docker Compose starts two services: `oficina-db` (PostgreSQL) and
`oficina-backend` (the API). The backend only starts after PostgreSQL reports
healthy, and connects to it over the internal `oficina-network`.

```bash
# Build images and start everything in the background
docker compose up --build -d

# Check the status of the containers
docker compose ps
```

When healthy, the API is available at `http://localhost:8080`.

### Database connection
Inside the Compose network the backend reaches the database by service name:

```
jdbc:postgresql://postgres:5432/oficina_db
```

From your host machine (e.g. a SQL client or `psql`) use `localhost`:

```
host=localhost  port=5432  db=oficina_db  user=oficina
```

These values come from the `SPRING_DATASOURCE_*` variables injected into the
backend container and can be overridden via your `.env`.

### Verifying the connection
```bash
# Database is accepting connections
docker compose exec postgres pg_isready -U oficina -d oficina_db

# Backend health (includes the database check)
curl -fsS http://localhost:8080/actuator/health     # -> {"status":"UP"}

# Open a psql session inside the database container
docker compose exec postgres psql -U oficina -d oficina_db
```

### Stopping
```bash
docker compose down          # stop and remove containers (keeps data volume)
docker compose down -v       # also remove the PostgreSQL data volume (wipes data)
```

## Running the API without containers (optional)
With a PostgreSQL instance reachable, point the datasource via environment
variables and start the app with the Gradle wrapper:

```bash
export SPRING_DATASOURCE_URL=jdbc:postgresql://localhost:5432/oficina_db
export SPRING_DATASOURCE_USERNAME=oficina
export SPRING_DATASOURCE_PASSWORD=your_password
./gradlew bootRun
```

API documentation (Swagger UI) is available at
`http://localhost:8080/swagger-ui.html` once the application is running.

## Team Members:

Thank you to the following people who contributed to this project:

<table>
  <tr>
    <td align="center">
      <a href="https://www.linkedin.com/in/wesslima/" title="Wesley Lima">
        <img src="https://media.licdn.com/dms/image/v2/D4D03AQGxzuIy-ANfNA/profile-displayphoto-crop_800_800/B4DZ7HT8V.GkAI-/0/1781460357892?e=1783555200&v=beta&t=iV0RLtZj1z9zgOntL3X6Y0CzY05dJeIL8VivX5fr3RA" width="100px;" alt="Foto do Wesley Lima"/><br>
        <sub>
          <b>Wesley Lima</b>
        </sub>
      </a>
      <br>
      <sub>Backend Engineer</sub>
    </td>
    <td align="center">
      <a href="https://www.linkedin.com/in/tim-morgenstern-4581911b1/" title="Tim Morgenstern">
        <img src="https://media.licdn.com/dms/image/v2/C4E03AQG57Du9tsCS5A/profile-displayphoto-shrink_800_800/profile-displayphoto-shrink_800_800/0/1643980434981?e=1782345600&v=beta&t=r2XlUXgFNRi3C0LKczF1AGjaMhCUQSYQcLx11Ilq_Yk" width="100px;" alt="Foto do Tim Morgenstern"/><br>
        <sub>
          <b>Tim Morgenstern</b>
        </sub>
      </a>
      <br>
      <sub>Backend Engineer</sub>
    </td>
    <td align="center">
      <a href="https://www.linkedin.com/in/matheus-pitas-baptista/" title="Matheus Pitas Baptista">
        <img src="https://media.licdn.com/dms/image/v2/D4D03AQHFS4VJk5WteA/profile-displayphoto-crop_800_800/B4DZv_LwbmHYAI-/0/1769512832027?e=1782345600&v=beta&t=yZNCtRAXZNCIw2ecvKaIVvtxjQy4dkoilGYparn1br0" width="100px;" alt="Foto do Matheus Pitas Baptista"/><br>
        <sub>
          <b>Matheus Pitas Baptista</b>
        </sub>
      </a>
      <br>
      <sub>Backend Engineer</sub>
    </td>
  </tr>
</table>

## Licence
This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.
