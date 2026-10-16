---
status: draft
sprint: 1
---

# Actors - H4Y (Hotel4You)

Source: `enunciado.md`, section 2. Items not in the statement are not listed. Team decisions (user) are marked and not confirmed by the teacher. Open points: see `questions.md`.

| Actor | Description | Allowed actions | Not allowed (from statement) |
|-------|-------------|-----------------|------------------------------|
| Client | Guest of the hotel. Uses all services of the hotel. Gets a magnetic card after check-in. | Choose a free room type for one or more nights (RF-05). Use room, restaurant and pool with the card (RF-17, RF-18). Use services that can have an extra cost (RF-09; private pools, spa and conference rooms are paid extras in the reservation and bill, Q-20). Uses the magnetic card only, no login (Q-13, team decision). | Use the restaurant or pool when they are not in the reservation (RF-18). Open private pools, spa or conference rooms with the card (Q-20, team decision). Delete reservations (RF-22; Q-05). |
| Receptionist (reception staff) | Hotel staff on floor 0. Supports clients in all they need. | Register clients (RF-10). Make and cancel reservations (RF-11, RF-12; cancel is a state change, Q-05). Do check-in and check-out (RF-13, RF-14). Help with room services (RF-15; scope Q-25). Record client suggestions and complaints in the app (RF-27; Q-18; scope Q-25). Record stock (RF-29, as staff). Log in (RF-01). | Delete reservations (RF-22; only managers; Q-05). Access outside floors 0 and 1 (RF-24; Q-08). Enter guest rooms without a manager master card (Q-08, team decision). |
| Manager | Makes sure the hotel works well. | Create, edit and remove staff accounts. No schedules (RF-26; Q-18, team decision). Receive suggestions and complaints (RF-27). Configure room items (RF-08). Set base room price per type and fixed extra prices per night (Q-11, team decision). Delete reservations before check-in, for good (RF-22; Q-05). Use the master card (one per manager) for all rooms and all zones (RF-23; Q-19). Block a lost master card and issue a new one; the block is logged (RF-30; Q-19, team decision). Get the places of one card (RF-20). Get the card passes of one room (RF-21). Record stock (RF-29). Log in (RF-01). | Delete a reservation after check-in (RF-22). |
| Restaurant staff (external company) | Employee of the external company that provides the restaurant service. | Log in with user name and password (RF-01; Q-23, team decision). Access the kitchen and the restaurant (RF-25). | Access anything other than the kitchen and the restaurant (RF-25). Delete reservations (RF-22; Q-05). |
| Supplier | Company that delivers catering, hygiene material, beds, sofas, sheets and other room material. | Send an XML file with the material of a delivery (RF-28). Staff or manager loads the file (Q-17, team decision). | Log in to the system (Q-17, team decision). Other system access is not stated. |

## Notes

- RF-24 (floors 0 and 1 only) applies to reception staff only (Q-08, team decision).
- Other hotel staff (for example cleaning) are not named in the statement. Q-08 does not cover them.
- The master magnetic card is held by the manager (RF-23). One master card per manager (Q-19, team decision). The manager can block a lost master card and issue a new one. What the block does at the doors is open (Q-26).
- Restaurant staff log in with user name and password (Q-23, team decision).
- Staff management covers staff accounts only, with no schedules (Q-18, team decision).

AI use: requirements-analyst, Claude Code (claude-sonnet-5-5), 2026-10-15; update of answers Q-01 to Q-23 by requirements-analyst, Claude Code (claude-haiku-5-5), 2026-10-15; update of answers Q-03 to Q-26 by requirements-analyst, Claude Code (claude-haiku-5-5), 2026-10-16. Reviewed by: pending.
