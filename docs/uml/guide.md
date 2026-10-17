# UML - Class Guide

This file explains each class in the class diagram. Use it to defend the design. Diagram: [class-diagram.md](class-diagram.md). Classes: [classes.md](classes.md). Requirements: [traceability.md](traceability.md).

## Main ideas (read first)

1. One class per domain concept. `Room`, `Reservation` and `MagneticCard` each match one real thing in the hotel.
2. The business layer never uses JavaFX. Controllers call `HotelFacade`. Services and domain classes have no GUI code (RNF-07).
3. Prices are derived, not stored. `Reservation.calculateTotal()` builds the total from `RoomType.basePrice` and the extras.
4. `ReservationExtra` keeps the price at that time (`unitPrice`). A later price change does not change old bills.
5. A card belongs to a reservation OR a staff user, never both. A card entry log has a room OR a location, never both.
6. Rules live in services. Data lives in domain classes.

## Layers

Interface (JavaFX controllers) calls Business (`HotelFacade`, then services). Services use Domain classes, Enums and the repository interfaces in Data. Calls go one way only.

## Interface

Each controller handles one screen group. Each controller calls only `HotelFacade`. JavaFX stays here (RNF-06, RNF-07).

### LoginController
- **What:** Login screen. Calls `login`.
- **Why:** RF-01. Staff log in with user name and password.
- **Why this way:** The screen only reads input and shows the result.
- **Other option:** Check the password in the controller. Not done: the GUI would hold security code.

### RoomController
- **What:** Room search, room save, room items and prices.
- **Why:** RF-04 to RF-08.
- **Why this way:** One screen group for rooms and room prices.
- **Other option:** One controller per action. Not done: more files for little gain.

### ClientController
- **What:** Client registration and search.
- **Why:** RF-10.
- **Why this way:** A client has its own use case and screen.
- **Other option:** Put it in `ReservationController`. Not done: client work is not reservation work.

### ReservationController
- **What:** Create, cancel and delete a reservation. Add and remove an extra. Add and remove a zone (pool, restaurant).
- **Why:** RF-11, RF-12, RF-15, RF-18, RF-22.
- **Why this way:** Reception works on reservations here.
- **Other option:** Put the delete button in the manager screen only. Not done: the role check is in the service, not the screen.

### LocationController
- **What:** Save and remove zones (pool, restaurant, bar).
- **Why:** RF-03, RF-18.
- **Why this way:** Zones are not rooms. They need their own screen.
- **Other option:** Put zones in `RoomController`. Not done: mixes two topics.

### CheckInOutController
- **What:** Check-in and check-out buttons.
- **Why:** RF-13, RF-14.
- **Why this way:** Check-in and check-out are separate steps of the stay.
- **Other option:** Put them in `ReservationController`. Not done: a separate screen is easier to test.

### StaffController
- **What:** Create, edit, remove staff. Remove is refused if the account has records. List users.
- **Why:** RF-26, RNF-01.
- **Why this way:** Staff admin is a manager task.
- **Other option:** Put it in `LoginController`. Not done: login and admin are different tasks.

### CardController
- **What:** Issue a card. Issue and block the master card. Show card places and room passes.
- **Why:** RF-16, RF-20, RF-21, RF-23, RF-30.
- **Why this way:** One screen group for cards and card reports.
- **Other option:** Separate report screens. Not done: few reports, one controller is enough.

### StockController
- **What:** Import the XML file. Save an item. Register a movement.
- **Why:** RF-28, RF-29.
- **Why this way:** One screen group for stock.
- **Other option:** Separate import screen. Not done: same data and same user.

### FeedbackController
- **What:** Save a feedback. List feedback.
- **Why:** RF-27.
- **Why this way:** Small screen group. Manager reads the list.
- **Other option:** Put it in `ClientController`. Not done: feedback is not client data.

## Facade

### HotelFacade
- **What:** One entry point for the business layer. Each method is one use case.
- **Why:** Controllers need one place to call. The facade hides the services.
- **Why this way:** Controllers do not know which service does the work. Keeps the GUI simple.
- **Other option:** Controllers call services directly. Not done: GUI would know every service and their rules.
- **Note:** The business layer does not use JavaFX (RNF-07).

## Services

Services hold the rules. Each service works on the domain classes.

