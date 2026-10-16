---
type: subtask
summary: T4 - Test data mechanism per story, and T1 layer check
parent: H4Y-11
requirements: [RNF-07, RNF-08, RNF-10]
points:
points_suggested:
status: published
jira_key: H4Y-34
labels: [foundation, tests, 1st-delivery]
sprint: 02
branch: feat-034-test-data-layer-check
---

# T4 and T1: Test data mechanism and layer check

Source: `epic-foundation-architecture.md`, tasks T4 and T1. Needs T0a. This card makes every story test alone, so no story blocks another. Each later story adds its own data file.
Proposed owner: José Cruz (the team decides).

## Checklist

- [ ] A simple loader puts test data into the in-memory repositories.
- [ ] The same loader works on a test SQL Server database. It never runs on the production database.
- [ ] Each story has its own data file. A story never edits the file of another story.
- [ ] Test data has no real password. Test users use a fake hash from the `PasswordHasher`.
- [ ] Sprint 2 data files exist: users (manager, receptionist, restaurant), for the 4 stories.
- [ ] T1: one business rule runs by JUnit without the GUI.
- [ ] T1: a new function does not break the tests of old functions.
- [ ] T1: when a rule refuses, the GUI shows the reason and does not decide the rule.

AI use: sprint-planner, Claude Code (claude-sonnet-5-5), 2026-10-16. Reviewed by: pending.
