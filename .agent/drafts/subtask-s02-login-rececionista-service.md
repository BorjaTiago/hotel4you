---
type: subtask
summary: Login service - check credentials, create session, generic error
parent: H4Y-22
requirements: [RF-01, RF-02]
points:
points_suggested:
status: published
jira_key: H4Y-46
labels: [security, backend, login, 1st-delivery]
sprint: 02
branch: feat-022-login-service
---

# login-rececionista: login service

Parent story H4Y-22 (5 points). Needs T0a, auth T1 (hash) and `UserRepository`. Tests use T4 test users, so the card does not wait for the seed. Branch key 022 is the story key.
Proposed owner: Antonio Martinho (the team decides).

## Checklist

- [ ] `AuthenticationService.login` checks the user name and the hashed password (AC-1). It returns a `Session` with the role.
- [ ] A wrong password and an unknown user give the same error "Credenciais inválidas" (AC-2, AC-3).
- [ ] Empty user name or password is refused before any lookup (AC-4).
- [ ] The service has no JavaFX code. The manager and restaurant logins reuse it.
- [ ] A test data file with one receptionist (T4).

AI use: sprint-planner, Claude Code (claude-sonnet-5-5), 2026-10-16. Reviewed by: pending.
