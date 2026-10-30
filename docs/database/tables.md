# Database - Table Guide

Use this file to explain and defend the schema. Each table has the same parts:

- **What:** one sentence.
- **Why:** the problem it solves.
- **Why this way:** the design choice and the reason.
- **Other option:** what we did not do, and why.
- **Example:** rows from one story.

Columns and constraints: [schema.md](schema.md). Diagram: [erd.md](erd.md).

## Changes

This version applies the schema decisions of PR #3 (branch `backlog-requirements`). The `Q-NN` are in `docs/requirements/questions.md` of that PR.

- **Added:**
  - `floors.description` (H4Y-28): the name of each floor that the manager sees. PR #3 removed it. It is back now.
  - `reservation_locations`: locations of a reservation (Q-09, RF-18).
  - `locations.requires_reservation` (Q-32): 1 = the zone needs a reservation to open with the client card. Used for restaurant and pool.
  - `locations.guest_card_access` (Q-20): 0 = a client card never opens the place (bar, conference rooms, garage, storeroom, private pools, spa). We chose one BIT column, the simplest way to say "never". Default 0.
  - `access_cards.blocked_at`: block record, the card row is never deleted (RF-30, Q-26, Q-31).
  - `items.unit` (Q-38).
  - `stock_movements.exit_reason`: discard, return, loss (Q-34).
  - `stock_imports` and `stock_movements.import_id`. `supplier_name` moves to `stock_imports` (RF-28, Q-10).
- **Changed:**
  - `roles` has 5 rows, the actor types of RF-02: CLIENT, RECEPTIONIST, MANAGER, RESTAURANT, SUPPLIER (upper case, the same as the Java `Role` enum).
  - Example data follows RF-03 (floor 1 has restaurant and pool; floors 2 and 3 have rooms) and RF-04 (20 rooms).
  - The view is a service: "sea view" and "mountain view" are rows of `additional_services`. One view per reservation (Q-37, Q-11).
  - Room service requests (food, drink, laundry) use `reservation_services`. `used_at` is the request time (Q-33).
  - `room_types.base_price` and `additional_services.price` allow NULL.
  - `access_cards.card_number` is plain UNIQUE.
  - `feedback.customer_id` allows NULL.
  - No CASCADE. All FKs are `NO ACTION`. The backend deletes the reservation children in the same transaction (Q-36, RF-22).
  - `locations.name` is unique per floor: `UNIQUE (floor_id, name)`.
  - `access_logs (room_id, entered_at)` is a plain index.
- **Removed:** `rooms.view_type`, `rooms.view_surcharge`, `users.active` (Q-36), `customers.birthday`, `floors.description`, `reservation_services.quantity`, `reservations.price_per_night`.

**The story:** Ana Silva books room 202 for 3 nights, uses the spa, and leaves feedback.

## Main ideas (read first)

1. **One fact in one place.** Capacity is in `room_types`, not in every room. A change is made once.
2. **Copy the price when it is sold.** `reservation_services.unit_price` keeps the price of that day. Old bills never change. The reservation has no price column: the backend computes the total from `base_price` and the services.
3. **The database blocks bad data.** CHECK, UNIQUE and FK stop wrong rows, even if the backend has a bug.
4. **Small types.** `TINYINT` for tables with few rows. `INT` for tables that grow. This saves space and shows the expected size.
5. **Keep history.** We do not delete users or cards. A card is turned off (`active = 0`) or blocked (`blocked_at`).
6. **Complex rules stay in the backend.** Room overlap and stock changes need a transaction. A trigger would hide the logic.

---

## Building and rooms

### floors
**What:** The floors of the hotel (-1 to 3): -1 garage and storeroom; 0 reception, bar, conference rooms; 1 restaurant and pool; 2 and 3 rooms (RF-03).
**Why:** Rooms and locations need a floor. Staff access uses the floor.
**Why this way:** `number` is `SMALLINT` because `TINYINT` cannot store -1. `id` is separate from `number`, so a renumbering does not break FKs. `description` is the name the manager sees (H4Y-28).
**Other option:** Store the floor number inside `rooms`. We did not, because `locations` also needs floors, and the number would repeat.

