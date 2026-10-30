# hotel4you

Hotel4You is a desktop application to manage a hotel. ISEP course project.

## How to start the project

### 1. Install

- JDK 21.
- Git.
- You do not need to install Maven. Use the Maven Wrapper: `./mvnw` (on Windows: `mvnw.cmd`).
- Docker (optional, see step 3).

### 2. Create your settings file

```bash
cp application.properties.example application.properties
```

Open `application.properties` and write your own values. The file has one `key=value` line for each setting.
Git ignores this file, so your passwords stay on your PC. Never commit it.
Do not use the `$` character in any password. Docker Compose reads `$` as a variable, so the password changes and the database login fails.

| Key | What it is | Can I change it? |
|-----|------------|------------------|
| `MSSQL_SA_PASSWORD` | The SQL Server password. Docker uses it to create the server. | **Yes, write your own.** 8 or more characters, with upper case, lower case, a digit and a symbol. Set it **before** the first `docker compose up`. To change it later, run `docker compose down -v` first (this deletes the data). |
| `db.url` | The address of the database. | Only if SQL Server uses another port or host. The default works for Docker and for Express on port 1433. |
| `db.user` | The database user. | Keep `sa`. |
| `db.password` | The password the app uses to enter the database. | **Yes. It must have the same value as `MSSQL_SA_PASSWORD`.** |

Rules for this file:

- Write comments on their own line, starting with `#`. A comment at the end of a value becomes part of the value.
- Do not put spaces around `=` and do not use quotes.

Example:

```properties
# Used by Docker
MSSQL_SA_PASSWORD=MyStrong-Password1!

# Used by the app
db.url=jdbc:sqlserver://localhost:1433;databaseName=hotel4you;encrypt=true;trustServerCertificate=true;loginTimeout=5
db.user=sa
db.password=MyStrong-Password1!
```

### 3. Start SQL Server (choose A or B)

**A. With Docker** (Linux, Mac, Windows):

```bash
docker compose up -d
```

Wait about 20 seconds, until `docker compose ps` shows `healthy`.

**B. Without Docker** (Windows):

1. Install SQL Server 2022 Express.
2. Open SQL Server Configuration Manager. Enable **TCP/IP** on port **1433**.
3. Enable **SQL Server and Windows Authentication** (mixed mode) and set the `sa` password.
4. Restart the SQL Server service.
5. In `application.properties`, set `db.password` to your `sa` password.

### 4. Create the database

Run the scripts in `database/`, in number order. Run each one by hand. You can run them again without error.

In the commands below, replace `MyStrong-Password1!` with your own `MSSQL_SA_PASSWORD`.

With Docker:

```bash
docker compose exec db /opt/mssql-tools18/bin/sqlcmd -S localhost -U sa -P "MyStrong-Password1!" -C -b -I -i /database/v1-create-database.sql
docker compose exec db /opt/mssql-tools18/bin/sqlcmd -S localhost -U sa -P "MyStrong-Password1!" -C -b -I -i /database/v2-create-roles-users.sql
docker compose exec db /opt/mssql-tools18/bin/sqlcmd -S localhost -U sa -P "MyStrong-Password1!" -C -b -I -i /database/v3-insert-test-users.sql
docker compose exec db /opt/mssql-tools18/bin/sqlcmd -S localhost -U sa -P "MyStrong-Password1!" -C -b -I -i /database/v4-create-floors-locations.sql
docker compose exec db /opt/mssql-tools18/bin/sqlcmd -S localhost -U sa -P "MyStrong-Password1!" -C -b -I -i /database/v5-create-room-types.sql
docker compose exec db /opt/mssql-tools18/bin/sqlcmd -S localhost -U sa -P "MyStrong-Password1!" -C -b -I -i /database/v6-create-rooms.sql
```

Without Docker (`sqlcmd` comes with SQL Server, or open the files in SQL Server Management Studio and run them):

