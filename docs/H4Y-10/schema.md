# H4Y-10 - Database Schema (SQL Server)

18 tables. All names in English, snake_case, plural. ER diagram: [erd.md](erd.md).

## Tables

```
floors
  id                    TINYINT IDENTITY PK             -- Internal floor ID.
  number                SMALLINT NOT NULL UNIQUE        -- Floor number (-1 to 3: -1 garage and storeroom; 0 reception, bar, conference rooms; 1 restaurant and pool; 2 and 3 rooms). SMALLINT because TINYINT has no negatives.

room_types
  id                    TINYINT IDENTITY PK             -- Internal room type ID.
  name                  VARCHAR(20) NOT NULL UNIQUE     -- Type name: individual, double, family.
  capacity              TINYINT NOT NULL                -- Maximum number of people (family: up to 4).
  base_price            DECIMAL(8,2) NULL               -- Base price per night for this type. NULL = not set yet.

rooms
  id                    SMALLINT IDENTITY PK            -- Internal room ID.
  number                SMALLINT NOT NULL UNIQUE        -- Visible room number (e.g. 202, 301).
  floor_id              TINYINT NOT NULL FK floors      -- Floor of the room.
  room_type_id          TINYINT NOT NULL FK room_types  -- Room type. Sets the capacity and the base price.

locations
  id                    TINYINT IDENTITY PK             -- Internal location ID.
  name                  VARCHAR(50) NOT NULL            -- Name of a place with a card reader (pool, restaurant, kitchen...).
  floor_id              TINYINT NOT NULL FK floors      -- Floor of the location. Used to check staff access.
  guest_card_access     BIT NOT NULL DEFAULT 0          -- 1 = a client card can open this place. 0 = a client card never opens it (bar, conference rooms, garage, storeroom, private pools, spa; Q-20). See decision 11.
  requires_reservation  BIT NOT NULL DEFAULT 0          -- 1 = the zone needs a reservation to open with the client card (Q-32). Used only when guest_card_access = 1. See decision 11.
  UNIQUE (floor_id, name)                               -- Same name allowed on different floors.

reservation_locations
  reservation_id        INT NOT NULL FK reservations    -- Reservation that may use the location. Part of the PK.
  location_id           TINYINT NOT NULL FK locations   -- Location included in the reservation. Part of the PK.
  PK (reservation_id, location_id)                      -- One location appears once per reservation.

items
  id                    SMALLINT IDENTITY PK            -- Internal item ID.
  name                  VARCHAR(100) NOT NULL UNIQUE    -- Item name (e.g. towel, shampoo, sheet). No repeated names.
  unit                  VARCHAR(20) NOT NULL            -- Unit of the item (e.g. unit, kg, litre, box).
  stock_quantity        INT NOT NULL DEFAULT 0          -- Current quantity in the warehouse. Changes with each stock movement.

room_items
  room_id               SMALLINT NOT NULL FK rooms      -- Room that holds the item. Part of the PK.
  item_id               SMALLINT NOT NULL FK items      -- Item assigned to the room. Part of the PK.
  quantity              TINYINT NOT NULL                -- Units of this item in the room (set by the manager).
  PK (room_id, item_id)                                 -- One item appears once per room.

stock_imports
  id                    INT IDENTITY PK                 -- Internal import ID (one XML file load).
  loaded_at             DATETIME2(0) NOT NULL           -- Date and time of the load.
  user_id               SMALLINT NOT NULL FK users      -- User who loaded the file.
  supplier_name         VARCHAR(100) NOT NULL           -- Supplier that delivered (comes from the XML).
  rejected_items        VARCHAR(MAX) NULL               -- Items of the file that the backend rejected, as text. NULL = none.

stock_movements
  id                    INT IDENTITY PK                 -- Internal movement ID.
  item_id               SMALLINT NOT NULL FK items      -- Item that came in, was used or left.
  type                  VARCHAR(5) NOT NULL             -- Type: in, use, out.
  exit_reason           VARCHAR(10) NULL                -- Reason for an "out": discard, return, loss. NULL for "in" and "use".
  quantity              SMALLINT NOT NULL               -- Quantity moved. Always positive. The type says add or subtract.
  moved_at              DATETIME2(0) NOT NULL           -- Date and time of the movement.
  user_id               SMALLINT NOT NULL FK users      -- Staff member or manager who recorded the movement.
  import_id             INT NULL FK stock_imports       -- XML load that created this movement. NULL = entered by hand.

customers
  id                    INT IDENTITY PK                 -- Internal customer ID.
  name                  NVARCHAR(100) NOT NULL          -- Full name. NVARCHAR to accept foreign names.
  document              VARCHAR(20) NOT NULL UNIQUE     -- ID document (ID card, passport). Avoids duplicate customers.

roles
  id                    TINYINT IDENTITY PK             -- Internal role ID.
  name                  VARCHAR(20) NOT NULL UNIQUE     -- Role name: client, receptionist, manager, restaurant, supplier (the 5 actor types of RF-02).

users
  id                    SMALLINT IDENTITY PK            -- Internal user ID.
  name                  NVARCHAR(100) NOT NULL          -- Staff name. The manager uses it to manage the team.
  username              VARCHAR(50) NOT NULL UNIQUE     -- Login name.
  password_hash         VARCHAR(255) NOT NULL           -- Password hash. Hashed in the backend. Never stores plain text.
  role_id               TINYINT NOT NULL FK roles       -- User role. Sets the permissions.

reservations
  id                    INT IDENTITY PK                 -- Internal reservation ID.
  customer_id           INT NOT NULL FK customers       -- Customer who made the reservation.
  room_id               SMALLINT NOT NULL FK rooms      -- Reserved room.
  start_date            DATE NOT NULL                   -- Planned arrival date (first night).
  end_date              DATE NOT NULL                   -- Planned departure date.
  status                VARCHAR(15) NOT NULL DEFAULT 'reserved' -- Status: reserved, cancelled, checked_in, checked_out.
  checked_in_at         DATETIME2(0) NULL               -- Real check-in date and time. NULL = no check-in yet (manager can delete).
  checked_out_at        DATETIME2(0) NULL               -- Real check-out date and time.
  created_by_user_id    SMALLINT NOT NULL FK users      -- Staff member who created the reservation.

additional_services
  id                    SMALLINT IDENTITY PK            -- Internal service ID.
  name                  VARCHAR(100) NOT NULL UNIQUE    -- Service name (e.g. spa, laundry, drinks, sea view, mountain view).
  price                 DECIMAL(8,2) NULL               -- Current service price. NULL = no price set (see decision 5).

reservation_services
  id                    INT IDENTITY PK                 -- Internal ID of the service in the reservation.
  reservation_id        INT NOT NULL FK reservations    -- Reservation of the service. NO ACTION (see decision 1).
  additional_service_id SMALLINT NOT NULL FK additional_services -- Service included or used.
  unit_price            DECIMAL(8,2) NULL               -- Price at that time. Does not change if the service price changes. NULL = no price.
  used_at               DATETIME2(0) NULL               -- When the service was used. For a room service request: the time of the request. NULL = included but not used yet.

access_cards
  id                    INT IDENTITY PK                 -- Internal card ID.
  card_number           VARCHAR(20) NOT NULL UNIQUE     -- Magnetic card number (e.g. 12345). Never repeats (see decision 7).
  reservation_id        INT NULL FK reservations        -- Reservation of the customer who uses the card. NULL for staff. ON DELETE NO ACTION (see decision 1).
  user_id               SMALLINT NULL FK users          -- Staff member or manager who uses the card. NULL for customers.
  is_master             BIT NOT NULL DEFAULT 0          -- 1 = manager master card (opens all rooms and zones).
  active                BIT NOT NULL DEFAULT 1          -- 1 = card works; 0 = turned off (e.g. after check-out).
  blocked_at            DATETIME2(0) NULL               -- When the card was blocked. NULL = not blocked. The row is never deleted.

access_logs
  id                    INT IDENTITY PK                 -- Internal log ID.
  access_card_id        INT NOT NULL FK access_cards    -- Card that was swiped.
  location_id           TINYINT NULL FK locations       -- Location entered. NULL if it was a room.
  room_id               SMALLINT NULL FK rooms          -- Room entered. NULL if it was a location.
  entered_at            DATETIME2(0) NOT NULL           -- Date and time of the entry.

feedback
  id                    INT IDENTITY PK                 -- Internal ID of the suggestion or complaint.
  customer_id           INT NULL FK customers           -- Customer who gave the feedback. NULL = anonymous.
  type                  VARCHAR(10) NOT NULL            -- Type: suggestion, complaint.
  description           NVARCHAR(500) NOT NULL          -- Text written by the customer.
  created_at            DATETIME2(0) NOT NULL           -- Date and time the feedback was recorded.
```

