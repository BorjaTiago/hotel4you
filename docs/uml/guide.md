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

### MainController
- **What:** Main window after login. Top: welcome text and Logout. Left: menu with the functions of the user. Centre: the open function. Logout clears `HotelApp.currentUser` and opens the login screen.
- **Why:** RNF-06, RNF-07. Each user finds the functions of the role in one place.
- **Why this way:** The menu asks `HotelFacade.canUse(actor, permission)` which buttons to show. The screen has no role check of its own.
- **Other option:** A `Session` or `PermissionPolicy` class for the menu. Not done: the facade already holds the rules, and the extra class adds no value now.

### HotelApp
- **What:** The JavaFX application. It opens the login screen. It keeps the facade and the logged-in user. Without a logged-in user, the main window does not open (H4Y-24).
- **Why:** RNF-06. JavaFX needs one `Application` class.
- **Why this way:** `Main` sets the facade first. If the start failed, `HotelApp` shows the error in a popup.
- **Other option:** Let each screen read the settings. Not done: the screens would know the database.

### ScreenLoader
- **What:** Opens a screen from an FXML file. It gives the `HotelFacade` to the controller.
- **Why:** RNF-06, RNF-07. All controllers get the facade in one way.
- **Why this way:** One class does the loading. A controller never creates services.
- **Other option:** Each screen loads its own FXML. Not done: the same code many times.

### RoomController
- **What:** Room search, room save, room items and prices.
- **Why:** RF-04 to RF-08.
- **Why this way:** One screen group for rooms and room prices.
- **Registration summary:** H4Y-64 requires counts by floor and type. The floor choices and denominators come from `HotelFacade.roomFloorLimits()`. The screen counts the loaded rooms for display. It does not choose room floors or enforce limits. Type totals have no fixed target until Q-01 is confirmed.
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

### FloorController
- **What:** Show the floors and their zones. Create, save and remove a zone with the form below the zone table (H4Y-28).
- **Why:** RF-03.
- **Why this way:** One screen for floors and zones. There is no `LocationController` for zone CRUD. The controller has no rules.
- **Other option:** A separate zone screen. Not done: the zones belong to the floor the manager is looking at.

### CheckInOutController
- **What:** Check-in and check-out buttons.
- **Why:** RF-13, RF-14.
- **Why this way:** Check-in and check-out are separate steps of the stay.
- **Other option:** Put them in `ReservationController`. Not done: a separate screen is easier to test.

### StaffController
- **What:** Create, edit, remove staff. Remove is refused if the account has records. List users. Now (H4Y-27) the screen creates accounts (name, username, password and type) and shows the list of staff accounts. It shows the service message on a refusal. The list loads when the screen opens and after each new account.
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
- **Permissions:** `canUse(actor, permission)` tells if the user may use a function. The GUI asks it to show or hide a menu button (H4Y-60).

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
- **What:** Create, edit and remove zones. Show floors and zones. Uses `FloorRepository` and `LocationRepository` (H4Y-28). It checks `Permission.MANAGE_FLOORS` and throws `AccessDeniedException` when it does not allow the actor.
- **Why:** RF-03, RF-18.
- **Why this way:** Zone rules (card access, reservation needed) stay in one place.
- **Rule:** Only a manager manages zones. The name is trimmed, required and has at most 50 characters. The name is unique per floor. Remove is refused with `LocationInUseException` when the delete fails because the zone has links (reservations, card passes). The system shows a clear error (Q-36).
- **Other option:** Put zones in `RoomService`. Not done: zones are not rooms.

