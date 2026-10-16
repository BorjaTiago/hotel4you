---
type: subtask
summary: Create staff account service - unique name, hashed password, manager only
parent: H4Y-27
requirements: [RF-26, RF-01, RNF-02]
points:
points_suggested:
status: published
jira_key: H4Y-40
labels: [staff, backend, 1st-delivery]
sprint: 02
branch: feat-027-create-staff-service
---

# create-staff-account: service

Parent story H4Y-27 (3 points). Needs T0a, the hasher (auth T1) and `UserRepository`. Tests use a stub manager session and an empty test base (T4). Does not wait for login. Branch key 027 is the story key.
Proposed owner: Tiago Borja (the team decides).

## Checklist

- [ ] AC-1: `UserService.createStaff` saves user name, role and hashed password. Roles: receptionist, restaurant staff, manager.
- [ ] AC-2: A repeated user name is refused with a message that the name exists.
- [ ] AC-3: A missing password (or user name or role) is refused.
- [ ] AC-4: A caller without the manager role is refused (uses `PermissionPolicy`).
- [ ] No plain password is stored or logged.

AI use: sprint-planner, Claude Code (claude-sonnet-5-5), 2026-10-16. Reviewed by: pending.
