---
type: subtask
summary: JavaFX form to create a staff account
parent: H4Y-27
requirements: [RF-26, RF-01]
points:
points_suggested:
status: published
jira_key: H4Y-39
labels: [staff, gui, 1st-delivery]
sprint: 02
branch: feat-027-create-staff-form
---

# create-staff-account: form

Parent story H4Y-27. Uses the navigation registry of T0b. Register the form for the manager role only. Branch key 027 is the story key.
Proposed owner: Tiago Borja (the team decides).

## Checklist

- [ ] The form has user name, password and type (receptionist, restaurant staff, manager).
- [ ] The form shows the service refusals: name exists, missing data, no permission.
- [ ] A success message shows after creation. The password is not shown again.
- [ ] The entry appears in the navigation for the manager only.

AI use: sprint-planner, Claude Code (claude-sonnet-5-5), 2026-10-16. Reviewed by: pending.
