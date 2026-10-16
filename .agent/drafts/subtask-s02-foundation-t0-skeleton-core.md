---
type: subtask
summary: T0a - Java project skeleton, domain classes and core interfaces
parent: H4Y-11
requirements: [RNF-05, RNF-07, RNF-08, RNF-12]
points:
points_suggested:
status: published
jira_key: H4Y-32
labels: [foundation, backend, 1st-delivery]
sprint: 02
branch: feat-032-project-skeleton-core
---

# T0a: Project skeleton, domain classes and core interfaces

Source: `epic-foundation-architecture.md`, task T0, items 1 to 3. First card of sprint 2. Every other card waits only for the end of this card (target: end of day 2). Branch key NNN: the Jira key does not exist yet. Write the real number after publish.
Proposed owner: Lukas Sperandio (the team decides).

## Checklist

- [ ] Build file and package layout exist: `domain`, `service`, `data`, `gui`, `config`. The project compiles.
- [ ] Domain classes and enums exist: `Role` (5 types), `User`, `Session`, `Floor`, `Location`, `RoomType`, `Room`, `Customer`, `Reservation`, `AccessCard`, `AccessLog`, `Item`, `StockMovement`, and the others of T0. No SQL and no JavaFX in `domain`.
- [ ] Service interfaces exist: `AuthenticationService`, `PasswordHasher`, `PermissionPolicy`, `UserService`, and the others of T0. Signatures only.
- [ ] Repository interfaces exist with an in-memory version for tests (`UserRepository` first).
- [ ] `config` reads database and first-manager values from local config or environment. No credential in Git.
- [ ] Short Javadoc on every public class (RNF-09).
- [ ] Merge to the shared branch by end of day 2. Tell the team.

AI use: sprint-planner, Claude Code (claude-sonnet-5-5), 2026-10-16. Reviewed by: pending.
