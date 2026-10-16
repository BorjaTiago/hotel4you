# Hotel4You - Domain Model

This document shows the business concepts of H4Y (Hotel4You). It has no controllers, facade or services. It has no methods. It comes from [class-diagram.md](class-diagram.md), [classes.md](classes.md) and the [glossary](../requirements/glossary.md).

## Diagram

```mermaid
classDiagram
    class User {
        <<abstract>>
        id: Long
        name: String
        username: String
        passwordHash: String
        role: Role
    }
    class Receptionist
    class Manager
    class RestaurantStaff
    class Client {
        id: Long
        name: String
        document: String
        birthday: LocalDate
    }
    class Floor {
        number: int
        description: String
    }
    class Location {
        id: Long
        name: String
        guestCardAccess: boolean
        requiresReservation: boolean
    }
    class Room {
        id: Long
        number: int
    }
    class RoomType {
        id: Long
        name: String
        capacity: int
        basePrice: BigDecimal
    }
    class RoomItem {
        quantity: int
    }
    class Reservation {
        id: Long
        startDate: LocalDate
        endDate: LocalDate
        status: ReservationStatus
        checkedInAt: LocalDateTime
        checkedOutAt: LocalDateTime
    }
    class ExtraService {
        id: Long
        name: String
        price: BigDecimal
    }
    class ReservationExtra {
        quantity: int
        unitPrice: BigDecimal
        usedAt: LocalDateTime
    }
    class MagneticCard {
        id: Long
        number: String
        type: CardType
        active: boolean
        blockedAt: LocalDateTime
    }
    class CardAccessRecord {
        entryDateTime: LocalDateTime
    }
    class StockItem {
        id: Long
        name: String
        unit: String
        stockQuantity: int
        price: BigDecimal
    }
    class StockMovement {
        type: MovementType
        quantity: int
        dateTime: LocalDateTime
        exitReason: ExitReason
        unitPrice: BigDecimal
    }
    class StockImport {
        supplierName: String
        importedAt: LocalDateTime
        fileHash: String
        createdCount: int
        updatedCount: int
        invalidCount: int
        invalidItems: List~String~
    }
    class Feedback {
        type: FeedbackType
        description: String
        createdAt: LocalDateTime
    }
    class Role {
        <<enumeration>>
        CLIENT
        RECEPTIONIST
        MANAGER
        RESTAURANT
        SUPPLIER
    }
    class ReservationStatus {
        <<enumeration>>
        RESERVED
        CANCELLED
        CHECKED_IN
        CHECKED_OUT
    }
    class CardType {
        <<enumeration>>
        CLIENT
        MASTER
    }
    class FeedbackType {
        <<enumeration>>
        SUGGESTION
        COMPLAINT
    }
    class MovementType {
        <<enumeration>>
        ENTRY
        USE
        EXIT
    }
    class ExitReason {
        <<enumeration>>
        DISCARD
        RETURN
        LOSS
    }

    User <|-- Receptionist
    User <|-- Manager
    User <|-- RestaurantStaff

    Client "1" --> "*" Reservation : makes
    Room "1" --> "*" Reservation : reserved in
    User "1" --> "*" Reservation : creates
    Reservation "1" *-- "*" ReservationExtra : includes
    ReservationExtra "*" --> "1" ExtraService : refers to
    Reservation "0..1" --> "0..1" MagneticCard : has
    User "0..1" --> "*" MagneticCard : owns
    MagneticCard "1" --> "*" CardAccessRecord : generates
    CardAccessRecord "*" --> "0..1" Room : at
    CardAccessRecord "*" --> "0..1" Location : at
    Floor "1" --> "*" Location : contains
    Floor "1" --> "*" Room : contains
    Room "*" --> "1" RoomType : has
    Room "1" --> "*" RoomItem : has
    RoomItem "*" --> "1" StockItem : refers to
    Reservation "*" --> "*" Location : includes
    Client "0..1" --> "*" Feedback : leaves
    StockItem "1" --> "*" StockMovement : has
    User "1" --> "*" StockMovement : records
    StockMovement "*" --> "0..1" StockImport : from
    StockItem "*" --> "0..1" StockImport : created by
    StockImport "*" --> "1" User : imported by
```