### RoomService
- **What:** Save and list rooms. Return the room type names for the registration screen. Keep the other room, price and room item operations here.
- **Why:** RF-04 to RF-09. `Permission.MANAGE_ROOMS` allows only a manager to register rooms, list saved rooms and read the room type catalogue for registration. `listRooms(actor)` and `listRoomTypes(actor)` reject other staff and a null actor before reading the repository.
- **Why this way:** The controller collects fields and shows results. `RoomService` defines the statement rules as constants: floors 2 and 3, with 10 rooms each. Its `roomFloorLimits()` method supplies the same rules to the screen. The service checks the permission, floor catalogue, room floor, type, duplicate number and limit. `RoomRepository.save(room, floorLimit)` locks the floor row and checks the limit in the same transaction. This protects the limit when two requests arrive together.
- **Other option:** Put the validation in `RoomController` or only in SQL constraints. Not done: controllers hold no business rules, and a unique number constraint cannot enforce a per-floor count.
- **Example:** A manager saves room 202 on floor 2 as a double room. The service checks that floor 2 and the type exist, that room 202 is free, and that floor 2 has fewer than 10 rooms. The repository saves the room with its floor and type foreign keys. The course statement sets the two room floors and the limit of 10 rooms per floor.
- **Q-01:** The test can register the proposed 7 individual, 10 double and 3 family rooms. The service does not enforce those type totals because the teacher has not confirmed them.
- **Integration:** H4Y-28 provides the `floors` table on `main`. The `room_types` table depends on H4Y-26.
- **Type names:** The chosen stored names are `individual`, `duplo`, `familiar` (user decision, 2026-10-30), as in the H4Y-26 draft. The screen labels them Individual, Duplo and Familiar. Tests and database documentation use the same names. The screen does not translate `single`, `double` or `family` as aliases. H4Y-26 must still provide the room type script; no replacement script is added here.

### ReservationService
- **What:** Create, cancel, delete a reservation. Add and remove an extra. Add and remove a zone. Check-in and check-out.
- **Why:** RF-05, RF-11 to RF-15, RF-18, RF-22.
- **Why this way:** Main transaction logic. `create` checks the overlap with `Reservation.overlaps()`. Delete only works for a manager and before check-in.
- **Rules:** `addExtra` refuses a second view (sea or mountain, Q-37). `removeExtra` works only before check-in (Q-40). `addLocation` and `removeLocation` accept only zones with `guestCardAccess = true` and `requiresReservation = true`, and only before check-in. `checkOut` sets `MagneticCard.active = false` (Q-30).
- **Other option:** Put the logic only in `Reservation`. Not done: one reservation cannot see the others.

