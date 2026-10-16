---
status: approved
sprint: 1
---

# Requirements - H4Y (Hotel4You)

Source: `.agent/context/enunciado.md` (sections 1 to 7). Open points are in `questions.md` (Q-NN). Team decisions (user) are marked in the Notes. None is confirmed by the teacher. Delivery phase: "1st delivery" or "final delivery" (Q-06). Phase not named in Q-06 = "planned by the prioritizer; phase set in the backlog order" (Q-25, team decision).

## Functional requirements

| ID | Text | Source section | Priority hint | Notes |
|----|------|----------------|---------------|-------|
| RF-01 | Hotel staff, restaurant staff and managers log in to the application with a user name and a password. Clients do not log in; they use the magnetic card. The supplier does not log in. | 2 | must | Phase: 1st delivery (Q-06). Changed from "all users log in" by Q-13 and Q-17. Restaurant staff login: Q-23 (team decision). |
| RF-02 | The system knows five actor types: client, receptionist, manager, restaurant staff (external company), supplier. Each type has its own access. | 2 | must | Phase: 1st delivery (Q-06, login roles). Needed for every access rule. See `actors.md`. Supplier does not log in (Q-17). |
| RF-03 | The system models the building: floor -1 (garage, storeroom), floor 0 (reception, bar, conference rooms), floor 1 (restaurant, swimming pool), floors 2 and 3 (sleeping rooms). | 2 | must | Phase: 1st delivery for the floor model (rooms). Zone use by the card: final delivery (Q-06). The manager creates, edits and removes zones per floor, and marks if a zone needs a reservation to open with the card (Q-32, team decision). |
| RF-04 | The system keeps rooms of three types: single, double, family (3 or 4 people). The hotel has 20 rooms in total: 10 on floor 2 and 10 on floor 3. Split for the whole hotel: 7 single, 10 double, 3 family. | 2 | must | Phase: 1st delivery (rooms, Q-06). Room count and split: Q-01 (team decision, not confirmed by the teacher). |
| RF-05 | A client can choose any room type, if the room is free, for one or more nights. | 2 | must | Phase: 1st delivery (reservations, Q-06). Testable: a free room is offered, an occupied room is not. |
| RF-06 | The price of a room per night changes by room type and by extra room services. | 2 | must | Phase: 1st delivery (rooms, Q-06). Manager sets the base price per room type (Q-11, team decision). |
| RF-07 | These items add to the base room price: special view (sea or mountain), drink consumption, laundry. | 2 | must | Phase: planned by the prioritizer; phase set in the backlog order (Q-25). Manager sets fixed extra prices per night (Q-11, team decision). Drink and laundry are also room service types (Q-33, team decision). Special view: one choice per reservation, sea or mountain, not both (Q-37, team decision). |
| RF-08 | The manager configures the room items (towels, shower conditioner, shampoo, hair dryer, television, air conditioning, bed, sofa, sheets, safe, and so on). | 2 | should | Phase: 1st delivery (rooms, Q-06). Statement says "can configure all of these". |
| RF-09 | The hotel offers other services (food, cultural events, access to private pools, spa, conference rooms). Each can have an extra cost for the client. | 2 | should | Phase: planned by the prioritizer; phase set in the backlog order (Q-25). Fixed extra price per night set by the manager (Q-11). Private pools, spa and conference rooms are paid extra services in the reservation and bill, with no card access (Q-20, team decision). |
| RF-10 | Reception staff register clients. | 2 | must | Phase: 1st delivery (Q-27, team decision, user, 2026-10-16): reservations need the client. Time limit: RNF-03. |
| RF-11 | Reception staff make client reservations. | 2 | must | Phase: 1st delivery (reservations, Q-06). Reservation can include restaurant and pool (see RF-18). Change: extras (RF-07, RF-09) can change until check-in. Dates and room cannot change: cancel and create a new reservation (Q-40, team decision). |
| RF-12 | Reception staff cancel client reservations. | 2 | must | Phase: 1st delivery (reservations, Q-06). Cancel is a state change, kept in history (Q-05, team decision). Delete is only for the manager (RF-22). Cancel is not possible after check-in (Q-40, team decision). |
| RF-13 | Reception staff do client check-in. After check-in the client gets a magnetic card. | 2 | must | Phase: check-in in 1st delivery (Q-06). Card issue: final delivery (RF-16). |
| RF-14 | Reception staff do client check-out. | 2 | must | Phase: 1st delivery (Q-06). Card state at check-out: the card stops working, no door opens, the log is kept (Q-30, team decision). |
| RF-15 | Reception staff help the client with room services. | 2 | should | Phase: planned by the prioritizer; phase set in the backlog order (Q-25). Staff enter guest rooms only with a manager master card (Q-08, team decision). Request types: food, drink, laundry (Q-33, team decision). |
| RF-16 | After check-in the system gives the client a magnetic card. The card has a number. One card per reservation. | 2 | must | Phase: final delivery (Q-06). One card per reservation (Q-09, team decision). The card stops working at check-out (Q-30, team decision). |
| RF-17 | The magnetic card opens the client room. | 2 | must | Phase: final delivery (Q-06). Confirmed by Q-09 (team decision). |
| RF-18 | The magnetic card opens the restaurant and the pool only if these places are part of the reservation. The card does not open the bar, conference rooms, garage, storeroom, private pools or spa. | 2 | must | Phase: final delivery (Q-06). Q-09 and Q-20 (team decision). Zone rule: the manager marks if a zone needs a reservation to open with the card (Q-32, team decision). |
| RF-19 | The system saves a record for each card entry. The record has the place and the entry time. | 2 | must | Phase: final delivery (Q-06). Each pass is logged, including room entries (Q-09, team decision). |
| RF-20 | The manager gets the list of places of one given card. | 2 | must | Phase: final delivery (Q-06). Example in statement: card 12345. |
| RF-21 | The manager gets the list of card passes of one given room. The list includes the master card. | 2 | must | Phase: final delivery (Q-06). Example in statement: room 301. |
| RF-22 | Only managers delete reservations. They delete only if the client has not done check-in. Delete removes the reservation for good (Q-05, team decision). | 2 | must | Phase: 1st delivery (reservations, Q-06). Other roles must be refused. |
| RF-23 | The manager opens all client rooms and enters all zones of the hotel with the master magnetic card. | 2 | must | Phase: final delivery (Q-06). One master card per manager. Every pass is logged (Q-19, team decision). Block and new card: RF-30. |
| RF-24 | Reception staff access only the services of floors 0 and 1. | 2 | must | Phase: planned by the prioritizer; phase set in the backlog order (Q-25). Limit applies to reception only (Q-08, team decision). Restaurant staff: RF-25. |
| RF-25 | Each employee of the external restaurant company accesses only the kitchen and the restaurant. | 2 | must | Phase: planned by the prioritizer; phase set in the backlog order (Q-25). Login: user name and password (Q-23, team decision). Nothing else. |
| RF-26 | The manager manages the staff: creates, edits and removes staff accounts. | 2 | must | Phase: planned by the prioritizer; phase set in the backlog order (Q-25). Accounts only, no schedules (Q-18, team decision). |
| RF-27 | Reception records client suggestions and complaints in the app. The manager receives them. | 2 | should | Phase: planned by the prioritizer; phase set in the backlog order (Q-25). Team decision (Q-18). |
| RF-28 | A supplier delivery comes with an XML file. Staff or the manager loads the file. The system reads the file and enters the listed material in the stock. Unknown items are rejected and listed in the import report. Valid items are imported. | 2 | must | Phase: final delivery (Q-06). Supplier does not log in (Q-17). XML format defined by the team; unknown item rule (Q-10, team decision). Stock catalogue: the manager creates and edits items (name, unit). The XML accepts only catalogue items; an item not in the catalogue is unknown (Q-38, team decision; Q-10 unchanged). Use case: UC-19. |
| RF-29 | Hotel staff or managers record all stock management in the system: entry, use and exit. Each movement has a quantity and a date. | 2 | must | Phase: final delivery (Q-06). Stock covers all supplier items. Movements with quantity and date (Q-21, team decision). Use = consumption in the hotel. Exit = removal (discard, return, loss). Both lower the balance (Q-34, team decision). Stock items come from the stock catalogue (Q-38, team decision). |
| RF-30 | The manager blocks a lost master card and issues a new master card with a new number. The block is immediate: a blocked card opens nothing. Each block is logged. The old number stays in the log. | 2, Q-19, Q-26 (team decision) | must | Phase: final delivery (card, Q-06). Linked to the master card (RF-23). Block effect and new number: Q-26 (team decision, not confirmed by the teacher). Block before new card issue: Q-31 (team decision). |

