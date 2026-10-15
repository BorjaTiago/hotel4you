# H4Y-10 - Table Guide

Use this file to explain and defend the schema. Each table has the same parts:

- **What:** one sentence.
- **Why:** the problem it solves.
- **Why this way:** the design choice and the reason.
- **Other option:** what we did not do, and why.
- **Example:** rows from one story.

Columns and constraints: [schema.md](schema.md). Diagram: [erd.md](erd.md).

**The story:** Ana Silva books room 202 for 3 nights, uses the spa, and leaves feedback.

## Main ideas (read first)

1. **One fact in one place.** Capacity is in `room_types`, not in every room. A change is made once.
2. **Copy the price when it is sold.** `reservations.price_per_night` and `reservation_services.unit_price` keep the price of that day. Old bills never change.
3. **The database blocks bad data.** CHECK, UNIQUE and FK stop wrong rows, even if the backend has a bug.
4. **Small types.** `TINYINT` for tables with few rows. `INT` for tables that grow. This saves space and shows the expected size.
5. **Keep history.** We do not delete users or cards. We turn them off (`active = 0`).
6. **Complex rules stay in the backend.** Room overlap and stock changes need a transaction. A trigger would hide the logic.

---

## Building and rooms

### floors
**What:** The floors of the hotel (-1 to 3).
**Why:** Rooms and locations need a floor. Staff access uses the floor.
**Why this way:** `number` is `SMALLINT` because `TINYINT` cannot store -1. `id` is separate from `number`, so a renumbering does not break FKs.
**Other option:** Store the floor number inside `rooms`. We did not, because `locations` also needs floors, and the description would repeat.

| id | number | description |
|---|---|---|
| 1 | -1 | Parking and laundry |
| 2 | 0 | Reception, bar and conference rooms |
| 3 | 2 | Rooms 201-210 |
| 4 | 3 | Rooms 301-310 |

### room_types
**What:** The kinds of room: individual, double, family.
**Why:** All rooms of one kind have the same capacity and base price.
**Why this way:** We store capacity and price once. To change a price, we edit one row.
**Other option:** Put `capacity` and `base_price` in `rooms`. We did not, because 40 rooms would repeat the same values, and one edit could be forgotten.

| id | name | capacity | base_price |
|---|---|---|---|
| 1 | individual | 1 | 60.00 |
| 2 | double | 2 | 90.00 |
| 3 | family | 4 | 140.00 |

### rooms
**What:** One row for each physical room.
**Why:** A reservation needs a real room. The room has a floor, a type and a view.
**Why this way:** `view_type` and `view_surcharge` are in `rooms` because the view belongs to one room, not to the type. A CHECK makes the surcharge 0 when there is no view. Price per night = `base_price` + `view_surcharge`.
**Other option:** A `views` table. We did not, because there are only 3 values (none, sea, mountain) and a CHECK is simpler.

| id | number | floor_id | room_type_id | view_type | view_surcharge |
|---|---|---|---|---|---|
| 12 | 201 | 3 | 2 | none | 0.00 |
| 13 | 202 | 3 | 2 | sea | 15.00 |
| 14 | 301 | 4 | 3 | mountain | 10.00 |

Room 202: 90.00 + 15.00 = 105.00 per night.

### locations
**What:** Places with a card reader that are not rooms (pool, restaurant, kitchen).
**Why:** The access log must say where a card entered. Staff may enter only some places.
**Why this way:** A separate table, because a pool is not a room (no type, no price, no reservation).
**Other option:** Put places in `rooms`. We did not, because it would mix two different things and leave many empty columns.

| id | name | floor_id |
|---|---|---|
| 1 | pool | 1 |
| 2 | restaurant | 2 |
| 3 | kitchen | 2 |

---

## Inventory