### AuthService
- **What:** Find a user by name. Check the password. Hash the password.
- **Why:** RF-01, RNF-02. Passwords are never stored in plain text.
- **Why this way:** One place for login and hashing.
- **Rules:** `login` trims the username. The lookup ignores case (contract of `UserRepository.findByUsername`). An unknown user and a wrong password throw the same `LoginFailedException`.
- **Other option:** Hash in `User`. Not done: `User` holds data only. The password check is in `AuthService.isValidPassword`, because the domain uses no external library (BCrypt).

### ClientService
- **What:** Register a client. Find a client by document.
- **Why:** RF-10. The document must be unique (`customers.document`).
- **Why this way:** One place for the duplicate check.
- **Other option:** Let the database fail. Not done: the user gets no clear message.

### LocationService
- **What:** Create, edit and remove zones.
- **Why:** RF-03, RF-18.
- **Why this way:** Zone rules (card access, reservation needed) stay in one place.
- **Rule:** Remove is refused when the zone has links (reservations, card passes). The system shows a clear error (Q-36).
- **Other option:** Put zones in `RoomService`. Not done: zones are not rooms.

### RoomService
- **What:** Check if a room is free. Search rooms. Save a room. Set prices. Configure room items.
- **Why:** RF-04 to RF-09.
- **Why this way:** Room and price rules in one place.
- **Other option:** Put rules in `Room`. Not done: `Room` would need reservation data.

### ReservationService
- **What:** Create, cancel, delete a reservation. Add and remove an extra. Add and remove a zone. Check-in and check-out.
- **Why:** RF-05, RF-11 to RF-15, RF-18, RF-22.
- **Why this way:** Main transaction logic. `create` checks the overlap with `Reservation.overlaps()`. Delete only works for a manager and before check-in.
- **Rules:** `addExtra` refuses a second view (sea or mountain, Q-37). `removeExtra` works only before check-in (Q-40). `addLocation` and `removeLocation` accept only zones with `guestCardAccess = true` and `requiresReservation = true`, and only before check-in. `checkOut` sets `MagneticCard.active = false` (Q-30).
- **Other option:** Put the logic only in `Reservation`. Not done: one reservation cannot see the others.

### StaffService
- **What:** Create, edit, remove staff. Remove is refused if the account has records. List users. Create the first manager (`createFirstManager`), only when no manager exists.
- **Why:** RF-26, RNF-01.
- **Why this way:** Staff admin rules in one place.
- **Rules:** `createFirstManager` trims the username and uses it as the manager name. It returns `false` when a manager already exists. It throws `UsernameTakenException` when the username is taken.
- **Other option:** Put them in `AuthService`. Not done: login and admin are different.

### CardAccessService
- **What:** Issue a card. Issue and block the master card. Check if a card opens a location (`canOpen`) or a room (`canOpenRoom`). Log an entry. Give the reports.
- **Why:** RF-16 to RF-25, RF-30.
- **Why this way:** The access check needs the card, the zone and the reservation. Only a service has all three.
- **Rules:** The access rules are in the table in the MagneticCard section. The old master card is blocked first (sets `blockedAt`). Then the new one is issued (Q-31, Q-26).
- **Other option:** Put `canOpen` in `MagneticCard`. Not done: `MagneticCard` would need reservation and location data.

### StockService
- **What:** Import the XML file. Create and edit items. Register and apply a movement. Each movement takes an exit reason (`ExitReason`) and the user.
- **Why:** RF-28, RF-29.
- **Why this way:** One import is one transaction. Stock changes follow the movement rules.
- **Other option:** Put the import in `StockImport`. Not done: the import has many items and rules.

### FeedbackService
- **What:** Save and list feedback.
- **Why:** RF-27.
- **Why this way:** Small service. Keeps the pattern of the other services.
- **Other option:** Save in `Feedback` only. Not done: no place for the list query.

### LoginFailedException
- **What:** Signals a failed login. One fixed message for an unknown user and a wrong password.
- **Why:** RF-01. The message does not tell which part was wrong.

### UsernameTakenException
- **What:** Signals that a login name is already in use.
- **Why:** RF-26. The caller gets a clear error before the save.

## Data

Data holds the storage interfaces. Services use them.

### UserRepository
- **What:** Interface for user storage. Finds a user by login name, checks if a login name or a role exists, saves a user.
- **Why:** RNF-08. Services depend on this interface, not on SQL. The JDBC version comes in H4Y-32 PR 2.

## Users

`User` is the base for staff. `Client` is a guest. Guests do not log in (RF-01).

### User (abstract)
- **What:** Staff account: name, username, password hash, role.
- **Why:** RF-01, RF-02, RNF-02. Login data is the same for all staff.
- **Why this way:** One base class for login fields.
- **Other option:** One table or class per staff type. Not done: the DB has one `users` table with `role_id`.

