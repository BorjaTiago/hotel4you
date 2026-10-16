---
type: subtask
summary: T2 - SQL Server connection and H4Y-10 schema scripts (PR #4 version)
parent: H4Y-11
requirements: [RNF-12]
points:
points_suggested:
status: published
jira_key: H4Y-33
labels: [foundation, database, 1st-delivery]
sprint: 02
branch: feat-033-sqlserver-schema-scripts
---

# T2: SQL Server connection and schema scripts

Source: `epic-foundation-architecture.md`, task T2. Needs T0a (config and repository interfaces). Use the merged PR #4 schema in `docs/H4Y-10/`, not the old one. Runs in parallel with auth T1 (hash).
Proposed owner: Lukas Sperandio (the team decides).

## Checklist

- [ ] The JDBC connection reads credentials from local config or environment. No credential in Git (RNF-12).
- [ ] The scripts create the schema of `docs/H4Y-10/` (PR #4 version). Check the merged files first.
- [ ] With an empty database, the first setup creates all tables.
- [ ] A saved record is still there after the app closes and opens.
- [ ] With SQL Server down, the app shows a clear message and no connection detail.
- [ ] A JDBC `UserRepository` exists (needed by login and create-staff-account).
- [ ] Setup steps for the team are written in `docs/` (no password in the text).
- [ ] Check roles data: 5 roles (Q-46). Report any gap to the Team Leader.

AI use: sprint-planner, Claude Code (claude-sonnet-5-5), 2026-10-16. Reviewed by: pending.
