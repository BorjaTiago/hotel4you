# External libraries

Every external library that the application or its tests use is listed here (RNF-11, Q-14). Add a row before you add a library to the build file.

The versions match `pom.xml`.

| Name | Version | Used by | Reason | Rejected option |
|------|---------|---------|--------|-----------------|
| JavaFX | 21.0.5 | `gui` | The statement recommends a JavaFX GUI (RNF-06, Q-22). | Swing: older and less flexible. |
| Microsoft JDBC Driver for SQL Server (`mssql-jdbc`) | 12.10.0.jre11 | `data` | The statement requires SQL Server (RNF-12). Java needs a driver to connect. This is the official Microsoft driver. | jTDS: not maintained. |
| BCrypt (`at.favre.lib:bcrypt`) | 0.10.2 | `service` (`AuthService`) | The statement asks that passwords are saved encrypted (RNF-02). BCrypt makes a salted one-way hash. Cost 12, version `$2a$`. Long passwords are first hashed with SHA-512 by the library, so no password length limit is needed. | jBCrypt: not maintained. Spring Security Crypto: adds the Spring libraries. PBKDF2 in the JDK: more own code. Argon2: needs a native library. |
| Gson (`com.google.code.gson:gson`) | 2.11.0 (planned, not in `pom.xml` yet) | `service` (`JsonStockFileReader`) | The statement update of 2026-11-06 asks to import supplier JSON files (RF-28, H4Y-84). The JDK has no JSON parser. Gson is one small jar with no other dependencies. It reads a JSON file into Java objects and stops with an error on a bad file. Add it to `pom.xml` with the import story. XML needs no library: the JDK has `javax.xml.parsers`. | Jackson (`jackson-databind`): more features than needed and three jars. `org.json`: no mapping to Java objects, more hand code. A hand-written parser: more code and more bugs. |
| JUnit | 5.11.3 | Tests only | The statement requires JUnit tests (RNF-10). | None: the statement names JUnit. |

Created by: jmbcruz. Updated by: antonioprmartinho. AI use: Cursor agent, Claude Opus 5.5, 2026-10-16; docs-updater, Claude Code (claude-haiku-5-5), 2026-10-16; docs agent, Claude Code (claude-haiku-5-5), 2026-10-17; H4Y-84 JSON stock import docs by Claude Code (claude-opus-5-5), 2026-11-12. Reviewed by: pending.