### StaffService
- **What:** Create, edit, remove staff. Remove is refused if the account has records. List users.
- **Why:** RF-26, RNF-01.
- **Why this way:** Staff admin rules in one place.
- **Rules:** Only a manager can create accounts. `Permission.MANAGE_STAFF` holds this rule. `createStaff` checks, in this order: the actor is a manager (else `AccessDeniedException`), no field is blank, the name has at most 100 and the username at most 50 characters (the `users` columns), the role is receptionist, restaurant staff or manager, and the username is free. It trims the name and the username and saves the bcrypt hash (H4Y-27). `listUsers` returns all staff accounts from `UserRepository.findAll`. Rows of roles without an account (client, supplier) are skipped.
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
- **Why this way:** Import every item from a valid XML file. Create missing catalogue items as part of the import (Q-10, Q-38; PR #20 decision, 2026-10-21). One transaction covers those items, the import, ENTRY movements and balances. A failure undoes all of them. No preview or confirmation API is needed.
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

### AccessDeniedException
- **What:** Signals that the logged-in user may not do the action. `StaffService.createStaff` throws it when `Permission.MANAGE_STAFF` does not allow the actor. `LocationService` throws it when `Permission.MANAGE_FLOORS` does not allow the actor (H4Y-28).
- **Why:** RF-26. The service refuses the request even if the screen shows the button by mistake.

## Config

### AppConfig
- **What:** A record with the settings from `application.properties`: database url, user and password.
- **Why:** RNF-12. Passwords stay out of the code and out of Git.
- **Why this way:** `load(file)` reads the file once. `toString()` hides the passwords.
- **Other option:** Environment variables. Not done: the team uses one settings file.

### Main
- **What:** Starts the application. Reads `AppConfig`. Creates `Database`, `JdbcUserRepository`, `AuthService`, `StaffService` and `HotelFacade`. Then it starts `HotelApp`.
- **Why:** RF-01, RNF-12. One place builds the objects.

## Data

Data holds the storage interfaces and the SQL Server version. Services use the interfaces.

### UserRepository
- **What:** Interface for user storage. Finds a user by login name, checks if a login name or a role exists, saves a user.
- **Why:** RNF-08. Services depend on this interface, not on SQL. `JdbcUserRepository` is the SQL Server version.

### Database
- **What:** Opens connections to SQL Server with the settings from `AppConfig`. `connect` opens a connection. `check` opens and closes one to test that the database works. `prepare` (static) fills the `?` values of a SQL text.
- **Why:** RNF-12. One place holds the connection code.
- **Why this way:** The values go in with `prepare`, not in the SQL text. It uses `setInt` for integers, `setLong` for long identifiers and `setString` for text. This stops SQL injection.
- **Other option:** Each repository opens its own connection. Not done: the same code many times.

### JdbcUserRepository
- **What:** Implements `UserRepository` with SQL on the tables `users` and `roles`.
- **Why:** RNF-01, RNF-12. Users are saved in SQL Server.
- **Why this way:** It gives domain objects to the services. Services never see SQL.
- **Other option:** SQL inside `AuthService`. Not done: rules and SQL would mix.

### JdbcRoomRepository
- **What:** Read the catalogues and save rooms in SQL Server.
- **Why:** RF-04 and RNF-12. Room numbers must be unique and a full floor must reject new rooms.
- **Why this way:** `save(room, floorLimit)` opens one transaction. It locks the floor row with `UPDLOCK, HOLDLOCK`, reads the count, inserts with `OUTPUT INSERTED.id` and commits. SQL and runtime errors roll back. A rollback error is kept as a suppressed error. Each statement and result closes with a resource block. Small helper methods avoid nested resource blocks.
- **Other option:** Check the limit only in the service. This cannot protect two application instances that save at the same time.
- **Example:** Two requests see nine rooms on a floor with a limit of ten. Only one holds the floor lock. After it commits, the next request sees ten rooms and rolls back without inserting.

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
- **Why this way:** Rooms and zones both link to a floor. The floors are fixed, so `Floor` is a record.
- **Other option:** Store the number on each `Location`. Not done: the floor is shared by many places.

### LocationRepository and JdbcLocationRepository
- **What:** Find a zone by id or by floor and name. Save, update and delete a zone.
- **Why:** RF-03 (H4Y-28).
- **Why this way:** `delete` returns false when SQL Server refuses it because other rows use the zone (error 547). The service then throws `LocationInUseException`.
- **Other option:** Count the links before the delete. Not done: the database already knows them.

### Location
- **What:** A zone with a card reader (pool, restaurant, bar). Flags: `guestCardAccess`, `requiresReservation`.
- **Why:** RF-03, RF-18.
- **Why this way:** The two flags say if a client card opens the zone. The manager changes zones, so `Location` is a class.
- **Other option:** Fixed list of zone names in code. Not done: the manager adds and removes zones.

### Room
- **What:** A room with a number. Links to a floor and a type.
- **Why:** RF-04, RF-05. Rooms are reserved.
- **Why this way:** A room is a real object. A reservation points to it.
- **Other option:** Room type as text. Not done: the type has a capacity and a price.

### RoomType
- **What:** A type (`individual`, `duplo`, `familiar`) with capacity and `basePrice`. These are the stored names in the database schema.
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
- **What:** One XML load with supplier and time. Its movements identify the imported items and quantities.
- **Why:** RF-28. The report records the delivery and who loaded it through the existing user association.
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

### Permission
- **What:** One value for each function of the application. Each value keeps the roles that may use it (`allowedRoles`). `allows(actor)` returns true only when the actor is logged in and has one of these roles.
- **Why:** RF-02. Each role uses only its own functions (H4Y-23, H4Y-60).
- **Why this way:** Before, each function needed its own method, for example `canCreateStaff`. Each new function (floors, rooms, prices) would add one more small method with the same check. Now the rule is in one list. To add a function, add one value. The GUI asks `HotelFacade.canUse(actor, permission)` and the service uses the same value to refuse the action. So the menu and the service never disagree.
- **Other option:** A `PermissionService` with a map from role to functions. Not done: one more class, and the enum already keeps the roles next to each function.
- **Roles and functions (only the functions that exist in the code):**

| Permission | Function | Manager | Receptionist | Restaurant staff |
|------------|----------|---------|--------------|------------------|
| MANAGE_STAFF | Create and list staff accounts (H4Y-27) | yes | no | no |
| MANAGE_FLOORS | View the floors and create, edit and remove zones (H4Y-28) | yes | no | no |
| MANAGE_ROOMS | Register rooms and read the room list and type catalogue (H4Y-25) | yes | no | no |

Without a logged-in user, no function is allowed. The receptionist and the restaurant staff have no function in the application yet.

Each story that adds a function adds its value to `Permission`, a test, and a row to this table, in the same PR.

Note on H4Y-23 AC-1 ("the receptionist can use the functions of the role"): today the receptionist has no function, so only the manager part of AC-1 has a test. The receptionist part is covered when H4Y-50 (list rooms) adds its permission and its test, with `@Tag("H4Y-23-AC-1")`. H4Y-23 is not done before that test exists (PR #33 review, 2026-10-27).

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
// Every item in a valid XML file enters stock. Create missing catalogue items.
// StockMovement with type ENTRY links each item's quantity to the import.
```

Created by: BorjaTiago. Updated by: pedrocostaisep. AI use: Claude Code, claude-haiku-5-5, 2026-10-16; edited by Claude Code, claude-haiku-5-5, 2026-10-16; doc-updater, Claude Code (claude-haiku-5-5), 2026-10-17; doc-updater, Claude Code (claude-sonnet-5-5), 2026-10-17; doc-updater, Claude Code (claude-haiku-5-5), 2026-10-19; PR-20 import decision review by sequence-diagram-editor, OpenAI Codex (GPT-6), 2026-10-22; merge-conflict resolution by sequence-diagram-editor, OpenAI Codex (GPT-6), 2026-10-22; doc-updater, Claude Code (claude-opus-5-5), 2026-10-23; H4Y-27 staff account update, Claude Code (claude-opus-5-5), 2026-10-23; first-manager removal by doc-updater, Claude Code (claude-haiku-5-5), 2026-10-26; Floor record note by doc-updater, Claude Code (claude-haiku-5-5), 2026-10-26; H4Y-28 zone merge by doc-updater, Claude Code (claude-sonnet-5-5), 2026-10-26; H4Y-60 role permission rule, Claude Code (claude-opus-5-5), 2026-10-27; PR #33 review change, Claude Code (claude-opus-5-5), 2026-10-27; merge with main by Claude Code (claude-opus-5-5), 2026-10-27; H4Y-28 floors permission by doc-updater, Claude Code (claude-haiku-5-5), 2026-10-27. H4Y-25 merge with latest main, OpenAI Codex (GPT-6), 2026-10-28; H4Y-25 review corrections, OpenAI Codex (gpt-6.1-sol), 2026-10-29; statement room constants review, OpenAI Codex (gpt-6.1-sol), 2026-10-29; room read permission and JDBC review, OpenAI Codex (gpt-6.1-sol), 2026-10-30; H4Y-26 room type name alignment, OpenAI Codex (gpt-6.1-sol), 2026-10-30. Reviewed by: pending.
