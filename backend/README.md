# backend

## Installation guide

In order to install a SmartHut.sm, you can use *Docker* and *Docker Compose*
in order to create che corresponding containers.

Use the following `docker-compose.yml` example file. Change the values
of `$PASSWORD` and `$SECRET` to respectively the chosen PostgreSQL password
and the JWT secret used to run the server. `$SECRET` must be at least 64 chars long.

```yaml
version:            '2.1'

services:
  smarthutdb:
    restart:             always
    image:               postgres:12-alpine
    container_name:      smarthutdb
    volumes:
      - ./data:/var/lib/postgresql/data
    environment:
      PGDATA:            /var/lib/postgresql/data/data
      POSTGRES_DB:       smarthut
      POSTGRES_USERNAME: postgres
      POSTGRES_PASSWORD: $PASSWORD
    healthcheck:
      test: ["CMD-SHELL", "pg_isready -U postgres"]
      interval: 5s
      timeout: 5s
      retries: 30

  smarthutbackend:
    restart:             always
    image:               smarthutsm/smarthut-backend:M1
    depends_on:
      smarthutdb:
        condition: service_healthy
    ports:
      - 8080:8080
    environment:
      - POSTGRES_JDBC=jdbc:postgresql://smarthutdb:5432/smarthut
      - POSTGRES_USER=postgres
      - POSTGRES_PASS=$PASSWORD
      - SECRET=$SECRET
      - MAIL_HOST=smtp.gmail.com
      - MAIL_PORT=587
      - MAIL_STARTTLS=true
      - MAIL_USER=smarthut.sm@gmail.com
      - MAIL_PASS=dcadvbagqfkwbfts
      - BACKEND_URL=http://localhost:8080
      - FRONTEND_URL=http://localhost

  smarthut:
    restart:             always
    image:               smarthutsm/smarthut:M1
    ports:
      - 80:80
    environment:
      - BACKEND_URL=http://localhost:8080
```