## Groups

### Building and rooms
- `floors`: floors of the hotel. `number` is `SMALLINT` because it can be negative (-1 to 3).
- `room_types`: individual, double or family. Holds `capacity` and `base_price` per night.
- `rooms`: one row per room. Links to one floor and one room type. The view is not here. It is a service of the reservation (see below).
- `locations`: places with a card reader (pool, restaurant, kitchen). Each one is on one floor. Used to check staff access. `guest_card_access = 0` means a client card never opens the place. `requires_reservation = 1` means the zone needs a reservation to open with the client card. The name is unique per floor.
- `reservation_locations`: locations included in a reservation. Composite PK (`reservation_id`, `location_id`).

### Inventory
- `items`: stock items (towel, shampoo, sheet). `unit` is the unit of the item. `stock_quantity` is the current stock.
- `stock_imports`: one row per XML file load. Holds the supplier, the user, the time and the rejected items.
- `room_items`: how many units of each item a room has. Composite PK (`room_id`, `item_id`). Set by the manager.
- `stock_movements`: every stock change. `type` is `in`, `use` or `out`. `quantity` is always positive. `exit_reason` (discard, return, loss) is only for `out`. `import_id` links to the XML load. Linked to the user who recorded it.

### People and security
- `customers`: hotel guests. `document` is unique to avoid duplicates.
- `roles`: the 5 actor types of RF-02: client, receptionist, manager, restaurant, supplier. The seed creates all 5. Only receptionist, manager and restaurant get accounts (Q-13, Q-17).
- `users`: staff accounts. Store `password_hash` only. There is no `active` column: a user is not disabled by a flag (Q-36).