## Non-functional requirements

| ID | Text | Source section | Priority hint | Notes |
|----|------|----------------|---------------|-------|
| RNF-01 | The system saves all users. Any user can be accessed at any time. | 3a | must | Users list: only the manager consults users, at any time (Q-39, team decision). |
| RNF-02 | Critical data (passwords) are saved encrypted. | 3b | must | Security. |
| RNF-03 | The registration process is easy and fast: 30 seconds maximum for client registration plus reservation. | 3c | must | Measurable. Scope: Q-12 (team decision). Timing: 30 s from opening client registration to reservation created. Room choice time is not counted (Q-29, team decision). |
| RNF-04 | The system is simple and intuitive for all actors in their diversity. | 3c | should | Check with usability test on the GUI. The usability test is a technical task, not a story (Q-41, team decision, follows Q-35). |
| RNF-05 | The software is written in Java (interoperability). | 1, 2, 3c | must | |
| RNF-06 | A GUI in JavaFX is recommended. | 3c | should | "Recommended", not mandatory. Team decision: JavaFX GUI from the 1st delivery (Q-22). |
| RNF-07 | Business logic and GUI logic are clearly separated. | 3c | must | Team decision (Q-22). |
| RNF-08 | The class structure supports maintenance and increments in a simple way. | 1, 3c | must | Also: architecture supports increments of required functions (section 1). |
| RNF-09 | The code is documented with Javadoc. | 3c | must | |
| RNF-10 | The implementation includes JUnit unit tests for the main functions. | 3c | must | |
| RNF-11 | The team justifies every external library it uses. | 3c | must | Justification file: docs/libraries.md (name, version, reason, rejected alternative). Process: Q-14 (team decision, user, 2026-10-16), not confirmed by the teacher. |
| RNF-12 | The database management system is SQL Server. | 2 | must | |
| RNF-13 | The team works with Scrum in sprints. The project lasts 16 weeks, as 13 sprints. | 1, 4 | must | Sprint 1: 2026-10-12 to 2026-10-18. Sprint length: 1 week (Q-03). Sprints 2 to 10: weeks of 10-19 to 12-14. No sprint in the weeks of 2026-12-21, 2026-12-28, 2027-01-04 (Christmas and New Year, Q-04, team decision, user, 2026-10-16). Sprints 11 to 13: weeks of 2027-01-11 to 2027-01-25. Sprint 13 ends 2027-01-31 (final delivery day). |
| RNF-14 | Sprint 1: read the statement and write the Requirements Engineering documents for sprint 2. In sprint 1 no responsibilities go to members. | 4 | must | Q-24 (team decision, user, 2026-10-16): follow RNF-14. Sprint 1 has no Team Leader. |
| RNF-15 | The team does and documents these ceremonies: Sprint Review (DEMO to the teacher), Sprint Retrospective (keep, stop, start), Grooming / Backlog Refinement, Sprint Planning, Daily Scrum. | 4 | must | |
| RNF-16 | Each team does a Daily Scrum at the start of each practical class. | 4 | must | |
| RNF-17 | The team has 1 Team Leader (Scrum Master and Product Owner) and development members. The responsibilities change in each sprint. | 1 | must | Team size: 5 members, all developers. The Team Leader also develops (Q-02, team decision, user, 2026-10-16). Roster and rotation: `.agent/context/team.md`. Leader order from sprint 2: Q-07 (team decision). |
| RNF-18 | Team Leader duties: update and validate software documents (JavaDocs, UML, Scrum), create user stories in the backlog, test and validate delivered tasks against acceptance tests, make processes and ceremonies agile, make sure the team works well. | 4 | must | |
| RNF-19 | Development team duties: develop and test the stories of the sprint, update the diagrams. | 4 | must | |
| RNF-20 | Code control uses Git flow. Branch name: (feat/fix)-(task_number)-description. Example: feat-004-create-database. | 4 | must | Team decision (Q-16): `feat-NNN-description` for docs and chore too (NNN = H4Y number). |
| RNF-21 | Commits have clear and short descriptions. Descriptions like "fix task X" or "general fix" are not allowed. | 4 | must | Incorrect use of the repository gives a penalty. |
| RNF-22 | 1st delivery is due 2026-12-01 at 23:55. Final delivery is due 2027-01-31 at 23:55. Teams submit on Moodle. | 5 | must | 1st delivery scope (Q-06, team decision): login, rooms, reservations, check-in and check-out. Card, card log, stock and XML: final delivery. Items not named: Q-25. |
| RNF-23 | Each delivery is a zip file named LP3-GY.zip (Y = group number). | 5 | must | |
| RNF-24 | The zip contains: application executables, SCRUM documentation, domain model, class diagram, use case diagram, sequence diagrams (at least one general and one detailed), Javadoc output, unit tests, source code. | 5 | must | |
| RNF-25 | Every use of AI code or engineering diagrams has a reference. No one claims authorship of work that is not their own. | 7 | must | Reference format: Q-15 (open). |
| RNF-26 | Code is not shared between groups. A missed deadline gives a penalty or the delivery is not accepted. | 7 | must | |
| RNF-27 | Evaluation weights: 1st delivery 35%, final delivery 65%. Each delivery: SCRUM practices 20%, Documentation 20%, Source code 25%, Application operation 35%. | 6 | should | Guides priority. Not a product feature. |

