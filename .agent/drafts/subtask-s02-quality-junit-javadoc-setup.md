---
type: subtask
summary: Set up JUnit and Javadoc generation with one command each
parent: H4Y-18
requirements: [RNF-09, RNF-10]
points:
points_suggested:
status: published
jira_key: H4Y-35
labels: [quality, tests, docs, 1st-delivery]
sprint: 02
branch: feat-035-junit-javadoc-setup
---

# Engineering quality T1 and T2: Javadoc and JUnit setup

Source: `epic-engineering-quality.md`, tasks T1 and T2. Needs the build file of T0a. Starts after day 1.
Proposed owner: Pedro Costa (the team decides).

## Checklist

- [ ] One command builds the Javadoc without errors.
- [ ] A public class without Javadoc fails the check and names the class.
- [ ] One command runs all JUnit tests.
- [ ] A failing test ends with an error and names the test.
- [ ] Rule written in `docs/`: each test cites "H4Y-NN AC-n" in `@DisplayName`.
- [ ] A short check or script lists any AC-n without a test.
- [ ] Any new library is listed in `docs/libraries.md`.

AI use: sprint-planner, Claude Code (claude-sonnet-5-5), 2026-10-16. Reviewed by: pending.
