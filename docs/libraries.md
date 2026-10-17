# External libraries

Every external library that the application or its tests use is listed here (RNF-11, Q-14). Add a row before you add a library to the build file.

The versions match `pom.xml`.

| Name | Version | Used by | Reason | Rejected option |
|------|---------|---------|--------|-----------------|
| JavaFX | 21.0.5 | `gui` | The statement recommends a JavaFX GUI (RNF-06, Q-22). | Swing: older and less flexible. |
| Microsoft JDBC Driver for SQL Server (`mssql-jdbc`) | 12.10.0.jre11 | `data` | The statement requires SQL Server (RNF-12). Java needs a driver to connect. This is the official Microsoft driver. | jTDS: not maintained. |
| BCrypt (`at.favre.lib:bcrypt`) | 0.10.2 | `service` (`AuthService`) | The statement asks that passwords are saved encrypted (RNF-02). BCrypt makes a salted one-way hash. Cost 12, version `$2a$`. Long passwords are first hashed with SHA-512 by the library, so no password length limit is needed. | jBCrypt: not maintained. Spring Security Crypto: adds the Spring libraries. PBKDF2 in the JDK: more own code. Argon2: needs a native library. |
| JUnit | 5.11.3 | Tests only | The statement requires JUnit tests (RNF-10). | None: the statement names JUnit. |

Created by: jmbcruz. Updated by: BorjaTiago. AI use: Cursor agent, Claude Opus 5.5, 2026-10-16; docs-updater, Claude Code (claude-haiku-5-5), 2026-10-16; docs agent, Claude Code (claude-haiku-5-5), 2026-10-17. Reviewed by: pending.
