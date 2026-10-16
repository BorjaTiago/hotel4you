---
type: subtask
summary: Tests, Javadoc and diagram update for the login
parent: H4Y-22
requirements: [RF-01, RF-02]
points:
points_suggested:
status: published
jira_key: H4Y-47
labels: [login, tests, docs, 1st-delivery]
sprint: 02
branch: feat-022-login-tests-docs
---

# login-rececionista: tests, Javadoc, diagram

Parent story H4Y-22. Branch key 022 is the story key.
Proposed owner: Antonio Martinho (the team decides).

## Checklist

- [ ] One JUnit test per AC-1 to AC-4. Names cite "H4Y-22 AC-n".
- [ ] A test proves AC-2 and AC-3 give an identical response.
- [ ] Javadoc on the new public classes. The Javadoc builds.
- [ ] Class and sequence diagram of the login updated, with the AI-use reference.
- [ ] One end-to-end check on SQL Server with the seed manager, after auth T2 (not a blocker for the unit tests).

AI use: sprint-planner, Claude Code (claude-sonnet-5-5), 2026-10-16. Reviewed by: pending.
