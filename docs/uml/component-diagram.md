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
- A small box labelled `port` is a port. A port sits on the border of a subsystem or an external component.
- A circle is a provided interface (ball). A solid line joins the circle to the component that provides it.
- A dashed arrow labelled `requires` goes to the interface that a component needs. Mermaid cannot draw the socket, so this arrow is the socket. A required interface and a provided interface together are one assembly connector (ball and socket).
- A solid arrow labelled `delegation` is a delegation connector. It joins a port to the part that does the work.
- A solid arrow labelled `input` shows the supplier XML file going in through a port. `service` (`StockService`) reads it.
- A box with `«external»` is outside our code. SQL Server is an external component. The supplier XML file is an external artifact.
- Libraries (JavaFX, JUnit) and the JDBC driver are not components. They are not in the diagram. See [libraries.md](../libraries.md).
- A dashed arrow labelled `uses` is a dependency. `gui`, `service` and `data` use the `domain` types.
- `config` creates all parts at start-up. These links are wiring, not dependencies, so they are not drawn.

```mermaid
%%{init: {"theme":"neutral","fontFamily":"Arial"}}%%
flowchart LR
    UI(("HotelUI"))
    XML["«external» «artifact»<br/>Supplier XML stock file"]
    subgraph H4Y["«subsystem» Hotel4You"]
        direction LR
        PUI["port"]
        PXML["port"]
        GUI["«component»<br/>:gui"]
        FAC(("HotelFacade"))
        SVC["«component»<br/>:service"]
        DOM["«component»<br/>:domain"]
        REPO(("Repositories"))
        DATA["«component»<br/>:data"]
        CFGI(("AppConfig"))
        CFG["«component»<br/>:config"]
        PDB["port"]
    end
    SQLI(("SQL"))
    subgraph DBS["«external» SQL Server"]
        direction LR
        PSQL["port"]
        DBE["«component»<br/>:database"]
    end

    UI --- PUI
    PUI -->|delegation| GUI
    GUI -.->|requires| FAC
    FAC --- SVC
    SVC -.->|requires| REPO
    REPO --- DATA
    DATA -.->|requires| CFGI
    CFGI --- CFG
    SVC -.->|uses| DOM
    GUI -.->|uses| DOM
    DATA -.->|uses| DOM
    DATA -->|delegation| PDB
    PDB -.->|requires| SQLI
    SQLI --- PSQL
    PSQL -->|delegation| DBE
    XML -->|input| PXML
    PXML -->|delegation| SVC

    classDef port fill:#fff,stroke:#333,stroke-width:1px
    class PUI,PDB,PXML,PSQL port
```

## Components

| Component (package) | Content | Provides | Uses | Requirement IDs |
|---------------------|---------|----------|------|-----------------|
| `gui` | JavaFX controllers from the class diagram (`LoginController`, `RoomController`, `ReservationController` and the others) and their views. | `HotelUI` (screens for the actors), through the subsystem port. | `HotelFacade`, `domain` (read only). | RNF-06, RNF-07 |
| `service` | `HotelFacade` and the services (`AuthService`, `RoomService`, `ReservationService`, `CardAccessService`, `StockService` and the others). All business rules. | `HotelFacade`. | `domain`, the repository interfaces in `data`, the supplier XML file (through a port). | RNF-07, RNF-08 |
| `domain` | Domain classes and enums (`User`, `Room`, `Reservation`, `MagneticCard`, `StockItem`, `Role` and the others). No SQL and no JavaFX. | Data types for the other packages. | None. | RNF-07, RNF-08 |
| `data` | Repository interfaces, including `UserRepository` and `RoomRepository`, and the JDBC versions for SQL Server (`JdbcUserRepository`, `JdbcRoomRepository`, `Database`). `Database` gets `AppConfig` and opens the connections. The JDBC versions map table rows to domain objects. Unit tests use in-memory repositories in `src/test`. | The repository interfaces. | `domain`, `AppConfig`, `SQL` of SQL Server (through a port). | RF-04, RNF-01, RNF-10, RNF-12 |
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

Created by: jmbcruz. Updated by: pedrocostaisep. AI use: Cursor agent, Claude 2026-10-16; review fixes by Cursor agent, Claude, 2026-10-16; docs-updater, Claude Code (claude-haiku-5-5), 2026-10-16; doc-updater, Claude Code (claude-haiku-5-5), 2026-10-17; doc-updater, Claude Code (claude-sonnet-5-5), 2026-10-17; teacher feedback fixes by jmbcruz with Claude Code, 2026-10-21; left-to-right layout fix by jmbcruz, 2026-10-23; merge with main by Claude Code (claude-opus-5-5), 2026-10-23; H4Y-25 room registration, OpenAI Codex (GPT-6), 2026-10-27. Reviewed by: pending.
