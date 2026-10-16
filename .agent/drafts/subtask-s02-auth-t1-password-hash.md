---
type: subtask
summary: T1 - Encrypted password storage (PasswordHasher)
parent: H4Y-12
requirements: [RNF-02]
points:
points_suggested:
status: published
jira_key: H4Y-29
labels: [security, backend, 1st-delivery]
sprint: 02
branch: feat-029-password-hasher
---

# Auth T1: Encrypted passwords

Source: `epic-authentication-users.md`, task T1. Needs T0a (`PasswordHasher` interface). Must end before the seed (auth T2). Runs in parallel with foundation T2.
Proposed owner: Antonio Martinho (the team decides).

## Checklist

- [ ] `PasswordHasher` implements a salted hash. The stored value is not plain text and differs from the input.
- [ ] A correct password verifies. A wrong password does not verify.
- [ ] The same error covers a wrong password and an unknown account.
- [ ] No password appears in a log or an error message.
- [ ] If an external library is used, add it to `docs/libraries.md` with name, version, reason and rejected alternative (RNF-11).
- [ ] JUnit tests and Javadoc for the class.

AI use: sprint-planner, Claude Code (claude-sonnet-5-5), 2026-10-16. Reviewed by: pending.
