---
type: epic
summary: Manager master card, block and access reports
parent:
requirements: [RF-20, RF-21, RF-23, RF-30]
points:
points_suggested:
status: approved
jira_key:
labels: [access-control, final-delivery]
sprint:
---

# Epic: Master card and access audit

## Goal
The manager opens all rooms and zones with a master card, blocks a lost card, and audits card use.

## Delivery
Final delivery (Q-06).

## Scope
- One master card per manager; every pass logged (Q-19).
- Block a lost master card at once; issue a new card with a new number; old number stays in the log (Q-26).
- List of places of one card.
- List of card passes of one room (master card included).

## Out of scope
- Client card (epic-magnetic-card-access).

## Actors
- Manager.

## Value
Full control and audit of access.

## Statement links
Section 2. RF-20, RF-21, RF-23, RF-30. Q-19, Q-26.

## Candidate stories
- Abrir todos os quartos e zonas com o cartão mestre (actor: manager; RF-23)
- Bloquear um cartão mestre perdido (actor: manager; RF-30)
- Emitir um novo cartão mestre com novo número (actor: manager; RF-30)
- Consultar os locais de um cartão (actor: manager; RF-20)
- Consultar as passagens de cartões num quarto (actor: manager; RF-21)

## Open questions
- None new. Q-19 and Q-26 are team decisions, not confirmed by the teacher.

AI use: epic-writer, Claude Code (claude-sonnet-5-5), 2026-10-16. Reviewed by: pending.