```bash
sqlcmd -S localhost -U sa -P "MyStrong-Password1!" -C -b -I -i database/v1-create-database.sql
sqlcmd -S localhost -U sa -P "MyStrong-Password1!" -C -b -I -i database/v2-create-roles-users.sql
sqlcmd -S localhost -U sa -P "MyStrong-Password1!" -C -b -I -i database/v3-insert-test-users.sql
sqlcmd -S localhost -U sa -P "MyStrong-Password1!" -C -b -I -i database/v4-create-floors-locations.sql
sqlcmd -S localhost -U sa -P "MyStrong-Password1!" -C -b -I -i database/v5-create-room-types.sql
sqlcmd -S localhost -U sa -P "MyStrong-Password1!" -C -b -I -i database/v6-create-rooms.sql
```

`v5-create-room-types.sql` from H4Y-26 creates `individual`, `duplo` and `familiar`. Run it before `v6-create-rooms.sql`. The manager then uses "Quartos" to register rooms. The separate "Pre?os" screen sets base prices. No room registration requires a base price to be set.

`v3-insert-test-users.sql` adds one test user for each staff role. Use it only on a local test database. The usernames and passwords are in the script header.

### 5. Run the application

```bash
./mvnw compile javafx:run
```

Log in with `gestor` / `1234` (created by `v3-insert-test-users.sql`, local tests only).

If SQL Server is not running, the app shows the message
`Base de dados indisponível. Verifique se o SQL Server está a funcionar.` (database not available).

### Stop Docker

```bash
docker compose down        # stop, keep the data
docker compose down -v     # stop and delete the data
```

## Build and test

```bash
./mvnw verify            # build and run the unit tests
./mvnw javadoc:javadoc   # build the Javadoc
```

More details and the build tool versions: [docs/setup.md](docs/setup.md).

## Documentation

### Requirements

- [actors.md](docs/requirements/actors.md): project actors
- [glossary.md](docs/requirements/glossary.md): project terms
- [questions.md](docs/requirements/questions.md): open questions for the teacher
- [requirements.md](docs/requirements/requirements.md): functional and non-functional requirements
- [use-cases.md](docs/requirements/use-cases.md): use cases

### UML

- [README.md](docs/uml/README.md): UML folder guide
- [class-diagram.md](docs/uml/class-diagram.md): class diagram
- [classes.md](docs/uml/classes.md): classes per layer
- [component-diagram.md](docs/uml/component-diagram.md): component diagram
- [component-guide.md](docs/uml/component-guide.md): notes on the component diagram
- [domain-model.md](docs/uml/domain-model.md): domain model (business concepts)
- [guide.md](docs/uml/guide.md): class design guide
- [sequence-diagrams.md](docs/uml/sequence-diagrams.md): sequence diagrams
- [sequence-guide.md](docs/uml/sequence-guide.md): notes on the sequence diagrams
- [traceability.md](docs/uml/traceability.md): requirements to classes and methods
- [use-case-catalog.md](docs/uml/use-case-catalog.md): use-case catalogue
- [use-cases.md](docs/uml/use-cases.md): use-case diagrams

### Database

- [erd.md](docs/database/erd.md): ER diagram
- [schema.md](docs/database/schema.md): database schema (SQL Server)
- [tables.md](docs/database/tables.md): table guide

### Other

- [setup.md](docs/setup.md): setup and build
- [libraries.md](docs/libraries.md): external libraries

Created by: BorjaTiago. Updated by: pedrocostaisep. AI use: Claude Code (claude-opus-5-5), 2026-10-23; H4Y-25 review corrections, OpenAI Codex (gpt-6.1-sol), 2026-10-29; statement room constants review, OpenAI Codex (gpt-6.1-sol), 2026-10-29; H4Y-25 entity and room type integration review, OpenAI Codex (gpt-6.1-sol), 2026-10-30. Reviewed by: pending.
