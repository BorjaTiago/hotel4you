---
type: subtask
summary: Manager login - role check, tests and Javadoc
parent: H4Y-20
requirements: [RF-01, RF-02]
points:
points_suggested:
status: published
jira_key: H4Y-44
labels: [login, tests, 1st-delivery]
sprint: 02
branch: feat-020-login-manager
---

# login-gestor: manager login

Parent story H4Y-20 (2 points). Hard link: reuses the login service of H4Y-22. Start when the login service is merged (mid sprint). Branch key 020 is the story key.
Proposed owner: José Cruz (the team decides).

## Checklist

- [ ] AC-1: A manager with valid credentials logs in and the session role is manager.
- [ ] AC-2: A wrong password is refused with "Credenciais inválidas".
- [ ] AC-3: An unknown user gets the same message as AC-2.
- [ ] No second login code path. Reuse the service and the screen of H4Y-22.
- [ ] Test data: one manager (T4). Tests cite "H4Y-20 AC-n".
- [ ] Javadoc on any new public class. The Javadoc builds.
- [ ] After auth T2: the seed manager can log in (manual check, secret stays local).

AI use: sprint-planner, Claude Code (claude-sonnet-5-5), 2026-10-16. Reviewed by: pending.
