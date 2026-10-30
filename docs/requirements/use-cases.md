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
  4. The system shows the functions of the role (UC-20, included).
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
  1. The receptionist selects a registered client (UC-33, included).
  2. The receptionist selects the room type and the nights.
  3. The system shows the free rooms of that type.
  4. The receptionist selects a room (UC-34, included) and, if needed, extras (UC-36), restaurant and pool (UC-38) and other paid services (UC-37). Private pools, spa and conference rooms are paid extras (Q-20, team decision).
  5. The system shows the price (UC-35, included) and saves the reservation.
- Alternative flows:
  - 1a. The client is not registered. The receptionist registers the client (UC-02 extends UC-03).
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
  2. The system checks that the client has not done check-in (UC-51, included).
  3. The manager deletes the reservation for good (Q-05, team decision).
- Alternative flows:
  - 2a. Check-in is done. The system refuses the deletion.
  - 1a. The user is not a manager. The system refuses the action.

## UC-06 Check-in
- Actors: receptionist.
- RF links: RF-13, RF-16, RF-17, RF-18.
- Phase: check-in in 1st delivery (Q-06). Step 3 (card): final delivery.
- Main flow:
  1. The receptionist finds the reservation of the client. The system checks that the client has a reservation (UC-52, included).
  2. The receptionist does the check-in.
  3. The system issues the guest card (UC-39, included).
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
  4. The system deactivates the guest card (UC-40, included).
- Alternative flows:
  - Charges at check-out are not answered (Q-11).

## UC-08 Record room service request
- Actors: receptionist (primary), client (secondary).
- RF links: RF-15, RF-07, RF-09.
- Phase: planned by the prioritizer; phase set in the backlog order (Q-25).
- Main flow:
  1. The client asks reception for a room service.
  2. The receptionist records the service: food, drink or laundry (Q-33, team decision).
  3. The system adds the extra cost to the stay, when the service has one.
- Alternative flows:
  - Staff enter guest rooms only with a manager master card (Q-08, team decision).
  - Price of extra services: fixed extra price per night, set by the manager (Q-11, team decision).

## UC-09 Open room with guest card
- Actors: client.
- RF links: RF-17, RF-19.
- Phase: final delivery (Q-06).
- Main flow:
  1. The client puts the card at the reserved room.
  2. The system checks that the card is active and linked to that room.
  3. The system opens the room.
  4. The system records the pass (UC-44, included).
- Alternative flows:
  - 2a. The card is for another room, or the card is not active (after check-out, Q-30). The system does not open the room.

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

## UC-13 Create staff account
- Actors: manager.
- RF links: RF-26, RF-01, RNF-02.
- Phase: planned by the prioritizer; phase set in the backlog order (Q-25).
- Main flow:
  1. The manager opens staff management.
  2. The manager enters the name, user name, password and role of the new account (accounts only, Q-18, team decision).
  3. The system saves the account. The password is saved as a hash (Q-46, Q-52).
- Alternative flows:
  - 3a. The user name exists. The system refuses the account.
  - 3b. The password is blank. The system refuses the account (Q-51).
  - Schedules and other staff actions are not in scope (Q-18, team decision).
  - Edit, remove and list: UC-22, UC-23, UC-24.

## UC-14 Record suggestion or complaint
- Actors: receptionist (primary), client (secondary).
- RF links: RF-27.
- Phase: planned by the prioritizer; phase set in the backlog order (Q-25).
- Main flow:
  1. The client gives a suggestion or complaint to reception.
  2. The receptionist records it in the app (Q-18, team decision).
  3. The system saves it. The manager reads it with UC-41.