| id | number | description |
|---|---|---|
| 1 | -1 | Garagem e arrecadação |
| 2 | 0 | Receção, bar e salas de conferência |
| 3 | 1 | Restaurante e piscina |
| 4 | 2 | Quartos |
| 5 | 3 | Quartos |

### room_types
**What:** The stored room type names are `individual`, `duplo`, `familiar` (aligned with H4Y-26, user decision, 2026-10-30). They mean individual, double and family rooms. The hotel has 20 rooms: 7 individual, 10 double, 3 family (RF-04).
**Why:** All rooms of one kind have the same capacity and base price.
**Why this way:** We store capacity and price once. To change a price, we edit one row. `base_price` can be NULL (price not set yet).
**Other option:** Put `capacity` and `base_price` in `rooms`. We did not, because 20 rooms would repeat the same values, and one edit could be forgotten.

| id | name | capacity | base_price |
|---|---|---|---|
| 1 | individual | 1 | 60.00 |
| 2 | duplo | 2 | 90.00 |
| 3 | familiar | 4 | 140.00 |

`database/v5-create-room-types.sql` creates the three rows with `base_price` NULL. The manager sets the prices (H4Y-26). The prices above are examples.

### rooms
**What:** One row for each physical room.
**Why:** A reservation needs a real room. The room has a floor and a type.
**Why this way:** The view is not a room column. A guest chooses "sea view" or "mountain view" for the reservation, so it is a service (see `additional_services`). The price per night comes from the room type.
**Other option:** `view_type` and `view_surcharge` in `rooms`. We did not, because the view is sold with the reservation, and the backend allows one view per reservation.

| id | number | floor_id | room_type_id |
|---|---|---|---|
| 12 | 201 | 4 | 2 |
| 13 | 202 | 4 | 2 |
| 14 | 301 | 5 | 3 |

Room 202 is a double room: 90.00 per night.

### locations
**What:** Places with a card reader that are not rooms (pool, restaurant, bar, garage...).
**Why:** The access log must say where a card entered. Staff may enter only some places.
**Why this way:** A separate table, because a pool is not a room (no type, no price, no reservation).
- `guest_card_access` says if a client card can open the place. `0` = never (bar, conference rooms, garage, storeroom, private pools, spa; Q-20). `1` = can open.
- `requires_reservation = 1` means the zone needs a reservation to open with the client card (restaurant, pool; Q-32). The reservation lists the place in `reservation_locations`. It counts only when `guest_card_access = 1`.
- Rule: `guest_card_access = 0` refuse; `guest_card_access = 1` and `requires_reservation = 0` open; both 1: open only if the reservation lists the place.
- `name` is unique per floor: `UNIQUE (floor_id, name)`.

**Other option:** Put places in `rooms`. We did not, because it would mix two different things and leave many empty columns.

| id | name | floor_id | guest_card_access | requires_reservation |
|---|---|---|---|---|
| 1 | Garagem | 1 | 0 | 0 |
| 2 | Arrecadação | 1 | 0 | 0 |
| 3 | Receção | 2 | 0 | 0 |
| 4 | Bar | 2 | 0 | 0 |
| 5 | Salas de conferência | 2 | 0 | 0 |
| 6 | Restaurante | 3 | 1 | 1 |
| 7 | Piscina | 3 | 1 | 1 |

Floor ids: 1 = floor -1, 2 = floor 0, 3 = floor 1.

### reservation_locations
**What:** The locations that a reservation includes.
**Why:** The backend must know which places a customer card may open (Q-09, RF-18).
**Why this way:** The PK is (`reservation_id`, `location_id`). It is a link table for a many-to-many relation. The backend deletes these rows before it deletes the reservation (no CASCADE).
**Other option:** A list in a text column. We did not, because the database could not check the FKs.

| reservation_id | location_id |
|---|---|
| 1 | 1 |
| 1 | 2 |

---

## Inventory

### items
**What:** The warehouse list (towel, shampoo, sheet).
**Why:** The hotel must know what it has in stock.
**Why this way:** `stock_quantity` is saved in the row, so a read is fast. A CHECK blocks negative stock. `name` is UNIQUE, so one item has one row. `unit` says how we count the item (Q-38).
**Other option:** Calculate the stock from `stock_movements` each time. This is always correct, but slow when there are many movements. We keep both and change both in one transaction.

