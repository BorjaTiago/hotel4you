---
type: subtask
summary: Main window with navigation by role and no-session guard
parent: H4Y-24
requirements: [RNF-06, RNF-07]
points:
points_suggested:
status: published
jira_key: H4Y-42
labels: [foundation, gui, 1st-delivery]
sprint: 02
branch: feat-024-main-window-navigation
---

# janela-principal: main window and navigation

Parent story H4Y-24 (3 points). Builds on the T0b shell, the stub session and the stub policy. Does not wait for login. Branch key 024 is the story key. The team may prefer the sub-task key after publish.
Proposed owner: Pedro Costa (the team decides).

## Checklist

- [ ] AC-1: With a session, the main window shows the navigation of the user role.
- [ ] AC-2: The user opens a function from the navigation and returns to the main window.
- [ ] AC-3: A function not allowed to the role does not appear (uses `PermissionPolicy`).
- [ ] AC-4: With no session, the app shows the login screen and not the main window.
- [ ] The window only shows and navigates. No business rule in the GUI (RNF-07).
- [ ] Re-check AC-3 after `funcoes-por-papel` (sprint 3).

AI use: sprint-planner, Claude Code (claude-sonnet-5-5), 2026-10-16. Reviewed by: pending.
