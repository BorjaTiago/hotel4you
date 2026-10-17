# Setup

## Requirements

- JDK 21.
- You do not need to install Maven. Use the Maven Wrapper: `./mvnw`.
- On Windows, use `mvnw.cmd`.

## Build and test

Run these commands from the repo root:

```bash
./mvnw verify            # compile, run the unit tests, and package
./mvnw javadoc:javadoc   # build the Javadoc
```

The output goes to `target/`. The Javadoc is in `target/site/apidocs/`.

## Build tools

| Tool | Version |
|------|---------|
| Maven Wrapper (Maven) | 3.3.2 (Maven 3.9.9) |
| maven-compiler-plugin | 3.13.0 |
| maven-surefire-plugin | 3.5.2 |
| maven-javadoc-plugin | 3.10.1 |
| javafx-maven-plugin | 0.0.8 |

The libraries are listed in [libraries.md](libraries.md).

## Database

SQL Server setup comes with H4Y-32 PR 2.

Created by: BorjaTiago. Updated by: BorjaTiago. AI use: doc-updater, Claude Code (claude-haiku-5-5), 2026-10-17. Reviewed by: pending.