| id | name | unit | stock_quantity |
|---|---|---|---|
| 1 | towel | unit | 120 |
| 2 | shampoo | bottle | 300 |
| 3 | sheet | unit | 80 |

### room_items
**What:** The items each room must have, and how many.
**Why:** The manager configures what is in each room.
**Why this way:** The PK is (`room_id`, `item_id`). It is a link table for a many-to-many relation: a room has many items, and an item is in many rooms. No row means the room does not have the item.
**Other option:** Set items per room type. It is less work to configure, but a room with a special setup is not possible. We chose per room for flexibility.

| room_id | item_id | quantity |
|---|---|---|
| 13 | 1 | 4 |
| 13 | 2 | 2 |
| 13 | 3 | 2 |

### stock_movements
**What:** The history of every stock change.
**Why:** The manager must see who changed the stock, when, and why.
**Why this way:** `quantity` is always positive. `type` says the effect: `in` adds, `use` and `out` subtract. This avoids sign errors. `exit_reason` (discard, return, loss) only exists for `out` (CHECK, Q-34). `import_id` links a movement to the XML load that created it. It is NULL for a movement entered by hand.
**Other option:** Negative numbers for `use` and `out`. We did not, because a wrong sign gives wrong stock and the type is already clear.

| id | item_id | type | exit_reason | quantity | moved_at | user_id | import_id |
|---|---|---|---|---|---|---|---|
| 1 | 1 | in | NULL | 50 | 2026-10-01 09:00 | 2 | 1 |
| 2 | 1 | use | NULL | 4 | 2026-10-12 10:30 | 3 | NULL |
| 3 | 2 | out | discard | 10 | 2026-10-13 08:00 | 2 | NULL |

