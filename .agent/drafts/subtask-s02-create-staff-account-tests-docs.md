---
type: subtask
summary: Tests, Javadoc and diagram update for create staff account
parent: H4Y-27
requirements: [RF-26, RF-01, RNF-02]
points:
points_suggested:
status: published
jira_key: H4Y-41
labels: [staff, tests, docs, 1st-delivery]
sprint: 02
branch: feat-027-create-staff-tests-docs
---

# create-staff-account: tests, Javadoc, diagram

Parent story H4Y-27. Branch key 027 is the story key.
Proposed owner: Tiago Borja (the team decides).

## Checklist

- [ ] One JUnit test per AC-1 to AC-4. Names cite "H4Y-27 AC-n".
- [ ] A test shows the stored password differs from the input (AC-1).
- [ ] Test data: empty base and one existing user "ana" (T4).
- [ ] Javadoc on the new public classes. The Javadoc builds.
- [ ] Class diagram updated (`UserService`), with the AI-use reference.
- [ ] Manual check: the manager creates a receptionist and a restaurant account that log in (sprint 3 login-restaurante uses it).

AI use: sprint-planner, Claude Code (claude-sonnet-5-5), 2026-10-16. Reviewed by: pending.