### items
**What:** The warehouse list (towel, shampoo, sheet).
**Why:** The hotel must know what it has in stock.
**Why this way:** `stock_quantity` is saved in the row, so a read is fast. A CHECK blocks negative stock. `name` is UNIQUE, so one item has one row.
**Other option:** Calculate the stock from `stock_movements` each time. This is always correct, but slow when there are many movements. We keep both and change both in one transaction.

| id | name | stock_quantity |
|---|---|---|
| 1 | towel | 120 |
| 2 | shampoo | 300 |
| 3 | sheet | 80 |

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
**Why:** The manager must see who changed the stock, when, and why. Supplier data from the XML file is saved here.
**Why this way:** `quantity` is always positive. `type` says the effect: `in` adds, `use` and `out` subtract. This avoids sign errors. `supplier_name` only exists for `in` (CHECK).
**Other option:** Negative numbers for `use` and `out`. We did not, because a wrong sign gives wrong stock and the type is already clear.

| id | item_id | type | quantity | moved_at | user_id | supplier_name |
|---|---|---|---|---|---|---|
| 1 | 1 | in | 50 | 2026-10-01 09:00 | 2 | TextilPT |
| 2 | 1 | use | 4 | 2026-10-12 10:30 | 3 | NULL |
| 3 | 2 | out | 10 | 2026-10-13 08:00 | 2 | NULL |

---

## People and security

### customers
**What:** The hotel guests.
**Why:** Every reservation and feedback needs a customer.
**Why this way:** `document` is UNIQUE, so the same person is not created twice. `birthday` is optional, so registration is fast. `NVARCHAR` accepts foreign names.
**Other option:** Use the document as the PK. We did not, because documents can have typos and change, and an `INT` key is smaller in FKs.

| id | name | document | birthday |
|---|---|---|---|
| 1 | Ana Silva | 12345678 | 1990-05-14 |
| 2 | John Müller | X9876543 | NULL |

### roles
**What:** The permission groups: reception, manager, restaurant.
**Why:** The role of the user decides what the API allows.
**Why this way:** A table, not free text, so a typo cannot make a new role.
**Other option:** A text column in `users` with a CHECK. It works for 3 roles, but the table is easier to extend.

| id | name |
|---|---|
| 1 | reception |
| 2 | manager |
| 3 | restaurant |

### users
**What:** The staff accounts. Customers do not log in.
**Why:** Staff must log in, and the system must know who did each action.
**Why this way:** The backend hashes the password. The database stores only `password_hash`. `active = 0` blocks login but keeps the row, because old reservations and stock movements point to this user.
**Other option:** Delete the user who leaves. We did not, because the FKs would break or we would lose history.

| id | name | username | password_hash | role_id | active |
|---|---|---|---|---|---|
| 1 | Rita Costa | rita | `$2b$12$...` | 1 | 1 |
| 2 | Pedro Lima | manager | `$2b$12$...` | 2 | 1 |
| 3 | Joana Reis | joana | `$2b$12$...` | 3 | 0 |

---

## Reservations and services

### reservations
**What:** A customer has a room for a range of dates.
**Why:** This is the main business data.
**Why this way:**
- `price_per_night` is a copy of the price at booking time. A later price change does not change it.
- CHECK `end_date > start_date`: at least 1 night.
- `status` must match the check-in and check-out dates (CHECK). Example: `checked_in` needs `checked_in_at`.
- The backend checks overlap and locks the room before the INSERT.

**Other option:** Calculate the price from `rooms` when needed. We did not, because the price would change in old reservations.

| id | customer_id | room_id | start_date | end_date | price_per_night | status | checked_in_at | checked_out_at | created_by_user_id |
|---|---|---|---|---|---|---|---|---|---|
| 1 | 1 | 13 | 2026-10-20 | 2026-10-23 | 105.00 | reserved | NULL | NULL | 1 |
| 2 | 2 | 12 | 2026-10-10 | 2026-10-12 | 90.00 | checked_out | 2026-10-10 15:10 | 2026-10-12 11:00 | 1 |

Reservation 1: 3 nights x 105.00 = 315.00, plus services.

