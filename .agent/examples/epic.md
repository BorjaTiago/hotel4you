> EXAMPLE only. Not real backlog. Do not publish.

---
type: epic
summary: Magnetic card access control
parent:
requirements: [RF-12, RF-13, RF-14]
points:
points_suggested:
status: draft
jira_key:
labels: [access-control, reservation]
sprint:
---

# Epic: Magnetic card access control

## Goal
After check-in, the client gets a magnetic card. The card opens the room and the places in the reservation. The system saves every card entry.

## Scope
- Check-in with card assignment.
- Card access to room, restaurant and pool.
- Entry log (place and time).
- Manager reports: places of one card, card passes of one room.

## Out of scope
- Physical card reader hardware.

## Actors
- Receptionist, client, manager.

## Value
The hotel controls who enters each room and zone, and the manager can audit card use.

## Source
Statement, section 2 (cartão magnético). Requirements RF-12, RF-13, RF-14.

## Candidate stories
- Realizar check-in com cartão magnético
- Registar entradas do cartão
- Consultar locais visitados por um cartão
- Consultar passagens de cartões num quarto

## Open questions
- Can a client have more than one card? (teacher question 9)