## UC-15 Enter supplier delivery in stock
- Actors: supplier (delivers, does not log in), receptionist or other hotel staff, manager.
- RF links: RF-28, RF-29.
- Phase: final delivery (Q-06).
- Main flow:
  1. The supplier delivers goods and sends the XML file.
  2. Staff or the manager loads the XML file into the system (Q-17, team decision).
  3. The system checks permission and reads and validates the XML (UC-53, included).
  4. The system shows a preview of the items and quantities in the file. The user confirms or cancels (teacher feedback, 2026-10-30, Jira H4Y-8).
  5. The user confirms. The system checks permission and the data again. For every item in the file, it finds or creates the catalogue item and records a stock entry for the delivered quantity (UC-50, included; Q-10, Q-38; PR #20 decision, 2026-10-21).
  6. The system saves the import, catalogue changes, ENTRY movements and balances together. It shows the import report (UC-47, included).
- Alternative flows:
  - 3a. The file is not valid XML. The system refuses it and records nothing.
  - 3b. The caller has no permission. The system refuses the import and changes no data.
  - 4a. The user cancels. The system closes the preview and changes no data.
  - 6a. A save fails. The system undoes the whole import, including new catalogue items, and keeps the original stock.
  - A missing catalogue item is created, not rejected (PR #20 decision, 2026-10-21, confirmed by the user on 2026-10-22). The preview with confirm and cancel replaces the "no preview" part of that decision (teacher feedback, 2026-10-30, Jira H4Y-8).
  - Difference from UC-50: UC-15 handles the full supplier delivery. It reads the XML file (UC-53), creates missing catalogue items, records one entry for each item with UC-50 and shows the report (UC-47). UC-50 alone is one manual entry of an existing item.

## UC-16 Record stock use
- Actors: receptionist, manager.
- RF links: RF-29.
- Phase: final delivery (Q-06).
- Main flow:
  1. Staff or the manager selects a stock item.
  2. The user records a use with the quantity and the date (Q-21, team decision). Use = consumption in the hotel. It lowers the balance (Q-34, team decision).
  3. The system saves the record and updates the stock.
- Alternative flows:
  - 3a. Quantity is more than the stock. Behavior is not in the statement and not answered by Q-21.

## UC-17 Access kitchen and restaurant
- Actors: restaurant staff.
- RF links: RF-25, RF-01.
- Phase: planned by the prioritizer; phase set in the backlog order (Q-25).
- Main flow:
  1. The restaurant employee logs in with user name and password (UC-01; Q-23, team decision).
  2. The employee enters the kitchen or the restaurant.
- Alternative flows:
  - 2a. The employee tries any other place. The system refuses access.

## UC-18 Block lost master card
- Actors: manager.
- RF links: RF-30, RF-23.
- Phase: final delivery (Q-06).
- Main flow:
  1. The manager opens the master card function.
  2. The manager blocks the lost master card. The block is immediate: the blocked card opens nothing.
  3. The system saves the block in the log.
- Alternative flows:
  - Block effect and new card number: team decision, not confirmed by the teacher (Q-26).
  - The new master card: UC-46. It is issued only after the old master card is blocked (Q-31, team decision).

## UC-19 Create stock item
- Actors: manager.
- RF links: RF-28, RF-29.
- Phase: final delivery (Q-06).
- Main flow:
  1. The manager opens the stock catalogue.
  2. The manager creates a stock item with its name and unit. Edit: UC-48.
  3. The system saves the item in the catalogue.
- Alternative flows:
  - 2a. Data is not valid. The system shows the error and does not save.
  - XML import (UC-15) accepts all items from a valid file and creates missing catalogue items (Q-10, Q-38; PR #20 decision, 2026-10-21). Manual catalogue management remains a manager action.
  - Stock use and exit (UC-16) use catalogue items (Q-38, team decision).
  - Removal of a catalogue item is not in the team decision (Q-38). If it is added, Q-36 applies.

## UC-20 Show the functions of the role
- Actors: via UC-01 (receptionist, manager, restaurant staff).
- RF links: RF-02.
- Phase: 1st delivery (Q-06, login roles).
- Included in: UC-01.
- Main flow:
  1. The login succeeds.
  2. The system opens the main window.
  3. The system shows only the functions of the role of the user.

## UC-21 Log out
- Actors: receptionist, manager, restaurant staff.
- RF links: RF-01.
- Phase: 1st delivery (Q-06).
- Main flow:
  1. The user selects log out in the main window.
  2. The system closes the session.
  3. The system shows the login screen.

## UC-22 Edit staff account
- Actors: manager.
- RF links: RF-26.
- Phase: planned by the prioritizer; phase set in the backlog order (Q-25).
- Main flow:
  1. The manager selects a staff account.
  2. The manager changes the account data.
  3. The system saves the changes.
- Alternative flows:
  - 3a. Data is not valid, or the new user name exists. The system shows the error and does not save.

## UC-23 Remove staff account
- Actors: manager.
- RF links: RF-26.
- Phase: planned by the prioritizer; phase set in the backlog order (Q-25).
- Main flow:
  1. The manager selects a staff account.
  2. The manager removes it.
  3. The system removes the account.
- Alternative flows:
  - 3a. The account has linked records. The system refuses with a clear error (Q-36, team decision).

## UC-24 List users
- Actors: manager.
- RF links: RNF-01.
- Phase: planned by the prioritizer; phase set in the backlog order (Q-25).
- Main flow:
  1. The manager opens the users list.
  2. The system shows all saved users with their role.
- Alternative flows:
  - Only the manager consults users, at any time (Q-39, team decision).

## UC-25 Consult floors and zones
- Actors: manager.
- RF links: RF-03.
- Phase: 1st delivery for the floor model (RF-03).
- Main flow:
  1. The manager opens the hotel structure.
  2. The system shows floors -1, 0, 1, 2 and 3 and the zones of each floor.

## UC-26 Create zone
- Actors: manager.
- RF links: RF-03, RF-18.
- Phase: final delivery (zone use by the card, Q-06).
- Main flow:
  1. The manager selects a floor.
  2. The manager enters the zone name and sets the two card flags: guest card access, and reservation needed (Q-32, Q-45, team decision).
  3. The system saves the zone.
- Alternative flows:
  - 3a. Data is not valid. The system shows the error and does not save.

## UC-27 Edit zone
- Actors: manager.
- RF links: RF-03, RF-18.
- Phase: final delivery (Q-06).
- Main flow:
  1. The manager selects a zone.
  2. The manager changes the name or the card flags (Q-45).
  3. The system saves the changes.
- Alternative flows:
  - 3a. Data is not valid. The system shows the error and does not save.

## UC-28 Remove zone
- Actors: manager.
- RF links: RF-03.
- Phase: final delivery (Q-06).
- Main flow:
  1. The manager selects a zone.
  2. The manager removes it.
  3. The system removes the zone.
- Alternative flows:
  - 3a. The zone has rooms, reservations or card passes. The system refuses with a clear error (Q-36, team decision).

## UC-29 Register rooms
- Actors: manager.
- RF links: RF-04.
- Phase: 1st delivery (rooms, Q-06).
- Main flow:
  1. The manager enters the room number, floor and type.
  2. The system saves the room.
- Alternative flows:
  - 2a. The room number exists, or the floor is not 2 or 3. The system refuses the room.
  - Room count: 20 rooms, 10 per floor; 7 single, 10 double, 3 family for the whole hotel (Q-01, team decision).

## UC-30 List rooms
- Actors: receptionist.
- RF links: RF-04, RF-06.
- Phase: 1st delivery (rooms, Q-06).
- Main flow:
  1. The receptionist opens the room list.
  2. The system shows each room with its number, floor, type and price per night.

## UC-31 Set base price per room type
- Actors: manager.
- RF links: RF-06.
- Phase: 1st delivery (rooms, Q-06).
- Main flow:
  1. The manager selects a room type.
  2. The manager enters the price per night (Q-11, team decision).
  3. The system saves the price.
- Alternative flows:
  - 3a. The price is not valid. The system shows the error and does not save.

## UC-32 Set extra prices
- Actors: manager.
- RF links: RF-07, RF-09.
- Phase: planned by the prioritizer; phase set in the backlog order (Q-25).
- Main flow:
  1. The manager selects an extra or a paid service.
  2. The manager enters its fixed price per night (Q-11, team decision).
  3. The system saves the price.
- Alternative flows:
  - 3a. The price is not valid. The system shows the error and does not save.

## UC-33 Search client
- Actors: via UC-03 (receptionist).
- RF links: RF-10.
- Phase: 1st delivery (Q-27).
- Included in: UC-03.
- Main flow:
  1. The receptionist enters search data of the client.
  2. The system shows the matching registered clients.
  3. The receptionist selects the client.
- Alternative flows:
  - 2a. No client matches. The receptionist registers the client (UC-02).

## UC-34 Query free rooms
- Actors: via UC-03 (receptionist).
- RF links: RF-05.
- Phase: 1st delivery (reservations, Q-06).
- Included in: UC-03.
- Main flow:
  1. The receptionist selects the room type and the nights.
  2. The system shows the rooms of that type with no reservation in those nights.
- Alternative flows:
  - 2a. No free room. The system shows an empty list.

## UC-35 Calculate reservation price
- Actors: via UC-03 (receptionist).
- RF links: RF-06, RF-07, RF-09.
- Phase: 1st delivery for the base price (Q-06). Extras: planned by the prioritizer (Q-25).
- Included in: UC-03.
- Main flow:
  1. The system takes the base price per night of the room type (UC-31).
  2. The system adds the fixed price per night of each extra and paid service (UC-32; Q-11, team decision).
  3. The system multiplies by the nights and shows the total.

## UC-36 Add room extras to reservation
- Actors: receptionist.
- RF links: RF-07.
- Phase: planned by the prioritizer; phase set in the backlog order (Q-25).
- Extends: UC-03.
- Main flow:
  1. The receptionist selects a reservation, or is in UC-03.
  2. The receptionist adds or removes a special view, drinks or laundry.
  3. The system saves the change and updates the price (UC-35).
- Alternative flows:
  - 2a. The reservation already has a view. The system refuses a second view: sea or mountain, not both (Q-37, team decision).
  - 2b. Check-in is done. The system refuses the change (Q-40, team decision).

## UC-37 Add paid services to reservation
- Actors: receptionist.
- RF links: RF-09.
- Phase: planned by the prioritizer; phase set in the backlog order (Q-25).
- Extends: UC-03.
- Main flow:
  1. The receptionist selects a reservation, or is in UC-03.
  2. The receptionist adds or removes food, cultural events, private pools, spa or conference rooms.
  3. The system saves the change and updates the price (UC-35).
- Alternative flows:
  - 2a. Check-in is done. The system refuses the change (Q-40, team decision).
  - These services give no card access (Q-20, team decision).

## UC-38 Include restaurant and pool in reservation
- Actors: receptionist.
- RF links: RF-11, RF-18.
- Phase: final delivery (needs zones, Q-42, team decision).
- Extends: UC-03.
- Main flow:
  1. The receptionist selects a reservation, or is in UC-03.
  2. The receptionist adds or removes the restaurant or the pool.
  3. The system saves the zone in the reservation. The guest card then opens it (UC-42).
- Alternative flows:
  - 2a. The zone does not have both card flags. The system does not offer it (Q-45, team decision).
  - 2b. Check-in is done. The system refuses the change (Q-40, team decision).

## UC-39 Issue guest card
- Actors: via UC-06 (receptionist).
- RF links: RF-13, RF-16, RF-17, RF-18.
- Phase: final delivery (Q-06).
- Included in: UC-06.
- Main flow:
  1. The system creates a magnetic card with a new number.
  2. The system links the card to the room and to the zones in the reservation (restaurant, pool).
- Alternative flows:
  - One card per reservation (Q-09, team decision).

## UC-40 Deactivate guest card
- Actors: via UC-07 (receptionist).
- RF links: RF-14, RF-16.
- Phase: final delivery (Q-06).
- Included in: UC-07.
- Main flow:
  1. The system marks the card of the reservation as not active.
  2. From now on the card opens nothing. The log is kept (Q-30, team decision).

## UC-41 Consult suggestions and complaints
- Actors: manager.
- RF links: RF-27.
- Phase: planned by the prioritizer; phase set in the backlog order (Q-25).
- Main flow:
  1. The manager opens the suggestions and complaints.
  2. The system shows each suggestion and complaint that reception recorded (UC-14).
- Alternative flows:
  - 2a. There are no records. The system shows an empty list.

## UC-42 Open restaurant or pool with guest card
- Actors: client.
- RF links: RF-18, RF-19.
- Phase: final delivery (Q-06).
- Main flow:
  1. The client puts the card at the restaurant or the pool.
  2. The system checks that the card is active and that the reservation includes the zone.
  3. The system opens the zone.
  4. The system records the pass (UC-44, included).
- Alternative flows:
  - 2a. The zone is not in the reservation. The system does not open it.
  - 2b. The place is the bar, a conference room, the garage, the storeroom, a private pool or the spa. The card does not open it (Q-20, team decision).
  - 2c. The zone is marked as needing a reservation to open with the card (Q-32, team decision). The card opens it only if the reservation includes it.

## UC-43 Open any room or zone with master card
- Actors: manager.
- RF links: RF-23, RF-19.
- Phase: final delivery (Q-06).
- Main flow:
  1. The manager puts the master card at a room or a zone.
  2. The system checks that the master card is not blocked.
  3. The system opens the place.
  4. The system records the pass (UC-44, included). Every master card pass is logged (Q-19, team decision).
- Alternative flows:
  - 2a. The master card is blocked. The system opens nothing (Q-26, team decision).

## UC-44 Record card pass
- Actors: via UC-09, UC-42, UC-43.
- RF links: RF-19.
- Phase: final delivery (Q-06).
- Included in: UC-09, UC-42, UC-43.
- Main flow:
  1. The system saves a record with the card number, the place and the entry time.
- Alternative flows:
  - Denied attempts are not recorded. This is open (Q-49).

## UC-45 Access services of floors 0 and 1
- Actors: receptionist.
- RF links: RF-24.
- Phase: planned by the prioritizer; phase set in the backlog order (Q-25).
- Main flow:
  1. The logged-in receptionist opens a service of floor 0 or floor 1.
  2. The system allows the access.
- Alternative flows:
  - 1a. The service is on another floor. The system refuses the access with a clear message.
  - 1b. The receptionist tries a guest room without a manager master card. The system refuses the entry (Q-08, team decision).
  - The limit applies to reception only (Q-08, team decision).

## UC-46 Issue new master card
- Actors: manager.
- RF links: RF-30, RF-23.
- Phase: final delivery (Q-06).
- Precondition: the old master card of the manager is blocked (UC-18; Q-31, team decision), or the manager has no master card (Q-44).
- Main flow:
  1. The manager opens the master card function.
  2. The manager issues a new master card.
  3. The system creates the card with a new number (Q-26, team decision).
  4. The system keeps the old card number in the log.
- Alternative flows:
  - 2a. The manager has an active master card. The system refuses a second card (Q-19, Q-31, team decision).

## UC-47 Show import report
- Actors: via UC-15 (receptionist, manager).
- RF links: RF-28.
- Phase: final delivery (Q-06).
- Included in: UC-15.
- Main flow:
  1. The import ends.
  2. The system shows the import report: the items, the quantities and the catalogue items it created (Q-10; PR #20 decision, 2026-10-21).

## UC-48 Edit stock item
- Actors: manager.
- RF links: RF-28, RF-29.
- Phase: final delivery (Q-06).
- Main flow:
  1. The manager selects a catalogue item.
  2. The manager changes its name or unit.
  3. The system saves the item.
- Alternative flows:
  - 3a. Data is not valid. The system shows the error and does not save.
  - Removal of a catalogue item is not in the team decision (Q-38).

## UC-49 Record stock exit
- Actors: receptionist, manager.
- RF links: RF-29.
- Phase: final delivery (Q-06).
- Main flow:
  1. Staff or the manager selects a stock item.
  2. The user records an exit with the quantity and the date (Q-21, team decision). Exit = removal (discard, return, loss). It lowers the balance (Q-34, team decision).
  3. The system saves the record and updates the stock.
- Alternative flows:
  - 3a. Quantity is more than the stock. Behavior is not in the statement and not answered by Q-21.

## UC-50 Record stock entry
- Actors: receptionist, manager.
- RF links: RF-28, RF-29.
- Phase: final delivery (Q-06).
- Included in: UC-15 (one entry for each item of the supplier XML file).
- Main flow:
  1. Staff or the manager selects a stock item.
  2. The user records an entry with the quantity and the date (Q-21, team decision).
  3. The system saves the record and raises the balance.
- Alternative flows:
  - 2a. The quantity is not valid. The system shows the error and does not save.
  - Supplier deliveries use UC-15. UC-15 reads the XML file (UC-53) and records each item with this use case. Then the item and the quantity come from the file, not from the user.
  - The sprint 2 retrospective (Jira H4Y-7, 2026-10-23) asks to link UC-50 with the supplier XML stock file. The link goes through UC-15, so UC-50 stays a manual entry, as in the approved story "Registar entrada de stock" (Q-53, team decision, 2026-10-30).

## UC-51 Check reservation has check-in
- Actors: via UC-05 (manager).
- RF links: RF-22.
- Phase: 1st delivery (reservations, Q-06).
- Included in: UC-05.
- Main flow:
  1. The system checks if the reservation has a check-in.
  2. Without a check-in, the delete continues.
- Alternative flows:
  - 1a. The reservation has a check-in. The system refuses the delete (RF-22).

## UC-52 Check client has reservation
- Actors: via UC-06 (receptionist).
- RF links: RF-13.
- Phase: 1st delivery (Q-06).
- Included in: UC-06.
- Main flow:
  1. The system checks if the client has a reservation.
  2. With a reservation, the check-in continues.
- Alternative flows:
  - 1a. The client has no reservation. The system refuses the check-in.

## UC-53 Read supplier XML stock file
- Actors: via UC-15 (receptionist, manager).
- RF links: RF-28.
- Phase: final delivery (Q-06).
- Included in: UC-15.
- Main flow:
  1. The system opens the supplier XML stock file.
  2. The system reads each item and its quantity.
- Alternative flows:
  - 1a. The file is not valid XML. The system refuses it and changes no data (Q-10).

Created by: BorjaTiago. Updated by: antonioprmartinho. AI use: requirements-analyst, Claude Code (claude-sonnet-5-5), 2026-10-15; update of answers Q-01 to Q-23 by requirements-analyst, Claude Code (claude-haiku-5-5), 2026-10-15; update of answers Q-03 to Q-26 by requirements-analyst, Claude Code (claude-haiku-5-5), 2026-10-16; edit of UC-03, UC-18 by requirements-analyst, Claude Code (claude-haiku-5-5), 2026-10-16; notes of UC-02, UC-07, UC-08, UC-09, UC-16, UC-18 for Q-29 to Q-34 by requirements-analyst, Claude Code (claude-haiku-5-5), 2026-10-16; notes of UC-03, UC-04, UC-13, UC-15 and new UC-19 for Q-37 to Q-40 by requirements-analyst, Claude Code (claude-haiku-5-5), 2026-10-16; edit of UC-16 actor by OpenAI Codex, GPT-6, 2026-10-16; PR-20 import decision review by sequence-diagram-editor, OpenAI Codex (GPT-6), 2026-10-22; H4Y-7 review (renamed UC-08, UC-09, UC-13, UC-14, UC-16 to UC-19, include and extend steps, new UC-20 to UC-50) by use-case-diagram-editor, Claude Code (claude-opus-5-5), 2026-10-23; H4Y-7 retrospective fixes (UC-51, UC-52, UC-53, UC-50 XML-only entry) by use-case-diagram-editor, Claude Code (claude-opus-5-5), 2026-10-30; UC-50 manual entry back, UC-15 includes UC-50 and UC-53 after PR-36 review by use-case-diagram-editor, Claude Code (claude-opus-5-5), 2026-10-30; UC-15 XML preview with confirm and cancel by use-case-diagram-editor, Claude Code (claude-opus-5-5), 2026-10-30. Reviewed by: pending.