## Concepts

| Concept | Meaning | Main attributes |
|---------|---------|-----------------|
| User | Staff member who logs in. It is abstract. | name, username, passwordHash, role |
| Receptionist | User who does reservations, check-in, check-out and cards. | (from User) |
| Manager | User who manages rooms, prices, staff, stock and the master magnetic card. | (from User) |
| RestaurantStaff | User of the restaurant. | (from User) |
| Client | Hotel guest. | name, document, birthday |
| Floor | Floor of the hotel (-1, 0, 1, 2, 3). | number, description |
| Location | Hotel place with card access, such as restaurant, pool, garage or conference room. | name, guestCardAccess, requiresReservation |
| Room | Sleeping room on a floor. | number |
| RoomType | Type of room with its capacity and base price per night. | name, capacity, basePrice |
| RoomItem | Quantity of a stock item set in a room (Room item). | quantity |
| Reservation | Booking of a room by a client for one or more nights. | startDate, endDate, status, checkedInAt, checkedOutAt |
| ExtraService | Service that adds to the price (Extra service). | name, price |
| ReservationExtra | Use of an extra service in a reservation. It keeps the price at the time. | quantity, unitPrice, usedAt |
| MagneticCard | Magnetic card with a number. Type CLIENT or MASTER (Master magnetic card). | number, type, active, blockedAt |
| CardAccessRecord | Saved record of a card entry (Card access record). | entryDateTime |
| StockItem | Material stored in the hotel (Stock). | name, unit, stockQuantity, price |
| StockMovement | Entry, use or exit of a stock item. | type, quantity, dateTime, exitReason, unitPrice |
| StockImport | Import of a supplier XML file. | supplierName, importedAt, fileHash, createdCount, updatedCount, invalidCount, invalidItems |
| Feedback | Suggestion or complaint of a client. | type, description, createdAt |
| Role | Enum: CLIENT, RECEPTIONIST, MANAGER, RESTAURANT, SUPPLIER. | - |
| ReservationStatus | Enum: RESERVED, CANCELLED, CHECKED_IN, CHECKED_OUT. | - |
| CardType | Enum: CLIENT, MASTER. | - |
| FeedbackType | Enum: SUGGESTION, COMPLAINT. | - |
| MovementType | Enum: ENTRY, USE, EXIT. | - |
| ExitReason | Enum: DISCARD, RETURN, LOSS. | - |

## Business rules

- A card owner is 0..1. A card belongs to a reservation (client card) OR to a staff user (master card), never both.
- A master magnetic card belongs to a Manager. It opens all client rooms and all zones.
- A client card opens the room. It opens the restaurant and pool only if the reservation includes them.
- A reservation belongs to one client, one room and one user who created it.
- Only the manager deletes a reservation, and only before check-in. Reception cancels a reservation.
- A reservation extra keeps the unit price at the time of use. Later price changes do not change it.
- A room has one room type. The base price comes from the room type. Extras add to it.
- A card access record points to a room or a location (each 0..1).
- A stock movement of type EXIT has an exit reason (DISCARD, RETURN or LOSS).
- A stock movement from an XML import points to its stock import. The entry copies the unit price at that time. The stock import keeps the counters and the format-invalid items. A stock item made by an import points to it. The same file (same hash) cannot be imported twice: the system refuses it, records nothing, and shows an error message (no stock import). The import is an upsert (Q-48). Each XML item has a mandatory name, quantity, unit and price. An existing item (same name, trimmed, case-insensitive) with the same unit gets an entry and a new price. A different unit is format-invalid. A new item is created with the XML unit. A missing or invalid unit or price is format-invalid.
- A client may leave many feedbacks. The client link is 0..1.

AI use: Claude Code agent, Claude Sonnet 5.5, 2026-10-16. Also edited by doc-updater, Claude Sonnet 5.5, 2026-10-16. XML unit/price rule by docs-updater, Claude Code (claude-haiku-5-5), 2026-10-16. Reviewed by: pending.