## Statement coverage

| Checklist item | Source section | IDs |
|----------------|----------------|-----|
| Roles: client, receptionist, manager, restaurant staff (external), supplier | 2 | RF-01, RF-02, RF-24, RF-25 |
| Staff only on floors 0 and 1 | 2 | RF-24 |
| Restaurant staff only in kitchen and restaurant | 2 | RF-25 |
| Manager master card for all rooms and zones | 2 | RF-23 |
| Login for all users (user name and password) | 2 | RF-01 |
| Rooms: types, floors | 2 | RF-03, RF-04, RF-05 |
| Rooms: prices by type and extras | 2 | RF-06, RF-07 |
| Rooms: items the manager configures | 2 | RF-08 |
| Services with extra cost | 2 | RF-07, RF-09 |
| Reception: client registration | 2 | RF-10 |
| Reception: make and cancel reservations | 2 | RF-11, RF-12 |
| Reception: check-in and check-out | 2 | RF-13, RF-14 |
| Reception: room services | 2 | RF-15 |
| Magnetic card: given at check-in, card number | 2 | RF-13, RF-16 |
| Magnetic card: opens room | 2 | RF-17 |
| Magnetic card: restaurant and pool only if in the reservation | 2 | RF-18 |
| Magnetic card: master card lost or blocked (Q-19) | 2, Q-19 | RF-30 |
| Card access log: place and entry time | 2 | RF-19 |
| Manager list: places of one card | 2 | RF-20 |
| Manager list: card passes of one room (master card included) | 2 | RF-21 |
| Reservation delete: only managers, only before check-in | 2 | RF-22 |
| Manager manages staff | 2 | RF-26 |
| Manager receives suggestions and complaints | 2 | RF-27 |
| Stock: supplier XML file gives the entry | 2 | RF-28 |
| Stock: staff or managers record entry, use and exit | 2 | RF-29 |
| NFR: all users saved and accessible | 3a | RNF-01 |
| NFR: encrypted passwords | 3b | RNF-02 |
| NFR: registration in 30 seconds or less | 3c | RNF-03 |
| NFR: simple GUI | 3c | RNF-04 |
| NFR: Java | 1, 2, 3c | RNF-05 |
| NFR: JavaFX GUI recommended | 3c | RNF-06 |
| NFR: business logic separate from GUI | 3c | RNF-07 |
| NFR: maintainable classes | 1, 3c | RNF-08 |
| NFR: Javadoc | 3c | RNF-09 |
| NFR: JUnit for main functions | 3c | RNF-10 |
| NFR: justified external libraries | 3c | RNF-11 |
| NFR: SQL Server | 2 | RNF-12 |
| Process: Scrum ceremonies | 4 | RNF-13, RNF-14, RNF-15, RNF-16 |
| Process: Team Leader rotation | 1, 4 | RNF-17, RNF-18, RNF-19 |
| Process: Git flow branch and commit rules | 4 | RNF-20, RNF-21 |
| Deliverables: executables, Scrum docs, domain model, class diagram, use case diagram, sequence diagrams, Javadoc output, unit tests, source code | 5 | RNF-24 |
| Deliverables: zip `LP3-GY.zip` | 5 | RNF-23 |
| Deliverables: dates | 5 | RNF-22 |
| Code of conduct: reference every AI use in code and diagrams | 7 | RNF-25, RNF-26 |

Gaps: none. Every checklist item has at least one ID.

AI use: requirements-analyst, Claude Code (claude-sonnet-5-5), 2026-10-15; update of answers Q-01 to Q-23 by requirements-analyst, Claude Code (claude-haiku-5-5), 2026-10-15; update of answers Q-03 to Q-26 by requirements-analyst, Claude Code (claude-haiku-5-5), 2026-10-16; edit of RF-07, RF-09, RF-30, RNF-14 by requirements-analyst, Claude Code (claude-haiku-5-5), 2026-10-16; edit of RNF-11, RNF-13, RNF-17 by requirements-analyst, Claude Code (claude-haiku-5-5), 2026-10-16; notes of RF-03, RF-07, RF-14, RF-15, RF-16, RF-18, RF-29, RF-30, RNF-03 for Q-29 to Q-34 by requirements-analyst, Claude Code (claude-haiku-5-5), 2026-10-16; notes of RF-07, RF-11, RF-12, RF-28, RF-29, RNF-01, RNF-04 for Q-37 to Q-41 by requirements-analyst, Claude Code (claude-haiku-5-5), 2026-10-16. RNF-13 note for Q-04 by doc-editor, Claude Code (claude-haiku-5-5), 2026-10-16. Reviewed by: pending.
