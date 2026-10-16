---
type: subtask
summary: Tests, Javadoc and diagram update for the main window
parent: H4Y-24
requirements: [RNF-06, RNF-07]
points:
points_suggested:
status: published
jira_key: H4Y-43
labels: [foundation, tests, docs, 1st-delivery]
sprint: 02
branch: feat-024-main-window-tests-docs
---

# janela-principal: tests, Javadoc, diagram

Parent story H4Y-24. Branch key 024 is the story key.
Proposed owner: Pedro Costa (the team decides).

## Checklist

- [ ] One JUnit test per AC-1 to AC-4. Names cite "H4Y-24 AC-n". Tests use the navigation model, not a screen.
- [ ] Javadoc on the new public classes. The Javadoc builds.
- [ ] Class diagram updated (navigation registry, session) with the AI-use reference.
- [ ] No new library, or it is in `docs/libraries.md`.

AI use: sprint-planner, Claude Code (claude-sonnet-5-5), 2026-10-16. Reviewed by: pending.