### Receptionist
- **What:** Reception staff. Access to floors 0 and 1.
- **Why:** RF-02, RF-24.
- **Why this way:** The class makes the type visible in the code.
- **Other option:** Use only the `Role` value and `if` checks. Not done: the rules are spread over many places.

### Manager
- **What:** Manager. Has the master card. Manages staff. Deletes reservations.
- **Why:** RF-02, RF-23, RF-26, RF-30.
- **Why this way:** Same reason as `Receptionist`.
- **Other option:** Role checks only. Not done: as above.

### RestaurantStaff
- **What:** External restaurant staff. Access to kitchen and restaurant only.
- **Why:** RF-02, RF-25.
- **Why this way:** Same reason as `Receptionist`.
- **Other option:** Role checks only. Not done: as above.

### Client
- **What:** Hotel guest: name, document, birthday.
- **Why:** RF-10, RF-02. A client makes reservations and may leave feedback.
- **Why this way:** Clients have no password. A separate class is clearer than a `User` with no login.
- **Other option:** `Client` as a `User` subclass. Not done: clients do not log in.

## Hotel

### Floor
- **What:** A floor number (-1 to 3) and description.
- **Why:** RF-03. Floors decide staff access (floors 0 and 1).
- **Why this way:** Rooms and zones both link to a floor.
- **Other option:** Store the number on each `Location`. Not done: the floor is shared by many places.

### Location
- **What:** A zone with a card reader (pool, restaurant, bar). Flags: `guestCardAccess`, `requiresReservation`.
- **Why:** RF-03, RF-18.
- **Why this way:** The two flags say if a client card opens the zone.
- **Other option:** Fixed list of zone names in code. Not done: the manager adds and removes zones.

### Room
- **What:** A room with a number. Links to a floor and a type.
- **Why:** RF-04, RF-05. Rooms are reserved.
- **Why this way:** A room is a real object. A reservation points to it.
- **Other option:** Room type as text. Not done: the type has a capacity and a price.

### RoomType
- **What:** A type (single, double, family) with capacity and `basePrice`.
- **Why:** RF-04, RF-06.
- **Why this way:** The manager sets the price once for each type.
- **Other option:** Price on each `Room`. Not done: many rooms share one price.

### RoomItem
- **What:** How many units of one stock item a room has.
- **Why:** RF-08.
- **Why this way:** Each room can have its own items.
- **Other option:** Items per room type. Not done: the manager wants per room control.

## Reservations and extras

### Reservation
- **What:** A stay of one client in one room. Dates, status, real check-in and check-out times.
- **Why:** RF-05, RF-11 to RF-14, RF-22.
- **Why this way:** One reservation is one stay. Methods: `checkIn`, `checkOut`, `cancel`, `overlaps`, `calculateTotal`. A reservation has zero or one `MagneticCard` (`0..1`).
- **Other option:** A separate `Stay` class. Not done: the DB has no stay table. The reservation holds the times.

### ReservationExtra
- **What:** An extra on a reservation: quantity, `unitPrice`, `usedAt`.
- **Why:** RF-07, RF-09, RF-15.
- **Why this way:** The price is copied at that time (`unitPrice`).
- **Other option:** Read the price from `ExtraService` each time. Not done: old bills would change.

### ExtraService
- **What:** Catalogue item (spa, laundry, sea view). Has a `price`.
- **Why:** RF-07, RF-09.
- **Why this way:** The manager sets one price for each service.
- **Other option:** Text on the reservation. Not done: no price, no catalogue.

## Cards and access

### MagneticCard
- **What:** A card with a number, a type (client or master), an active flag and `blockedAt` (time of a block).
- **Why:** RF-16, RF-17, RF-18, RF-23, RF-24, RF-25, RF-30.
- **Why this way:** One class for all cards. `blockedAt` keeps the time a master card was blocked (RF-30).
- **Owner:** A client card belongs to a `Reservation` (`0..1`). A master card or a staff card belongs to a `User` (`0..1`). A card has a reservation OR a user, never both (XOR).
- **Other option:** Separate classes for client and master cards. Not done: same fields, one class is simpler.

**Access rules by card:**

| Card (owner) | Opens | Does not open |
|--------------|-------|---------------|
| Master (Manager) | All rooms and all zones | - |
| Client (Reservation) | Its own room. Zones with `guestCardAccess = true`. If `requiresReservation = true`, only zones in its reservation. | Other rooms. Zones with `guestCardAccess = false`. |
| Receptionist (User) | Zones on floors 0 and 1 | Any guest room. Zones on other floors. |
| Restaurant staff (User) | Kitchen and restaurant | Any guest room. Other zones. |
| Blocked or inactive (any) | Nothing | Everything |

