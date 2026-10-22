---
status: draft
sprint: 1
---

# Use cases - H4Y (Hotel4You)

Source: `enunciado.md`, section 2. Flows only use steps the statement supports, or team decisions from `questions.md` (marked, not confirmed by the teacher). Phase: "1st delivery" or "final delivery" (Q-06); phase not named in Q-06 = "planned by the prioritizer; phase set in the backlog order" (Q-25). Open points are in `questions.md`.

## UC-01 Log in
- Actors: receptionist, manager, restaurant staff.
- RF links: RF-01, RF-02, RNF-02.
- Phase: 1st delivery (Q-06).
- Main flow:
  1. The user opens the application.
  2. The user enters user name and password.
  3. The system checks the data against the saved user (password stored encrypted).
  4. The system opens the functions allowed for the role.
- Alternative flows:
  - 3a. User name or password is wrong. The system refuses access with the same message for both cases.
  - Clients do not log in; they use the magnetic card (Q-13, team decision).
  - Supplier does not log in (Q-17, team decision).
  - Restaurant staff log in with user name and password (Q-23, team decision).

## UC-02 Register client
- Actors: receptionist.
- RF links: RF-10, RNF-03.
- Phase: planned by the prioritizer; phase set in the backlog order (Q-25).
- Main flow:
  1. The receptionist opens client registration.
  2. The receptionist enters the client data.
  3. The system saves the client.
- Alternative flows:
  - 3a. Data is not valid. The system shows the error and does not save.
  - Registration plus reservation must take 30 seconds or less (Q-12, team decision). Timing counts from opening client registration to reservation created. Room choice time is not counted (Q-29, team decision).
  - Client data fields are not in the statement. The team defines them in the stories.

## UC-03 Make reservation
- Actors: receptionist.
- RF links: RF-05, RF-06, RF-07, RF-09, RF-11, RF-18.
- Phase: 1st delivery (reservations, Q-06). Card use of the reserved places: final delivery (RF-18). Extras (RF-07, RF-09): planned by the prioritizer (Q-25).
- Main flow:
  1. The receptionist selects a registered client.
  2. The receptionist selects the room type and the nights.
  3. The system shows the free rooms of that type.
  4. The receptionist selects a room, extras and, if needed, restaurant, pool and other paid services. Private pools, spa and conference rooms are paid extras (Q-20, team decision).
  5. The system shows the price and saves the reservation.
- Alternative flows:
  - 3a. No free room of that type. The system tells the receptionist. The receptionist selects another type or other nights.
  - Special view: one choice per reservation, sea or mountain, not both (Q-37, team decision).
  - Change after reservation: extras (RF-07, RF-09) can change until check-in. Dates and room cannot change: cancel and create a new reservation (Q-40, team decision).

## UC-04 Cancel reservation
- Actors: receptionist.
- RF links: RF-12.
- Phase: 1st delivery (reservations, Q-06).
- Main flow:
  1. The receptionist finds the reservation.
  2. The receptionist cancels it.
  3. The system saves the cancellation as a state change. The reservation stays in history (Q-05, team decision).
- Alternative flows:
  - Cancel after check-in is not possible (Q-40, team decision). The system refuses it. Reservation changes: see UC-03 (Q-40).

## UC-05 Delete reservation
- Actors: manager.
- RF links: RF-22.
- Phase: 1st delivery (reservations, Q-06).
- Main flow:
  1. The manager finds the reservation.
  2. The system checks that the client has not done check-in.
  3. The manager deletes the reservation for good (Q-05, team decision).
- Alternative flows:
  - 2a. Check-in is done. The system refuses the deletion.
  - 1a. The user is not a manager. The system refuses the action.

## UC-06 Check-in
- Actors: receptionist.
- RF links: RF-13, RF-16, RF-17, RF-18.
- Phase: check-in in 1st delivery (Q-06). Steps 3 and 4 (card): final delivery.
- Main flow:
  1. The receptionist finds the reservation of the client.
  2. The receptionist does the check-in.
  3. The system gives a magnetic card with a number.
  4. The system links the card to the room and to the places in the reservation (restaurant, pool).
- Alternative flows:
  - 1a. No reservation found. The receptionist cannot do the check-in.
  - One card per reservation (Q-09, team decision).

