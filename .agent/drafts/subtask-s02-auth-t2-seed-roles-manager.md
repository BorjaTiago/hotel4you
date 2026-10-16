---
type: subtask
summary: T2 - Seed script for the 5 roles and the first manager account
parent: H4Y-12
requirements: [RF-01, RF-02, RNF-02]
points:
points_suggested:
status: published
jira_key: H4Y-30
labels: [security, database, 1st-delivery]
sprint: 02
branch: feat-030-seed-roles-first-manager
---

# Auth T2: Seed of roles and first manager

Source: `epic-authentication-users.md`, task T2. Needs foundation T2 (schema) and auth T1 (hash). The logins use test data for their tests, so they do not wait for this card. Real end-to-end login needs it.
Proposed owner: Antonio Martinho (the team decides).

## Checklist

- [ ] With an empty database, the script creates the 5 roles of RF-02. Client and supplier get no account (Q-13, Q-17).
- [ ] The script creates one manager account. User name and password come from local config or environment. Never from code, Git or docs.
- [ ] The password is saved only as a hash. The plain value is not in any log.
- [ ] Without the config values, the script stops with a clear message. It does not create a default password.
- [ ] The script runs twice without duplicate roles or accounts.
- [ ] `.gitignore` covers the local config file. A sample file has no real value.
- [ ] JUnit test for the seed logic on the in-memory repository.

AI use: sprint-planner, Claude Code (claude-sonnet-5-5), 2026-10-16. Reviewed by: pending.
