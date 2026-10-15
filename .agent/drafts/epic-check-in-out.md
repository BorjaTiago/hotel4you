---
type: epic
summary: Client check-in and check-out
parent:
requirements: [RF-13, RF-14]
points:
points_suggested:
status: approved
jira_key:
labels: [check-in, check-out, 1st-delivery]
sprint:
---

# Epic: Check-in and check-out

## Goal
Reception checks the client in and out of a reservation.

## Delivery
1st delivery. The card issue part of RF-13 is in epic-magnetic-card-access (final).

## Scope
- Check-in of a reservation (state change; blocks manager delete).
- Check-out of a stay.

## Out of scope
- Card issue and card state at check-out (final delivery; Q-09 does not answer card state at check-out).

## Actors
- Receptionist.

## Value
The stay life cycle works end to end for the 1st delivery demo.

## Statement links
Section 2. RF-13, RF-14. Q-06.

## Candidate stories
- Fazer check-in de uma reserva (actor: receptionist; RF-13)
- Fazer check-out de uma estadia (actor: receptionist; RF-14)
- Impedir apagar reservas após check-in (actor: manager; RF-13, RF-22)

## Open questions
- Card state at check-out: answered by Q-30 (the card stops working, no door opens, the log is kept).

AI use: epic-writer, Claude Code (claude-sonnet-5-5), 2026-10-16; stale note fix by doc-editor, Claude Code (claude-haiku-5-5), 2026-10-16. Reviewed by: pending.
