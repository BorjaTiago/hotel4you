---
type: epic
summary: Client magnetic card, door access and access log
parent:
requirements: [RF-03, RF-13, RF-16, RF-17, RF-18, RF-19]
points:
points_suggested:
status: approved
jira_key:
labels: [access-control, final-delivery]
sprint:
---

# Epic: Client magnetic card and access log

## Goal
After check-in, the client gets a card. It opens the room, and the restaurant and pool if in the reservation. The system logs each entry.

## Delivery
Final delivery (Q-06).

## Scope
- Card issue at check-in; one card per reservation (Q-09).
- Card opens the room; restaurant and pool only if reserved.
- No access to bar, conference rooms, garage, storeroom, private pools, spa (Q-20).
- Log: place and entry time, including room entries.
- Zone use of the floor model (RF-03).

## Out of scope
- Physical readers (simulated entry in the app).
- Master card (epic-master-card-audit).

## Actors
- Receptionist, client.

## Value
The hotel controls and records who enters each place.

## Statement links
Section 2. RF-03, RF-13, RF-16, RF-17, RF-18, RF-19. Q-09, Q-20.

## Candidate stories
- Emitir um cartão com número ao fazer check-in (actor: receptionist; RF-13, RF-16)
- Abrir o quarto com o cartão (actor: client; RF-17)
- Abrir restaurante e piscina só se estiverem na reserva (actor: client; RF-18)
- Recusar o cartão nos locais não permitidos (actor: client; RF-18)
- Guardar cada passagem com local e hora (actor: client; RF-19)
- Criar uma zona num piso, com marca "exige reserva" (actor: manager; RF-03)
- Editar uma zona de um piso (actor: manager; RF-03)
- Remover uma zona de um piso (actor: manager; RF-03)

## Open questions
- Card state at check-out: decided 2026-10-16. The card stops working and the access log stays (see story-check-out AC-4).
- Zones: decided 2026-10-16. The manager creates, edits and removes zones per floor and marks if a zone needs a reservation.
- How is a door entry entered without hardware? Team choice: GUI simulation.

AI use: epic-writer, Claude Code (claude-sonnet-5-5), 2026-10-16. Reviewed by: pending.