### Reservations and services
- `reservations`: one customer, one room, a date range and a `status` (reserved, cancelled, checked_in, checked_out). Stores the real check-in and check-out times and the user who created it. No price column: the total is computed from `base_price` and the services.
- `additional_services`: catalogue of services with the current price (spa, laundry, drinks, sea view, mountain view). The view is a service.
- `reservation_services`: services used in a reservation. `unit_price` is a copy of the price at that time. `used_at = NULL` means included but not used yet. Room service requests (food, drink, laundry) use this table. `used_at` is the time of the request.

### Access control
- `access_cards`: a card belongs to a reservation (customer) or to a user (staff). `is_master = 1` opens everything. `blocked_at` records a block.
- `access_logs`: every card swipe. It points to a location or to a room, not both.

### Feedback
- `feedback`: suggestion or complaint from a customer, with free text.

## Relationships

| Parent | Child | FK | Required | ON DELETE |
|---|---|---|---|---|
| floors | rooms | floor_id | yes | NO ACTION |
| floors | locations | floor_id | yes | NO ACTION |
| room_types | rooms | room_type_id | yes | NO ACTION |
| rooms | room_items | room_id | yes | NO ACTION |
| items | room_items | item_id | yes | NO ACTION |
| items | stock_movements | item_id | yes | NO ACTION |
| roles | users | role_id | yes | NO ACTION |
| reservations | reservation_locations | reservation_id | yes | NO ACTION |
| locations | reservation_locations | location_id | yes | NO ACTION |
| users | stock_imports | user_id | yes | NO ACTION |
| stock_imports | stock_movements | import_id | no | NO ACTION |
| users | reservations | created_by_user_id | yes | NO ACTION |
| users | stock_movements | user_id | yes | NO ACTION |
| users | access_cards | user_id | no | NO ACTION |
| customers | reservations | customer_id | yes | NO ACTION |
| customers | feedback | customer_id | no | NO ACTION |
| rooms | reservations | room_id | yes | NO ACTION |
| reservations | reservation_services | reservation_id | yes | NO ACTION |
| additional_services | reservation_services | additional_service_id | yes | NO ACTION |
| reservations | access_cards | reservation_id | no | NO ACTION |
| access_cards | access_logs | access_card_id | yes | NO ACTION |
| locations | access_logs | location_id | no | NO ACTION |
| rooms | access_logs | room_id | no | NO ACTION |

## Constraints

Passwords are hashed in the backend code. The database only stores `password_hash`.

T-SQL has no boolean type. Do not compare two conditions with `=` or `<>`. Use AND/OR.

| Table | Constraint |
|---|---|
| room_types | `capacity > 0`, `base_price IS NULL OR base_price >= 0` |
| locations | `UNIQUE (floor_id, name)`; `guest_card_access` default 0; `requires_reservation` default 0 |
| reservation_locations | PK (`reservation_id`, `location_id`) |
| items | `name` UNIQUE; `stock_quantity >= 0` |
| room_items | PK (`room_id`, `item_id`); `quantity > 0` |
| additional_services | `name` UNIQUE; `price IS NULL OR price >= 0` |
| reservations | `end_date > start_date`; `status` in the 4 values, default `'reserved'`; check-in and check-out dates match the status (see below); `checked_out_at >= checked_in_at` |
| reservation_services | all FKs NO ACTION; `unit_price IS NULL OR unit_price >= 0` |
| access_cards | card belongs to a reservation OR a user; `is_master = 0 OR user_id IS NOT NULL`; `card_number` UNIQUE |
| access_logs | entry is in a location OR a room |
| stock_movements | `type IN ('in','use','out')`; `quantity > 0`; `exit_reason IN ('discard','return','loss')` or NULL; `type = 'out' OR exit_reason IS NULL` |
| stock_imports | `supplier_name` NOT NULL |
| feedback | `type IN ('suggestion','complaint')` |

CHECK constraints that need AND/OR:

