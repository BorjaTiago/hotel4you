---
type: subtask
summary: T0b - Empty JavaFX window, navigation registry, stub session and first class diagram
parent: H4Y-11
requirements: [RNF-05, RNF-06, RNF-07, RNF-08]
points:
points_suggested:
status: published
jira_key: H4Y-31
labels: [foundation, gui, diagram, 1st-delivery]
sprint: 02
branch: feat-031-gui-shell-class-diagram
---

# T0b: Empty JavaFX window, navigation registry, stub session, class diagram

Source: `epic-foundation-architecture.md`, task T0, item 4 and step (e). Starts with the core interfaces of T0a. The stubs let the story cards start without waiting for login.
Proposed owner: Pedro Costa (the team decides).

## Checklist

- [ ] An empty JavaFX window starts from the `gui` package.
- [ ] A navigation registry exists. A function registers a screen with the roles that may see it.
- [ ] A simulated `Session` (stub) can be set to receptionist or manager for tests and demo. Real login replaces it later.
- [ ] A stub `PermissionPolicy` returns the functions per role (RF-02). `funcoes-por-papel` (sprint 3) replaces it.
- [ ] The GUI only calls services. No business rule in `gui` (RNF-07).
- [ ] Initial class diagram (domain, services, repositories) in `docs/`, with the AI-use reference (RNF-25).
- [ ] Creation order of the classes is written in `docs/`. The team reviews it in planning.

AI use: sprint-planner, Claude Code (claude-sonnet-5-5), 2026-10-16. Reviewed by: pending.