### stock_imports
**What:** One row for each XML file that hotel staff or the manager loads.
**Why:** The hotel must know who loaded a delivery, when and from which supplier (RF-28, Q-10).
**Why this way:** The supplier is the same for all lines of one file, so it is stored once here, not in each movement. Every item from a valid XML file enters stock. The import creates missing catalogue items (Q-10, Q-38; PR #20 decision, 2026-10-21). Linked movements give the imported items and quantities. Save new items, the import, movements and balances together. A failed save undoes them all. No rejection list is stored.
**Other option:** `supplier_name` in each movement. We did not, because it repeats and it does not record the load itself.

| id | loaded_at | user_id | supplier_name |
|---|---|---|---|
| 1 | 2026-10-01 09:00 | 2 | TextilPT |

---

## People and security

### customers
**What:** The hotel guests.
**Why:** Every reservation and feedback needs a customer.
**Why this way:** `document` is UNIQUE, so the same person is not created twice. There is no birthday column: it was not needed. `NVARCHAR` accepts foreign names.
**Other option:** Use the document as the PK. We did not, because documents can have typos and change, and an `INT` key is smaller in FKs.

| id | name | document |
|---|---|---|
| 1 | Ana Silva | 12345678 |
| 2 | John Müller | X9876543 |

### roles
**What:** The permission groups: the 5 actor types of RF-02 (CLIENT, RECEPTIONIST, MANAGER, RESTAURANT, SUPPLIER).
**Why:** The role of the user decides what the API allows.
**Why this way:** A table, not free text, so a typo cannot make a new role.
**Other option:** A text column in `users` with a CHECK. It works for a few roles, but the table is easier to extend.

| id | name |
|---|---|
| 1 | CLIENT |
| 2 | RECEPTIONIST |
| 3 | MANAGER |
| 4 | RESTAURANT |
| 5 | SUPPLIER |

Client and supplier have no account (Q-13, Q-17). The seed still creates all 5 roles.

### users
**What:** The staff accounts. Customers do not log in.
**Why:** Staff must log in, and the system must know who did each action.
**Why this way:** The backend hashes the password. The database stores only `password_hash`. There is no `active` column (Q-36). Old reservations and stock movements point to the user, so the hotel does not delete a user who has history.
**Login names:** The database `hotel4you` is created with the collation `SQL_Latin1_General_CP1_CI_AS` (script `database/v1-create-database.sql`). So usernames are not case sensitive. The application trims spaces.
**Other option:** Delete the user who leaves. We did not, because the FKs would break or we would lose history.

| id | name | username | password_hash | role_id |
|---|---|---|---|---|
| 1 | Rita Costa | rita | `$2b$12$...` | 2 |
| 2 | Pedro Lima | manager | `$2b$12$...` | 3 |
| 3 | Joana Reis | joana | `$2b$12$...` | 4 |

---

## Reservations and services

### reservations
**What:** A customer has a room for a range of dates.
**Why:** This is the main business data.
**Why this way:**
- There is no price column. The backend computes the total from `room_types.base_price` and the `reservation_services` rows. The service price is copied in `unit_price`, so old bills do not change.
- CHECK `end_date > start_date`: at least 1 night.
- `status` must match the check-in and check-out dates (CHECK). Example: `checked_in` needs `checked_in_at`.
- The backend checks overlap and locks the room before the INSERT.

**Other option:** Store `price_per_night` in the reservation. We did not, because the total is easy to compute and one price column could differ from the services.

| id | customer_id | room_id | start_date | end_date | status | checked_in_at | checked_out_at | created_by_user_id |
|---|---|---|---|---|---|---|---|---|
| 1 | 1 | 13 | 2026-10-20 | 2026-10-23 | reserved | NULL | NULL | 1 |
| 2 | 2 | 12 | 2026-10-10 | 2026-10-12 | checked_out | 2026-10-10 15:10 | 2026-10-12 11:00 | 1 |

Reservation 1: 3 nights x 90.00 = 270.00, plus services (sea view 15.00 and spa 40.00 = 325.00).

### additional_services
**What:** The list of things a guest can buy or use (spa, pool, laundry, drinks).
**Why:** The hotel needs a price list.
**Why this way:** `price` is the current price only. `price = 0` is a free service. `price = NULL` means no price set yet. The price paid is saved in `reservation_services`. "Sea view" and "mountain view" are rows here (Q-37). The backend allows one view per reservation (Q-11).
**Other option:** Keep a price history table. It is more work and not needed, because `unit_price` already keeps the old price.

| id | name | price |
|---|---|---|
| 1 | spa | 40.00 |
| 2 | pool | 0.00 |
| 3 | drink | 4.50 |
| 4 | sea view | 15.00 |
| 5 | mountain view | 10.00 |

### reservation_services
**What:** The services of one reservation: included, used, or requested to the room.
**Why:** The final bill needs them.
**Why this way:**
- One row for each use. There is no `quantity`. For 3 drinks, the backend adds 3 rows.
- `unit_price` is copied, so old bills do not change. It can be NULL when the service has no price.
- `used_at = NULL` means included but not used yet.
- A room service request (food, drink, laundry) is a row here. `used_at` is the time of the request (Q-33).
- The view of the room is a row here too (sea view or mountain view). One per reservation.
- All FKs are `NO ACTION` (Q-36). To delete a reservation, the backend deletes these rows first, in the same transaction (RF-22).

**Other option:** `quantity` in one row, and `ON DELETE CASCADE`. We did not. One row per use keeps each request time. No CASCADE means the database never deletes data by itself.

| id | reservation_id | additional_service_id | unit_price | used_at |
|---|---|---|---|---|
| 1 | 1 | 1 | 40.00 | NULL |
| 2 | 1 | 4 | 15.00 | NULL |
| 3 | 2 | 3 | 4.50 | 2026-10-11 21:00 |
| 4 | 2 | 3 | 4.50 | 2026-10-11 21:40 |
| 5 | 2 | 3 | 4.50 | 2026-10-11 22:15 |

Reservation 2: 3 x 4.50 = 13.50.

---

## Access control

### access_cards
**What:** The magnetic cards (customer, staff, master).
**Why:** Rooms and places open with a card.
**Why this way:**
- A card belongs to a reservation OR a user, never both (CHECK).
- A master card needs a user (CHECK).
- `card_number` is plain UNIQUE. A number is never used twice.
- The backend creates the card at check-in and sets `active = 0` at check-out.
- `blocked_at` records when a card was blocked (RF-30, Q-26, Q-31). The card row is never deleted, so the access history stays.

**Other option:** `card_number` unique only among active cards (filtered index), to reuse a physical card. We did not. It needs a special index and a reuse rule. A plain UNIQUE is simpler.

| id | card_number | reservation_id | user_id | is_master | active | blocked_at |
|---|---|---|---|---|---|---|
| 1 | 12345 | 2 | NULL | 0 | 0 | NULL |
| 2 | 12346 | 1 | NULL | 0 | 1 | NULL |
| 3 | 90001 | NULL | 2 | 1 | 1 | NULL |

Card 12345 served reservation 2 (now off). After Ana checks in, she gets a new card with a new number for reservation 1. If a card is lost, the backend sets `blocked_at`.

### access_logs
**What:** Every card swipe.
**Why:** The manager must see where a card went, and who entered a room.
**Why this way:** One entry is in a location OR a room (CHECK). Two indexes make the two manager reports fast. Both are plain indexes on (card or room, `entered_at`).
**Other option:** Two log tables (rooms and locations). We did not, because one table is simpler and gives one timeline per card.

| id | access_card_id | location_id | room_id | entered_at |
|---|---|---|---|---|
| 1 | 1 | NULL | 12 | 2026-10-10 15:20 |
| 2 | 1 | 1 | NULL | 2026-10-11 16:00 |
| 3 | 3 | NULL | 13 | 2026-10-12 09:00 |

---

## Feedback

### feedback
**What:** Suggestions and complaints from customers.
**Why:** The hotel wants to improve.
**Why this way:** The FK is to the customer, not to the reservation, so a customer can write after the stay. `customer_id` can be NULL for an anonymous feedback. `type` is limited by a CHECK.
**Other option:** Link to the reservation. It tells which stay the feedback is about, but it was not required.

| id | customer_id | type | description | created_at |
|---|---|---|---|---|
| 1 | 2 | complaint | The air conditioning was loud. | 2026-10-12 11:30 |
| 2 | 1 | suggestion | Add a late breakfast. | 2026-10-14 09:00 |

---

## Delete rules

| You delete | Result |
|---|---|
| a reservation with no check-in and no card | OK. The backend first deletes its `reservation_services` and `reservation_locations`, in the same transaction. |
| a reservation with a card | Fails. The access history stays safe. |
| a floor, room type, room, role or item that is in use | Fails (`NO ACTION`). |
| a user | Do not delete. Old data points to the user. |

## Questions you may get

| Question | Short answer |
|---|---|
| Why not store the price only in `rooms`? | Prices change. Old reservations must keep the price of the booking day. |
| Why a stock column and a movements table? | The column is fast to read. The table is the history. We update both in one transaction. |
| Why `SMALLINT` for the floor number? | `TINYINT` cannot store -1. |
| Why do we not delete users? | Old data points to them. |
| Why CHECKs in the database? | The database protects the data even if the backend has a bug. |
| Why no trigger for overlap? | The backend does it in one transaction with a room lock. It is easier to test and read. |
| Why is there no cascade? | A delete must not erase data by itself. The access history stays safe, and the backend deletes the reservation children in the same transaction. |
| Why is the view not in `rooms`? | The guest chooses the view for the reservation. It is a service (Q-37). |

Created by: BorjaTiago. Updated by: BorjaTiago. AI use: main session, Claude Code (claude-opus-5-5), 2026-10-17; doc-updater, Claude Code (claude-sonnet-5-5), 2026-10-17; PR-20 import decision review by sequence-diagram-editor, OpenAI Codex (GPT-6), 2026-10-22; merge-conflict resolution by sequence-diagram-editor, OpenAI Codex (GPT-6), 2026-10-22; H4Y-28 floors view by doc-updater, Claude Code (claude-haiku-5-5), 2026-10-26; PR-32 seed rows by pr-fixer, Claude Code (claude-opus-5-5), 2026-10-27; H4Y-26 room type name alignment, OpenAI Codex (gpt-6.1-sol), 2026-10-30. H4Y-26 base price by doc-updater, Claude Code (claude-haiku-5-5), 2026-10-30; H4Y-25 room type integration, OpenAI Codex (gpt-6.1-sol), 2026-10-30; review fixes by ecc:doc-updater, Claude Code (claude-haiku-5-5), 2026-10-30. Reviewed by: pending.