```sql
-- reservations
CHECK ((status IN ('checked_in','checked_out') AND checked_in_at IS NOT NULL)
    OR (status IN ('reserved','cancelled')      AND checked_in_at IS NULL))
CHECK ((status = 'checked_out' AND checked_out_at IS NOT NULL)
    OR (status <> 'checked_out' AND checked_out_at IS NULL))

-- access_cards
CHECK ((reservation_id IS NOT NULL AND user_id IS NULL)
    OR (reservation_id IS NULL AND user_id IS NOT NULL))

-- access_logs
CHECK ((location_id IS NOT NULL AND room_id IS NULL)
    OR (location_id IS NULL AND room_id IS NOT NULL))
```

## Indexes

SQL Server does not create indexes on FK columns. Add only the indexes that the main queries need:

```sql
-- Availability and overlap check
CREATE INDEX ix_reservations_room_dates  ON reservations (room_id, start_date, end_date);

-- Manager report: places visited by card X
CREATE INDEX ix_access_logs_card_time    ON access_logs (access_card_id, entered_at);

-- Manager report: cards that entered room Y
CREATE INDEX ix_access_logs_room_time    ON access_logs (room_id, entered_at);

-- FK scans: delete of reservation children, and the "card exists" check on reservation delete
CREATE INDEX ix_reservation_services_reservation ON reservation_services (reservation_id);
CREATE INDEX ix_access_cards_reservation         ON access_cards (reservation_id) WHERE reservation_id IS NOT NULL;

-- Stock history per item
CREATE INDEX ix_stock_movements_item_time ON stock_movements (item_id, moved_at);
```

## Decisions

1. **Delete rules**: keep `NO ACTION` on `access_cards.reservation_id` and `access_logs.access_card_id`. The backend issues a card only at check-in. The manager can delete only a reservation with no check-in, so no card exists at that time. If a card exists, the delete fails. This is correct: it protects the access history. Do not use CASCADE here. The same rule applies to `reservation_services` and `reservation_locations`: all FKs are `NO ACTION`. To delete a reservation (RF-22, only without check-in), the backend deletes its children in the same transaction, then the reservation (Q-36).
2. **Overlap**: the backend checks overlap before the INSERT, in the same transaction. Overlap rule: `new.start_date < existing.end_date AND new.end_date > existing.start_date`, with `existing.status <> 'cancelled'`. No trigger. A transaction alone does not stop two parallel requests. Lock the room first in the same transaction, for example `SELECT id FROM rooms WITH (UPDLOCK, HOLDLOCK) WHERE id = @room_id`, or use `SERIALIZABLE`.
3. **Stock**: the backend updates `items.stock_quantity` and inserts into `stock_movements` in one transaction. If a `use` or `out` makes the stock negative, the CHECK `stock_quantity >= 0` fails. The backend catches the error, does a rollback and shows a message.
4. **Password hash size**: `VARCHAR(255)`. A `VARCHAR` stores only the real length, so this costs no extra space.
5. **Price**: `reservations` has no price column. The backend computes the total from `room_types.base_price` and the `reservation_services` rows (`unit_price` is a copy). `base_price` and `price` can be NULL (not set yet). The backend treats NULL as no charge.
6. **Cancelled after check-in**: not allowed. After check-in, the stay ends with a check-out, not a cancel.

7. **Card number**: `card_number` is plain UNIQUE. A card number never repeats. The backend sets `active = 0` at check-out. A block sets `blocked_at`. The card row is never deleted, so `access_logs` stay as history (RF-30, Q-26, Q-31).
8. **Room items per room**: the manager sets items for each room (`room_items` links `rooms` and `items`). This is more flexible than per room type and costs only one extra row per item.
9. **Views**: "sea view" and "mountain view" are 2 rows in `additional_services`, linked by `reservation_services`. The backend allows one view per reservation (Q-37, Q-11).
10. **Room service**: a request for food, drink or laundry is a `reservation_services` row. `used_at` is the request time (Q-33).
11. **Zones and the client card**: the backend decides in this order. (a) `guest_card_access = 0`: refuse the client card (bar, conference rooms, garage, storeroom, private pools, spa; Q-20). (b) `guest_card_access = 1` and `requires_reservation = 0`: open. (c) `guest_card_access = 1` and `requires_reservation = 1`: open only if `reservation_locations` lists the place for that reservation (restaurant and pool; Q-09, Q-32, RF-18). We chose one extra BIT column because it is the simplest way to say "never". The default is 0, so a new zone is closed to client cards until the manager opens it. Staff and master cards do not use these two flags.
12. **Stock import and exit**: one XML load is one `stock_imports` row. Its movements point to it by `import_id`. The supplier is stored here only (RF-28, Q-10). `exit_reason` (discard, return, loss) explains an `out` (Q-34). `items.unit` gives the unit (Q-38).
