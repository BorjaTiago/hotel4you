# H4Y-9 - Component Guide

Use this guide to defend the [component diagram](component-diagram.md). Classes come from [classes.md](classes.md) and [class-diagram.md](class-diagram.md). Package names come from H4Y-32.

## Main ideas

1. One component is one Java package: `gui`, `service`, `domain`, `data`, `config`.
2. The GUI talks only to the business logic. `gui` calls `HotelFacade` and reads `domain` objects. It never uses `data`, JDBC or SQL Server (RNF-07).
3. Services do not know SQL. They use the repository interfaces in `data` (T0, H4Y-32). `data` implements them with JDBC (H4Y-32 PR 2). Unit tests use an in-memory repository in `src/test`, not in the application (RNF-08, RNF-10, RNF-12).
4. Only `config` knows all packages. It creates and connects the objects at start-up. It holds no business rule.
5. External libraries are few. Each one has a reason from the statement (RNF-11).

## Done criteria of H4Y-9

| Criterion | How the diagram meets it |
|-----------|--------------------------|
| The interface communicates only with the business logic. | `gui` has arrows only to `HotelFacade`, `domain` and JavaFX. `HotelFacade` and `domain` are in the "Business logic" block. |
| Each component matches a package in the code. | The five components are the five packages planned in H4Y-32, and the five packages exist in the Java code. `HotelFacade` lives in `service`, the repository interfaces in `data`, `AppConfig` in `config`. The check of each class is pending (C-04). |
| External systems and libraries appear and are justified. | See "External systems and libraries" below. |

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
- **What:** Repository interfaces. The JDBC versions for SQL Server come in H4Y-32 PR 2.
- **Why:** The statement requires SQL Server (RNF-12). All users are saved (RNF-01).
- **Why this way:** T0 (H4Y-32) puts the repository interfaces in `data`. `service` uses only the interfaces, never JDBC. Unit tests use an in-memory repository in `src/test` (RNF-10).
- **Other option:** SQL inside the services. Not done: rules and SQL would mix, and tests would need a database.

### config
- **What:** Reads the database settings. Starts the application.
- **Why:** Credentials must not be in the code or in Git (H4Y-33).
- **Why this way:** One place creates and connects all objects. The other packages do not create each other.
- **Other option:** Each class reads the settings. Not done: settings code in many places.

## External systems and libraries

| Name | Why | Rejected option |
|------|-----|-----------------|
| SQL Server | The statement requires SQL Server (RNF-12). | Other database: not allowed. |
| Microsoft JDBC Driver for SQL Server (mssql-jdbc) | Official Microsoft driver. Java needs a driver to connect to SQL Server (RNF-12). | jTDS: not maintained. |
| JavaFX | GUI library recommended by the statement (RNF-06, Q-22). | Swing: older and less flexible. |
| BCrypt (`at.favre.lib:bcrypt`) | Passwords must be saved encrypted (RNF-02). BCrypt makes a salted one-way hash. Cost 12. | jBCrypt: not maintained. Spring Security Crypto: adds the Spring libraries. PBKDF2 in the JDK: more own code. Argon2: needs a native library. |
| Supplier XML stock file | The supplier sends one XML file with each delivery (RF-28). | None: the statement sets the format. |
| JUnit | The statement requires JUnit tests (RNF-10). | None: the statement names JUnit. |

Every external library also goes in [docs/libraries.md](../libraries.md) with its version (RNF-11, H4Y-36). The versions are set when the build file exists (H4Y-32).

## Questions in class

| Question | Answer |
|----------|--------|
| Why does `gui` use `domain`? | `HotelFacade` returns domain objects. The GUI only reads them. `domain` is part of the business logic and has no rules about storage. |
| Why are the repository interfaces in `data`, not in `service`? | T0 (H4Y-32) puts them in `data`. `service` still depends only on the interfaces, so the unit tests can use the in-memory repository in `src/test`. |
| Where is the database in the class diagram? | The class diagram has no database class yet (G-01, C-01). It shows `UserRepository`. The JDBC version comes in H4Y-32 PR 2. |
| Why is JUnit not in the diagram? | The diagram shows the running application. JUnit runs only in tests. |
| Why `config` uses all packages? | It only creates and connects objects at start-up. No business call goes through it. |

## Validation results (2026-10-16)

Mermaid CLI 12.0.0 rendered the diagram to a temporary PNG file. The preview was inspected: the layers read from top to bottom (`config`, `gui`, business logic, `data`, external systems). No rendered file was added to the repository.

`git diff --check` passed. The five packages now exist in the Java code. The check of each class against the diagram stays pending (C-04). Human review is still required.

Created by: jmbcruz. Updated by: BorjaTiago. AI use: Cursor agent, Claude Opus 5.5, 2026-10-16; review fixes by Cursor agent, Claude Opus 5.5, 2026-10-16; docs-updater, Claude Code (claude-haiku-5-5), 2026-10-16; doc-updater, Claude Code (claude-haiku-5-5), 2026-10-17. Reviewed by: pending.
