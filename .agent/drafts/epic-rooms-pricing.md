---
type: epic
summary: Building model, rooms, room types, base prices and room items
parent:
requirements: [RF-03, RF-04, RF-06, RF-08]
points:
points_suggested:
status: approved
jira_key:
labels: [rooms, 1st-delivery]
sprint:
---

# Epic: Rooms and base pricing

## Goal
The system knows the building and the 20 rooms. The manager sets base prices and room items.

## Delivery
1st delivery (rooms). Zone use by card is in epic-magnetic-card-access (final).

## Scope
- Floor model: -1, 0, 1, 2, 3.
- 20 rooms: 10 on floor 2, 10 on floor 3; 7 single, 10 double, 3 family (Q-01).
- Base price per night by room type (manager, Q-11).
- Manager configures room items.

## Out of scope
- Extra prices (view, drinks, laundry) - epic-extra-services-pricing.
- Availability and booking - epic-reservations.

## Actors
- Manager, receptionist.

## Value
Rooms and prices exist before any reservation.

## Statement links
Section 2. RF-03, RF-04, RF-06, RF-08. Q-01, Q-11.

## Candidate stories
- Modelar os pisos do hotel (actor: manager; RF-03)
- Registar os 20 quartos por tipo e piso (actor: manager; RF-04)
- Definir o preço base por noite de cada tipo de quarto (actor: manager; RF-06)
- Configurar os itens de um quarto (actor: manager; RF-08)
- Consultar a lista de quartos (actor: receptionist; RF-04)

## Open questions
- Q-01 and Q-11 are team decisions, not confirmed by the teacher.

AI use: epic-writer, Claude Code (claude-sonnet-5-5), 2026-10-16. Reviewed by: pending.