### additional_services
**What:** The list of things a guest can buy or use (spa, pool, laundry, drinks).
**Why:** The hotel needs a price list.
**Why this way:** `price` is the current price only. `price = 0` is a free service. The price paid is saved in `reservation_services`.
**Other option:** Keep a price history table. It is more work and not needed, because `unit_price` already keeps the old price.

| id | name | price |
|---|---|---|
| 1 | spa | 40.00 |
| 2 | pool | 0.00 |
| 3 | drink | 4.50 |

### reservation_services
**What:** The services of one reservation, used or only included.
**Why:** The final bill needs them.
**Why this way:**
- `unit_price` is copied, so old bills do not change.
- `used_at = NULL` means included but not used yet.
- `ON DELETE CASCADE`: if the manager deletes a reservation, its services go too. They have no meaning alone.

**Other option:** One row per use, with no `quantity`. It is more rows. `quantity` is simpler for "3 drinks".

| id | reservation_id | additional_service_id | quantity | unit_price | used_at |
|---|---|---|---|---|---|
| 1 | 1 | 1 | 1 | 40.00 | NULL |
| 2 | 2 | 3 | 3 | 4.50 | 2026-10-11 21:00 |

Reservation 2: 3 x 4.50 = 13.50.

---

## Access control

### access_cards
**What:** The magnetic cards (customer, staff, master).
**Why:** Rooms and places open with a card.
**Why this way:**
- A card belongs to a reservation OR a user, never both (CHECK).
- A master card needs a user (CHECK).
- `active` lets the hotel reuse the same physical card. `card_number` is unique only among active cards.
- The backend creates the card at check-in and sets `active = 0` at check-out.

**Other option:** `card_number` always UNIQUE. We did not, because a reused card would need a new number or a deleted row, and we would lose history.

| id | card_number | reservation_id | user_id | is_master | active |
|---|---|---|---|---|---|
| 1 | 12345 | 2 | NULL | 0 | 0 |
| 2 | 12345 | 1 | NULL | 0 | 1 |
| 3 | 90001 | NULL | 2 | 1 | 1 |

Card 12345 served reservation 2 (now off). After Ana checks in, it gets a new row for reservation 1.

### access_logs
**What:** Every card swipe.
**Why:** The manager must see where a card went, and who entered a room.
**Why this way:** One entry is in a location OR a room (CHECK). Two indexes make the two manager reports fast.
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
**Why this way:** The FK is to the customer, not to the reservation, so a customer can write after the stay. `type` is limited by a CHECK.
**Other option:** Link to the reservation. It tells which stay the feedback is about, but it was not required.

| id | customer_id | type | description | created_at |
|---|---|---|---|---|
| 1 | 2 | complaint | The air conditioning was loud. | 2026-10-12 11:30 |
| 2 | 1 | suggestion | Add a late breakfast. | 2026-10-14 09:00 |

---

## Delete rules

| You delete | Result |
|---|---|
| a reservation with no check-in and no card | OK. Its `reservation_services` are deleted too. |
| a reservation with a card | Fails. The access history stays safe. |
| a floor, room type, room, role or item that is in use | Fails (`NO ACTION`). |
| a user | Do not delete. Set `active = 0`. |

## Questions you may get

| Question | Short answer |
|---|---|
| Why not store the price only in `rooms`? | Prices change. Old reservations must keep the price of the booking day. |
| Why a stock column and a movements table? | The column is fast to read. The table is the history. We update both in one transaction. |
| Why `SMALLINT` for the floor number? | `TINYINT` cannot store -1. |
| Why do we not delete users? | Old data points to them. We set `active = 0`. |
| Why CHECKs in the database? | The database protects the data even if the backend has a bug. |
| Why no trigger for overlap? | The backend does it in one transaction with a room lock. It is easier to test and read. |
| Why are `access_cards` and `access_logs` not cascade? | A delete must not erase the access history. |
