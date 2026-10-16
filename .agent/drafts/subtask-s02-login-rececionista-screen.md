---
type: subtask
summary: JavaFX login screen with validation messages
parent: H4Y-22
requirements: [RF-01, RF-02]
points:
points_suggested:
status: published
jira_key: H4Y-45
labels: [login, gui, 1st-delivery]
sprint: 02
branch: feat-022-login-screen
---

# login-rececionista: login screen

Parent story H4Y-22. Uses the T0b shell and the stub login result until the service is merged. Branch key 022 is the story key.
Proposed owner: José Cruz (the team decides).

## Checklist

- [ ] The screen has user name, password (masked) and a login button.
- [ ] After a valid login, the app opens the main window with the session (links to `janela-principal`).
- [ ] The screen shows "Credenciais inválidas" for AC-2 and AC-3, with the same text.
- [ ] The screen shows a validation message for empty fields (AC-4).
- [ ] The screen does not show the password or any internal detail.
- [ ] The screen is the first screen when no session exists.

AI use: sprint-planner, Claude Code (claude-sonnet-5-5), 2026-10-16. Reviewed by: pending.
