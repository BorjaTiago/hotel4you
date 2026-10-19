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
    }
    class StockMovement {
        type: MovementType
        quantity: int
        dateTime: LocalDateTime
        exitReason: ExitReason
    }
    class StockImport {
        supplierName: String
        importedAt: LocalDateTime
        rejectedItems: List~String~
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
| StockItem | Material stored in the hotel (Stock). | name, unit, stockQuantity |
| StockMovement | Entry, use or exit of a stock item. | type, quantity, dateTime, exitReason |
| StockImport | Import of a supplier XML file. | supplierName, importedAt, rejectedItems |
| Feedback | Suggestion or complaint of a client. | type, description, createdAt |
| Role | Enum: CLIENT, RECEPTIONIST, MANAGER, RESTAURANT, SUPPLIER. | - |
| ReservationStatus | Enum: RESERVED, CANCELLED, CHECKED_IN, CHECKED_OUT. | - |
| CardType | Enum: CLIENT, MASTER. | - |
| FeedbackType | Enum: SUGGESTION, COMPLAINT. | - |
| MovementType | Enum: ENTRY, USE, EXIT. | - |
| ExitReason | Enum: DISCARD, RETURN, LOSS. | - |

## Business rules

- Each card has exactly one owner: a reservation OR a staff user. Each link is `0..1`. Both links set and both links empty are invalid (owner XOR).
- One reservation has `0..1` card (Q-09). One staff user can retain `0..*` cards across history. A manager's blocked old master card and replacement remain linked to that user (Q-19, Q-26, Q-31).
- A master magnetic card belongs to a Manager. It opens all client rooms and all zones.
- A client card opens the room. It opens the restaurant and pool only if the reservation includes them.
- A reservation belongs to one client, one room and one user who created it.
- Only the manager deletes a reservation, and only before check-in. Reception cancels a reservation.
- A reservation extra keeps the unit price at the time of use. Later price changes do not change it.
- A room has one room type. The base price comes from the room type. Extras add to it.
- Each card access record has exactly one card. A card can have `0..*` records. Each record has a room OR a location (each link is `0..1`). The target XOR requires exactly one of them.
- A stock movement of type EXIT has an exit reason (DISCARD, RETURN or LOSS).
- A stock movement from an XML import points to its stock import. Rejected items stay in the stock import.
- A client may leave many feedbacks. The client link is 0..1.

Created by: BorjaTiago. Updated by: pedrocostaisep. AI use: Claude Code agent, Claude Sonnet 5.5, 2026-10-16; cardinality review by sequence-diagram-editor, OpenAI Codex (GPT-6), 2026-10-19. Reviewed by: pending.
