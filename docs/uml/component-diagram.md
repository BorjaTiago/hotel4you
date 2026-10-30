# H4Y-9 - Component Diagram

This diagram shows the main blocks of the application and the dependencies between them. Read [component-guide.md](component-guide.md) to explain the choices in class.

Sources: [statement](../../.agent/context/enunciado.md) sections 2 and 3, [requirements](../requirements/requirements.md), [class diagram](class-diagram.md), [classes](classes.md) and [schema](../database/schema.md). Package names come from H4Y-32 (project skeleton).

- **Requirements:** RF-28; RNF-02, RNF-05, RNF-06, RNF-07, RNF-08, RNF-10, RNF-11, RNF-12.

## Reading rules

The diagram reads from left to right, from input to output, as in the class 1 slides (Moodle).

- Left side, inputs: the actors use `HotelUI`, and the supplier XML stock file comes in.
- Middle: the parts of the subsystem, in call order (`gui`, `service`, `data`).
- Right side, output: `data` stores the data in SQL Server.
- The big box `«subsystem» Hotel4You` is the structured classifier. Its inside is the internal structure.
- A box `«component» :name` inside the subsystem is a part. Each part is one Java package of our code.
- A small empty box is a port. Each part has its own ports. Mermaid cannot put a box on a border, so the ports of the subsystem are the small boxes next to its border.
- A `○` is a provided interface (ball).
- A `(` is a required interface (socket). Mermaid has no socket shape, so the socket is a text character.
- A `(○` with a name is an assembly connector (ball and socket) between two parts.
- The line from a port of the subsystem to the port of a part is a delegation connector.
- A dashed arrow from a socket to a ball is a dependency. The `SQL` arrow goes from the subsystem to SQL Server.
- A solid arrow shows the supplier XML file going in through a port. `service` (`StockService`) reads it.
- A box with `«external»` is outside our code. SQL Server is an external component. The supplier XML file is an external artifact.
- Libraries (JavaFX, JUnit) and the JDBC driver are not components. They are not in the diagram. See [libraries.md](../libraries.md).
- A dashed arrow to `:domain` is a dependency. `gui`, `service` and `data` use the `domain` types.
- `config` creates all parts at start-up. These links are wiring, not dependencies, so they are not drawn.

```mermaid
%%{init: {"theme":"neutral","fontFamily":"Arial","flowchart":{"padding":6,"nodeSpacing":28,"rankSpacing":22}}}%%
flowchart LR
    BUI@{ shape: text, label: "○" }
    XML["«external» «artifact»<br/>Supplier XML stock file"]
    subgraph H4Y["«subsystem» Hotel4You : internal structure"]
        direction LR
        PUI[" "]
        BGUI@{ shape: text, label: "○" }
        PG1[" "]
        GUI["«component»<br/>:gui"]
        PG2[" "]
        AFAC@{ shape: text, label: "HotelFacade<br/>(○" }
        PS1[" "]
        SVC["«component»<br/>:service"]
        PS2[" "]
        AREPO@{ shape: text, label: "Repositories<br/>(○" }
        PD1[" "]
        DATA["«component»<br/>:data"]
        PD2[" "]
        PD3[" "]
        ACFG@{ shape: text, label: "AppConfig<br/>(○" }
        PC1[" "]
        CFG["«component»<br/>:config"]
        DOM["«component»<br/>:domain"]
        SDB@{ shape: text, label: "(" }
        PDB[" "]
        PXML[" "]
        PS3[" "]
    end
    RSQL@{ shape: text, label: "(" }
    BSQL@{ shape: text, label: "○" }
    PSQL[" "]
    SQL["«external» «component»<br/>SQL Server"]

    BUI ---|HotelUI| PUI
    PUI --- BGUI --- PG1 --- GUI --- PG2 --- AFAC --- PS1 --- SVC --- PS2 --- AREPO --- PD1 --- DATA --- PD2 --- SDB --- PDB --- RSQL
    RSQL -.->|SQL| BSQL
    BSQL --- PSQL --- SQL
    DATA --- PD3 --- ACFG --- PC1 --- CFG
    XML --> PXML --> PS3 --> SVC
    GUI -.-> DOM
    SVC -.-> DOM
    DATA -.-> DOM

    classDef port fill:#fff,stroke:#333,stroke-width:1px
    class PUI,PG1,PG2,PS1,PS2,PS3,PD1,PD2,PD3,PC1,PDB,PXML,PSQL port
    classDef symbol font-size:26px,font-weight:bold
    class BUI,BGUI,BSQL,AFAC,AREPO,ACFG,SDB,RSQL symbol
```

