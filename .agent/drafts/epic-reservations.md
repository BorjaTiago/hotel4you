---
type: epic
summary: Room availability, reservation, cancel and delete
parent:
requirements: [RF-05, RF-11, RF-12, RF-22, RNF-03]
points:
points_suggested:
status: approved
jira_key:
labels: [reservations, 1st-delivery]
sprint:
---

# Epic: Reservations

## Goal
Reception makes and cancels reservations for free rooms. Only the manager deletes a reservation, before check-in.

## Delivery
1st delivery (reservations).

## Scope
- Offer free rooms by type and nights; hide occupied rooms.
- Make a reservation (can include restaurant and pool).
- Cancel = state change, kept in history (Q-05).
- Delete = manager only, only before check-in, for good (Q-05).
- Client registration plus reservation in 30 seconds or less (RNF-03, Q-12).

## Out of scope
- Client registration screens (epic-client-registration; see Q-27).
- Check-in and check-out (epic-check-in-out).

## Actors
- Receptionist, manager. Client (chooses the room type through reception).

## Value
The hotel books rooms without double booking.

## Statement links
Section 2, 3c. RF-05, RF-11, RF-12, RF-22, RNF-03.

## Candidate stories
- Consultar quartos livres por tipo e noites (actor: receptionist; RF-05)
- Criar uma reserva para um cliente (actor: receptionist; RF-11)
- Incluir restaurante e piscina na reserva (actor: receptionist; RF-11)
- Cancelar uma reserva mantendo o histórico (actor: receptionist; RF-12)
- Apagar uma reserva antes do check-in (actor: manager; RF-22)
- Recusar o apagar de reservas a outros papéis e após check-in (actor: receptionist; RF-22)
- Medir registo mais reserva abaixo de 30 segundos (actor: receptionist; RNF-03)

## Open questions
- Q-27 (open): reservation needs a client record, RF-10 is not in the 1st delivery scope.

AI use: epic-writer, Claude Code (claude-sonnet-5-5), 2026-10-16. Reviewed by: pending.
