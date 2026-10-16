# External libraries

Every external library that the application or its tests use is listed here (RNF-11, Q-14). Add a row before you add a library to the build file.

The versions are not set yet. Write each version when the build file exists (H4Y-32).

| Name | Version | Used by | Reason | Rejected option |
|------|---------|---------|--------|-----------------|
| JavaFX | Not set (H4Y-32) | `gui` | The statement recommends a JavaFX GUI (RNF-06, Q-22). | Swing: older and less flexible. |
| Microsoft JDBC Driver for SQL Server (`mssql-jdbc`) | Not set (H4Y-32) | `data` | The statement requires SQL Server (RNF-12). Java needs a driver to connect. This is the official Microsoft driver. | jTDS: not maintained. |
| JUnit | Not set (H4Y-32) | Tests only | The statement requires JUnit tests (RNF-10). | None: the statement names JUnit. |

## Java SE parts (no extra library)

| Part | Used by | Reason | Status |
|------|---------|--------|--------|
| `java.sql` | `data` | Connects to SQL Server through the JDBC driver. | Planned |
| `javax.xml` | `service` | Reads the supplier XML stock file (RF-28). | Planned |
| `java.time` | `domain`, `service` | Dates and times of reservations and card passes. | Planned |
| `javax.crypto` | `service` | Password hashing (RNF-02). | Proposed. H4Y-29 decides the hash method. If it picks a library, add a row to the first table. |

AI use: Cursor agent, Claude Opus 5.5, 2026-10-16. Reviewed by: pending.