## Components

| Component (package) | Content | Provides | Uses | Requirement IDs |
|---------------------|---------|----------|------|-----------------|
| `gui` | JavaFX controllers from the class diagram (`LoginController`, `RoomController`, `ReservationController` and the others) and their views. | `HotelUI` (screens for the actors), through the subsystem port. | `HotelFacade`, `domain` (read only). | RNF-06, RNF-07 |
| `service` | `HotelFacade` and the services (`AuthService`, `RoomService`, `ReservationService`, `CardAccessService`, `StockService` and the others). All business rules. | `HotelFacade`. | `domain`, the repository interfaces in `data`, the supplier XML file (through a port). | RNF-07, RNF-08 |
| `domain` | Domain classes and enums (`User`, `Room`, `Reservation`, `MagneticCard`, `StockItem`, `Role` and the others). No SQL and no JavaFX. | Data types for the other packages. | None. | RNF-07, RNF-08 |
| `data` | Repository interfaces, for example `UserRepository`, and the JDBC version for SQL Server (`JdbcUserRepository`, `Database`). `Database` gets `AppConfig` and opens the connections. The JDBC version maps table rows to domain objects. Unit tests use an in-memory repository in `src/test`. | The repository interfaces. | `domain`, `AppConfig`, `SQL` of SQL Server (through a port). | RNF-01, RNF-10, RNF-12 |
| `config` | Reads the settings from the `application.properties` file (`AppConfig`). Starts the application: creates the repositories and services and gives `HotelFacade` to `gui`. The start-up links are not drawn. | `AppConfig`. | `gui`, `service`, `data` (start-up only). | RNF-08, RNF-12 |

## Interfaces

| Interface | Provided by | Required by | Requirement IDs |
|-----------|-------------|-------------|-----------------|
| `HotelFacade` | `service` | `gui` | RNF-07 |
| Repositories (`UserRepository` and one for each other aggregate) | Declared in `data`. `JdbcUserRepository` is the SQL Server version. | `service` | RNF-08, RNF-10, RNF-12 |
| `AppConfig` | `config` | `data` (`Database` reads it) | RNF-12 |
| `HotelUI` | `gui`, through the subsystem port | The actors | RNF-06 |
| `SQL` | SQL Server (external) | `data`, through the subsystem port | RNF-12 |

## External components

| Name | Type | Used by | Requirement IDs |
|------|------|---------|-----------------|
| SQL Server | External component. Provides `SQL`. | `data`, through a port | RNF-12 |
| Supplier XML stock file | External artifact (file) | `service` (`StockService`), through a port | RF-28 |

JavaFX, the Microsoft JDBC Driver for SQL Server, BCrypt (`at.favre.lib:bcrypt`) and JUnit are libraries. A library is not a component (teacher feedback, H4Y-9, 2026-10-17). They are listed with their reasons in [libraries.md](../libraries.md) and [component-guide.md](component-guide.md).

## Gaps

These gaps need team review. They are not approved requirements.

| ID | Gap | Effect and next step |
|----|-----|----------------------|
| C-01 | The class diagram had no repository classes (G-01 in the sequence guide). | Closed. `UserRepository`, `JdbcUserRepository` and `Database` exist in `data` and the class diagram shows them. |
| C-02 | The password hash method was open (H4Y-29). | Closed. BCrypt (`at.favre.lib:bcrypt`) is chosen. It is a library, not a component. It is in `docs/libraries.md`. |
| C-04 | There was no Java code (H4Y-32). | Closed for the classes that exist now. The five packages exist in the Java code. The class-to-diagram check is done (architecture review, 2026-10-17). |
| C-03 | The package of `HotelFacade` was not named in H4Y-32. | Closed. `HotelFacade` is in `service`, as in the Java code. |

Created by: jmbcruz. Updated by: jmbcruz. AI use: Cursor agent, Claude 2026-10-16; review fixes by Cursor agent, Claude, 2026-10-16; docs-updater, Claude Code (claude-haiku-5-5), 2026-10-16; doc-updater, Claude Code (claude-haiku-5-5), 2026-10-17; doc-updater, Claude Code (claude-sonnet-5-5), 2026-10-17; teacher feedback fixes by jmbcruz with Claude Code, 2026-10-21; left-to-right layout fix by jmbcruz, 2026-10-23; merge with main by Claude Code (claude-opus-5-5), 2026-10-23; slide match by Claude Code (claude-opus-5-5), 2026-10-30. Reviewed by: pending.