A staff card never opens a guest room. Only the master card does.

### CardAccessRecord
- **What:** One entry in the card log. Time and a room OR a location.
- **Why:** RF-19 to RF-21.
- **Why this way:** A log row is never changed. Reports read these rows.
- **Other option:** Text file log. Not done: reports need queries.

## Stock

### StockItem
- **What:** A stock item with a unit and `stockQuantity`.
- **Why:** RF-28, RF-29.
- **Why this way:** The balance is kept on the item. It is not summed each time.
- **Other option:** Sum all movements each time. Not done: slower, and the DB keeps `stock_quantity` with a check `>= 0`.

### StockMovement
- **What:** One change: entry, use or exit. Quantity and time.
- **Why:** RF-29.
- **Why this way:** Keeps the history of each change.
- **Other option:** Change the quantity only. Not done: no history.

### StockImport
- **What:** One XML load: supplier, time and rejected items.
- **Why:** RF-28. The report lists the rejected items.
- **Why this way:** Each movement from the file points to its import.
- **Other option:** No import record. Not done: the manager needs the report.

## Feedback

### Feedback
- **What:** A suggestion or a complaint: type, text, time.
- **Why:** RF-27.
- **Why this way:** The client may be null: anonymous feedback (`customer_id` NULL in the DB). Feedback from a known guest links to a `Client` (`0..1` to `*`).
- **Other option:** Send by email. Not done: the manager reads it in the app.

## Enums

Enums have fixed values. They stop typing errors.

### Role
- **What:** CLIENT, RECEPTIONIST, MANAGER, RESTAURANT, SUPPLIER.
- **Why:** RF-02. The five actor types.
- **Why this way:** Fixed list in code. The DB has a `roles` table with the same five names.
- **Other option:** Only the `roles` table. Not done: code needs a type-safe value.

### ReservationStatus
- **What:** RESERVED, CANCELLED, CHECKED_IN, CHECKED_OUT.
- **Why:** RF-12 to RF-14.
- **Why this way:** A cancel is a state change, not a delete (RF-12).
- **Other option:** Text status. Not done: typing errors.

### CardType
- **What:** CLIENT, MASTER.
- **Why:** RF-16, RF-23.
- **Why this way:** A master card opens all rooms and zones.
- **Other option:** A boolean `isMaster`. Not done: the enum is clearer.

### FeedbackType
- **What:** SUGGESTION, COMPLAINT.
- **Why:** RF-27.
- **Why this way:** Two fixed types.
- **Other option:** Text. Not done: typing errors.

### MovementType
- **What:** ENTRY, USE, EXIT. The DB stores `in`, `use`, `out`.
- **Why:** RF-29.
- **Why this way:** Fixed set of movements.
- **Other option:** Sign of the quantity. Not done: less clear.

### ExitReason
- **What:** DISCARD, RETURN, LOSS. Only for an EXIT.
- **Why:** RF-29.
- **Why this way:** Explains each exit.
- **Other option:** Text. Not done: typing errors.

## Key flows

Java style. Names come from the diagram. Variables are examples.

### 1. Create a reservation and read the total
```java
Client client = hotelFacade.findClient("12345678");
Room room = hotelFacade.searchRooms(start, end, doubleType).get(0);
Reservation res = hotelFacade.createReservation(client, room, start, end); // ReservationService checks overlap
hotelFacade.addExtra(res, seaView, 1);                                     // ReservationExtra copies the price
BigDecimal total = res.calculateTotal();                                   // derived, not stored
```

### 2. Check-in issues a card
```java
hotelFacade.checkIn(res);                       // status CHECKED_IN, checkedInAt set
MagneticCard card = hotelFacade.issueCard(res); // one card per reservation
```
Order: check-in first, then the card (RF-13, RF-16).

### 3. XML stock import
```java
StockImport report = hotelFacade.importStock("delivery-0412.xml", user);
List<String> rejected = report.getRejectedItems(); // unknown items
// Known items: StockMovement with type ENTRY, linked to the import.
```

Created by: BorjaTiago. Updated by: BorjaTiago. AI use: Claude Code, claude-haiku-5-5, 2026-10-16; edited by Claude Code, claude-haiku-5-5, 2026-10-16; doc-updater, Claude Code (claude-haiku-5-5), 2026-10-17. Reviewed by: pending.