## UC-07 Check-out
- Actors: receptionist.
- RF links: RF-14.
- Phase: 1st delivery (Q-06).
- Main flow:
  1. The receptionist finds the stay of the client.
  2. The receptionist does the check-out.
  3. The system saves the check-out.
- Alternative flows:
  - Card stops working at check-out. No door opens. The log is kept (Q-30, team decision).
  - Charges at check-out are not answered (Q-11).

## UC-08 Help with room services
- Actors: receptionist, client.
- RF links: RF-15, RF-07, RF-09.
- Phase: planned by the prioritizer; phase set in the backlog order (Q-25).
- Main flow:
  1. The client asks reception for a room service.
  2. The receptionist records the service: food, drink or laundry (Q-33, team decision).
  3. The system adds the extra cost to the stay, when the service has one.
- Alternative flows:
  - Staff enter guest rooms only with a manager master card (Q-08, team decision).
  - Price of extra services: fixed extra price per night, set by the manager (Q-11, team decision).

## UC-09 Use magnetic card at a place
- Actors: client, manager (master card).
- RF links: RF-17, RF-18, RF-19, RF-23, RF-24.
- Phase: final delivery (Q-06).
- Main flow:
  1. The card holder puts the card at a room, restaurant or pool.
  2. The system checks that the card may enter the place.
  3. The system opens the place.
  4. The system saves a record with the place and the entry time.
- Alternative flows:
  - 2a. The place is the restaurant or pool and it is not in the reservation. The system does not open it.
  - 2b. The card is the master card. The system opens any room or zone. Every pass is logged (Q-19, team decision).
  - 2c. The place is the bar, a conference room, the garage, the storeroom, a private pool or the spa. The card does not open it (Q-20, team decision).
  - 2d. The zone is marked as needing a reservation to open with the card (Q-32, team decision). The card opens it only if the reservation includes it.

## UC-10 List places of a card
- Actors: manager.
- RF links: RF-19, RF-20.
- Phase: final delivery (Q-06).
- Main flow:
  1. The manager enters a card number.
  2. The system shows each place and entry time of that card.
- Alternative flows:
  - 2a. The card has no records. The system shows an empty list.

## UC-11 List card passes of a room
- Actors: manager.
- RF links: RF-19, RF-21, RF-23.
- Phase: final delivery (Q-06).
- Main flow:
  1. The manager enters a room number.
  2. The system shows each card pass of that room with the card number and time. Master card passes are included.
- Alternative flows:
  - 2a. The room has no records. The system shows an empty list.

## UC-12 Configure room items
- Actors: manager.
- RF links: RF-08.
- Phase: 1st delivery (rooms, Q-06).
- Main flow:
  1. The manager opens the room configuration.
  2. The manager adds, changes or removes room items.
  3. The system saves the configuration.
- Alternative flows:
  - 3a. Data is not valid. The system shows the error and does not save.

## UC-13 Manage staff
- Actors: manager.
- RF links: RF-26, RF-01, RF-24, RF-25.
- Phase: planned by the prioritizer; phase set in the backlog order (Q-25).
- Main flow:
  1. The manager opens staff management.
  2. The manager creates, edits or removes a staff account (accounts only, Q-18, team decision).
  3. The system saves the result.
- Alternative flows:
  - Schedules and other staff actions are not in scope (Q-18, team decision).
  - Users list: only the manager consults users, at any time (Q-39, team decision).

## UC-14 Receive suggestion or complaint
- Actors: receptionist, manager, client.
- RF links: RF-27.
- Phase: planned by the prioritizer; phase set in the backlog order (Q-25).
- Main flow:
  1. The client gives a suggestion or complaint to reception.
  2. The receptionist records it in the app (Q-18, team decision).
  3. The manager receives it.

