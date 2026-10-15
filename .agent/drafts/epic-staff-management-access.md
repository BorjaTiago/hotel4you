---
type: epic
summary: Staff accounts and floor and zone access limits
parent:
requirements: [RF-24, RF-25, RF-26]
points:
points_suggested:
status: approved
jira_key:
labels: [staff, access-control]
sprint:
---

# Epic: Staff management and access limits

## Goal
The manager manages staff accounts. Reception sees only floors 0 and 1. Restaurant staff see only the kitchen and restaurant.

## Delivery
Planned by the prioritizer (Q-25).

## Scope
- Create, edit, remove staff accounts (no schedules, Q-18).
- Reception limited to floors 0 and 1 services (Q-08).
- Restaurant staff limited to kitchen and restaurant.

## Out of scope
- Login itself (epic-authentication-users).

## Actors
- Manager, receptionist, restaurant staff.

## Value
Each employee has the right access.

## Statement links
Section 2. RF-24, RF-25, RF-26. Q-08, Q-18, Q-23.

## Candidate stories
- Criar uma conta de funcionário (actor: manager; RF-26)
- Editar uma conta de funcionário (actor: manager; RF-26)
- Remover uma conta de funcionário (actor: manager; RF-26)
- Limitar a receção aos serviços dos pisos 0 e 1 (actor: receptionist; RF-24)
- Limitar o restaurante à cozinha e ao restaurante (actor: restaurant staff; RF-25)

## Open questions
- Other staff such as cleaning are not in the statement (actors.md notes).

AI use: epic-writer, Claude Code (claude-sonnet-5-5), 2026-10-16. Reviewed by: pending.
