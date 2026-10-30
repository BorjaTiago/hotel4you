# H4Y-9 - Component Guide

Use this guide to defend the [component diagram](component-diagram.md). Classes come from [classes.md](classes.md) and [class-diagram.md](class-diagram.md). Package names come from H4Y-32.

## Main ideas

1. The layout follows the teacher's slide: one `«subsystem» Hotel4You` box with its internal structure. Each part inside is one Java package: `gui`, `service`, `domain`, `data`, `config`. The diagram reads from left to right, from input to output (class 1 slides): the actors and the supplier XML file on the left, SQL Server on the right.
2. Parts connect with assembly connectors (ball and socket). Ports on the border connect to parts with delegation connectors.
3. The GUI talks only to the business logic. `gui` calls `HotelFacade` and reads `domain` objects. It never uses `data` or SQL Server (RNF-07).
4. Services do not know SQL. They use the repository interfaces in `data` (T0, H4Y-32). `data` implements them with JDBC (`JdbcUserRepository`). Unit tests use an in-memory repository in `src/test`, not in the application (RNF-08, RNF-10, RNF-12).
5. Only `config` knows all packages. It creates and connects the objects at start-up. It holds no business rule.
6. Only external components and artifacts are in the diagram: SQL Server and the supplier XML file. Libraries and the JDBC driver are not components (teacher feedback, 2026-10-17). Each library has a reason from the statement (RNF-11).

## Done criteria of H4Y-9

| Criterion | How the diagram meets it |
|-----------|--------------------------|
| The interface communicates only with the business logic. | `gui` requires only `HotelFacade` and reads `domain`. `gui` has no connector to `data` or SQL Server. |
| Each component matches a package in the code. | The five components are the five packages planned in H4Y-32. `HotelFacade` lives in `service`, the repository interfaces in `data`, `AppConfig` in `config`. The check against the Java files is done (C-04). |
| External systems and libraries appear and are justified. | SQL Server and the supplier XML file are in the diagram as external elements. Libraries are not components, so they are only in the table below and in `libraries.md`. |

## Components

### gui
- **What:** JavaFX controllers and views. One controller per screen group.
- **Why:** The statement recommends a JavaFX GUI (RNF-06).
- **Why this way:** A controller reads input and shows results. It calls `HotelFacade` only.
- **Other option:** Controllers call services or SQL. Not done: GUI and business logic would mix (RNF-07).

### service
- **What:** `HotelFacade` and the services. All business rules.
- **Why:** RNF-07 and RNF-08. Rules stay in one layer and are easy to test.
- **Why this way:** `HotelFacade` is one entry point. The GUI does not know which service does the work.
- **Other option:** Put rules in the controllers. Not done: no separation, hard to test with JUnit.

### domain
- **What:** Domain classes and enums from the class diagram.
- **Why:** All layers share the same types.
- **Why this way:** No SQL and no JavaFX in `domain`. Any layer can use it.
- **Other option:** Separate data objects for the GUI. Not done: more code for little gain.

### data
- **What:** Repository interfaces and the JDBC version for SQL Server (`JdbcUserRepository`, `Database`). `Database` gets `AppConfig` from `config` and opens the connections.
- **Why:** The statement requires SQL Server (RNF-12). All users are saved (RNF-01).
- **Why this way:** T0 (H4Y-32) puts the repository interfaces in `data`. `service` uses only the interfaces, never JDBC. Unit tests use an in-memory repository in `src/test` (RNF-10).
- **Other option:** SQL inside the services. Not done: rules and SQL would mix, and tests would need a database.

### config
- **What:** Reads the settings from the `application.properties` file (`AppConfig`). Starts the application.
- **Why:** Credentials must not be in the code or in Git (H4Y-33).
- **Why this way:** One place creates and connects all objects. The other packages do not create each other.
- **Other option:** Each class reads the settings. Not done: settings code in many places.

## External components and libraries

| Name | In the diagram | Why | Rejected option |
|------|----------------|-----|-----------------|
| SQL Server | Yes, external component | The statement requires SQL Server (RNF-12). | Other database: not allowed. |
| Microsoft JDBC Driver for SQL Server (mssql-jdbc) | No, library | Official Microsoft driver. Java needs a driver to connect to SQL Server (RNF-12). | jTDS: not maintained. |
| JavaFX | No, library | GUI library recommended by the statement (RNF-06, Q-22). | Swing: older and less flexible. |
| BCrypt (`at.favre.lib:bcrypt`) | No, library | Passwords must be saved encrypted (RNF-02). BCrypt makes a salted one-way hash. Cost 12. | jBCrypt: not maintained. Spring Security Crypto: adds the Spring libraries. PBKDF2 in the JDK: more own code. Argon2: needs a native library. |
| Supplier XML stock file | Yes, external artifact | The supplier sends one XML file with each delivery (RF-28). | None: the statement sets the format. |
| JUnit | No, library | The statement requires JUnit tests (RNF-10). | None: the statement names JUnit. |

Every external library also goes in [docs/libraries.md](../libraries.md) with its version (RNF-11, H4Y-36). The versions are in `pom.xml` and `docs/libraries.md`.

## Questions in class

| Question | Answer |
|----------|--------|
| Why does `gui` use `domain`? | `HotelFacade` returns domain objects. The GUI only reads them. `domain` is part of the business logic and has no rules about storage. |
| Why are the repository interfaces in `data`, not in `service`? | T0 (H4Y-32) puts them in `data`. `service` still depends only on the interfaces, so the unit tests can use the in-memory repository in `src/test`. |
| Where is the database in the class diagram? | `Database` opens the connection. `JdbcUserRepository` implements `UserRepository` with SQL (G-01, C-01 closed). |
| Why are JavaFX, the JDBC driver and JUnit not in the diagram? | A library is not a component. Only components, interfaces and external elements go in the diagram. |
| Why is SQL Server outside the subsystem? | We do not write it. It is an external component that provides `SQL`. `data` reaches it through a port. |
| Why is the socket a `(` character? | Mermaid has no socket shape. The `(` and `(○` text nodes draw the socket and the ball-and-socket of the slide. |
| Why are the subsystem ports next to the border? | Mermaid cannot put a box on a border. This is the only layout difference from the slide. |
| Why `config` uses all packages? | It only creates and connects objects at start-up. No business call goes through it. |

## Validation results (2026-10-21)

Mermaid CLI rendered the diagram to a temporary PNG file. The preview was inspected: the subsystem box holds the parts, the ports sit on its border, and SQL Server and the XML file are outside. No rendered file was added to the repository.

The five packages now exist in the Java code. The class-to-diagram check is done (architecture review, 2026-10-17, C-04). Human review is still required.

Created by: jmbcruz. Updated by: jmbcruz. AI use: Cursor agent, Claude Opus 5.5, 2026-10-16; review fixes by Cursor agent, Claude Opus 5.5, 2026-10-16; docs-updater, Claude Code (claude-haiku-5-5), 2026-10-16; doc-updater, Claude Code (claude-haiku-5-5), 2026-10-17; doc-updater, Claude Code (claude-sonnet-5-5), 2026-10-17; teacher feedback fixes by jmbcruz with Claude Code, 2026-10-21; left-to-right layout fix by jmbcruz with Claude Code (claude-opus-5-5), 2026-10-23; merge with main by Claude Code (claude-opus-5-5), 2026-10-23; slide match by Claude Code (claude-opus-5-5), 2026-10-30. Reviewed by: pending.