## UC-15 Enter supplier delivery in stock
- Actors: supplier (delivers, does not log in), receptionist or other hotel staff, manager.
- RF links: RF-28, RF-29.
- Phase: final delivery (Q-06).
- Main flow:
  1. The supplier delivers goods and sends the XML file.
  2. Staff or the manager loads the XML file into the system (Q-17, team decision).
  3. The system checks permission and validates the XML. For every item in a valid file, it finds or creates the catalogue item and adds the delivered quantity to stock (Q-10, Q-38; PR #20 decision, 2026-10-21).
  4. The system saves the import, catalogue changes, ENTRY movements and balances together. It shows the import report.
- Alternative flows:
  - 3a. The file is not valid XML. The system refuses it and records nothing.
  - 3b. The caller has no permission. The system refuses the import and changes no data.
  - 4a. A save fails. The system undoes the whole import, including new catalogue items, and keeps the original stock.
  - A missing catalogue item is created, not rejected. There is no preview, cancellation or confirmation stage in this flow (PR #20 decision, 2026-10-21, confirmed by the user on 2026-10-22).

## UC-16 Record stock use and exit
- Actors: receptionist, manager.
- RF links: RF-29.
- Phase: final delivery (Q-06).
- Main flow:
  1. Staff or the manager selects a stock item.
  2. The user records a use or an exit with the quantity and the date (Q-21, team decision). Use = consumption in the hotel. Exit = removal (discard, return, loss). Both lower the balance (Q-34, team decision).
  3. The system saves the record and updates the stock.
- Alternative flows:
  - 3a. Quantity is more than the stock. Behavior is not in the statement and not answered by Q-21.

## UC-17 Restricted access for external restaurant staff
- Actors: restaurant staff.
- RF links: RF-25, RF-01.
- Phase: planned by the prioritizer; phase set in the backlog order (Q-25).
- Main flow:
  1. The restaurant employee logs in with user name and password (UC-01; Q-23, team decision).
  2. The employee enters the kitchen or the restaurant.
- Alternative flows:
  - 2a. The employee tries any other place. The system refuses access.

## UC-18 Block lost master card and issue a new one
- Actors: manager.
- RF links: RF-30, RF-23.
- Phase: final delivery (Q-06).
- Main flow:
  1. The manager opens the master card function.
  2. The manager blocks the lost master card. The block is immediate: the blocked card opens nothing.
  3. The system saves the block in the log.
  4. The manager issues a new master card. The new master card has a new number.
  5. The system keeps the old card number in the log.
- Alternative flows:
  - Block effect and new card number: team decision, not confirmed by the teacher (Q-26).
  - The new master card is issued only after the old master card is blocked (Q-31, team decision).

## UC-19 Manage stock catalogue
- Actors: manager.
- RF links: RF-28, RF-29.
- Phase: final delivery (Q-06).
- Main flow:
  1. The manager opens the stock catalogue.
  2. The manager creates or edits a stock item with its name and unit.
  3. The system saves the item in the catalogue.
- Alternative flows:
  - 2a. Data is not valid. The system shows the error and does not save.
  - XML import (UC-15) accepts all items from a valid file and creates missing catalogue items (Q-10, Q-38; PR #20 decision, 2026-10-21). Manual catalogue management remains a manager action.
  - Stock use and exit (UC-16) use catalogue items (Q-38, team decision).
  - Removal of a catalogue item is not in the team decision (Q-38). If it is added, Q-36 applies.

Created by: BorjaTiago. Updated by: pedrocostaisep. AI use: requirements-analyst, Claude Code (claude-sonnet-5-5), 2026-10-15; update of answers Q-01 to Q-23 by requirements-analyst, Claude Code (claude-haiku-5-5), 2026-10-15; update of answers Q-03 to Q-26 by requirements-analyst, Claude Code (claude-haiku-5-5), 2026-10-16; edit of UC-03, UC-18 by requirements-analyst, Claude Code (claude-haiku-5-5), 2026-10-16; notes of UC-02, UC-07, UC-08, UC-09, UC-16, UC-18 for Q-29 to Q-34 by requirements-analyst, Claude Code (claude-haiku-5-5), 2026-10-16; notes of UC-03, UC-04, UC-13, UC-15 and new UC-19 for Q-37 to Q-40 by requirements-analyst, Claude Code (claude-haiku-5-5), 2026-10-16; edit of UC-16 actor by OpenAI Codex, GPT-6, 2026-10-16; PR-20 import decision review by sequence-diagram-editor, OpenAI Codex (GPT-6), 2026-10-22. Reviewed by: pending.
