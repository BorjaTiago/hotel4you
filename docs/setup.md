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
| exec-maven-plugin | 3.5.0 |

The libraries are listed in [libraries.md](libraries.md).

## Database

See the README section [How to start the project](../README.md#how-to-start-the-project).

Created by: BorjaTiago. Updated by: BorjaTiago. AI use: doc-updater, Claude Code (claude-haiku-5-5), 2026-10-17; doc-updater, Claude Code (claude-sonnet-5-5), 2026-10-17. Reviewed by: pending.
