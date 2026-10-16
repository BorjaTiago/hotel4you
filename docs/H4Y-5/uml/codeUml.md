# Hotel4You - Class Diagram (Organized)

```mermaid
classDiagram
    %% ==========================================
    %% LAYER 1: INTERFACE (JavaFX)
    %% ==========================================
    class ReservationController {
        +onConfirmClicked() void
    }
    class CheckInController {
        +onCheckInClicked() void
    }
    class StockController {
        +onImportXmlClicked() void
    }
    class HotelFacade {
        +createReservation(customer: Customer, room: Room, start: LocalDate, end: LocalDate) Reservation
        +checkIn(reservation: Reservation, card: KeyCard) Stay
        +importStock(file: String) void
    }

    %% ==========================================
    %% LAYER 2: BUSINESS LOGIC (Services)
    %% ==========================================
    class ReservationService {
        +createReservation(...) Reservation
        +cancelReservation(reservation: Reservation) void
    }
    class StayService {
        +checkIn(reservation: Reservation, card: KeyCard) Stay
        +checkOut(stay: Stay) void
    }
    class StockService {
        +importDelivery(delivery: SupplierDelivery) void
    }
    class BillingService {
        +processPayment(reservation: Reservation, amount: BigDecimal, method: PaymentMethod) Invoice
    }

    %% ==========================================
    %% LAYER 2: BUSINESS LOGIC (Domain - Users)
    %% ==========================================
    class User {
        <<abstract>>
        -id: Long
        -username: String
        -passwordHash: String
        -role: Role
        -active: boolean
        +checkPassword(password: String) boolean
    }
    class Employee {
        <<abstract>>
        -name: String
        -contact: String
        -employeeNumber: String
    }
    class Receptionist
    class Manager
    class RestaurantEmployee
    class Customer {
        -id: Long
        -name: String
        -email: String
        -phone: String
        -taxId: String
        -address: String
    }

    %% ==========================================
    %% LAYER 2: BUSINESS LOGIC (Domain - Hotel)
    %% ==========================================
    class Floor {
        -number: int
        -description: String
    }
    class Location {
        <<abstract>>
        -id: Long
        -name: String
    }
    class Room {
        -number: int
        -type: RoomType
        -view: ViewType
        -status: RoomStatus
        -basePrice: BigDecimal
        -capacity: int
        +pricePerNight() BigDecimal
        +isAvailable(start: LocalDate, end: LocalDate) boolean
    }
    class Pool
    class Restaurant
    class Bar
    class ConferenceRoom
    class Spa
    class Kitchen
    class Warehouse
    class Garage
    class Reception
    
    class Reservation {
        -startDate: LocalDate
        -endDate: LocalDate
        -creationDate: LocalDate
        -status: ReservationStatus
        -totalPrice: BigDecimal
        +calculateTotal() BigDecimal
        +confirm() void
        +cancel() void
        +overlaps(start: LocalDate, end: LocalDate) boolean
    }
    class ReservationService {
        -quantity: int
        -unitPrice: BigDecimal
    }
    class Stay {
        -checkInDate: LocalDateTime
        -checkOutDate: LocalDateTime
        +checkIn() void
        +checkOut() void
    }
    class KeyCard {
        -number: String
        -type: CardType
        -active: boolean
        +canOpen(location: Location) boolean
    }
    class AccessLog {
        -dateTime: LocalDateTime
    }

    %% ==========================================
    %% LAYER 2: BUSINESS LOGIC (Domain - Services)
    %% ==========================================
    class Service {
        -name: String
        -description: String
        -price: BigDecimal
    }
    class RoomService
    class LaundryService
    class Catering
    class CulturalEvent
    class PoolAccess
    class SpaService
    class RoomRental
    
    class Consumption {
        -description: String
        -quantity: int
        -unitPrice: BigDecimal
        -date: LocalDate
    }
    class BeverageConsumption
    class LaundryConsumption
    
    class Feedback {
        -date: LocalDate
        -description: String
        -type: FeedbackType
        -response: String
        +reply(text: String) void
    }

    %% ==========================================
    %% LAYER 2: BUSINESS LOGIC (Domain - Stock)
    %% ==========================================
    class StockItem {
        -name: String
        -category: String
        -quantity: int
        -minStock: int
        -price: BigDecimal
        +applyMovement(movement: StockMovement) void
        +isBelowMinimum() boolean
    }
    class StockMovement {
        -date: LocalDate
        -type: MovementType
        -quantity: int
    }
    class Supplier {
        -name: String
        -taxId: String
        -contact: String
    }
    class SupplierDelivery {
        -date: LocalDate
        -xmlFile: String
        +importFromXml(file: String) void
    }
    class DeliveryItem {
        -quantity: int
        -unitPrice: BigDecimal
    }

    %% ==========================================
    %% LAYER 2: BUSINESS LOGIC (Domain - Billing)
    %% ==========================================
    class Payment {
        -date: LocalDate
        -amount: BigDecimal
        -method: PaymentMethod
        +generateInvoice() Invoice
    }
    class Invoice {
        -number: String
        -date: LocalDate
        -totalAmount: BigDecimal
    }

    %% ==========================================
    %% ENUMS
    %% ==========================================
    class Role {
        <<enumeration>>
        RECEPTIONIST
        MANAGER
        RESTAURANT_EMPLOYEE
        CUSTOMER
    }
    class RoomType {
        <<enumeration>>
        SINGLE
        DOUBLE
        FAMILY
    }
    class ViewType {
        <<enumeration>>
        NORMAL
        SEA
        MOUNTAIN
    }
    class RoomStatus {
        <<enumeration>>
        FREE
        OCCUPIED
        MAINTENANCE
        CLEANING
    }
    class ReservationStatus {
        <<enumeration>>
        PENDING
        CONFIRMED
        CANCELLED
        CHECKIN
        CHECKOUT
    }
    class CardType {
        <<enumeration>>
        CUSTOMER
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
        EXIT
        USAGE
    }
    class PaymentMethod {
        <<enumeration>>
        CASH
        CREDIT_CARD
        DEBIT_CARD
        TRANSFER
    }

    %% ==========================================
    %% RELATIONSHIPS
    %% ==========================================
    %% Users
    User <|-- Employee
    Employee <|-- Receptionist
    Employee <|-- Manager
    Employee <|-- RestaurantEmployee
    User <|-- Customer

    %% Hotel
    Location <|-- Room
    Location <|-- Pool
    Location <|-- Restaurant
    Location <|-- Bar
    Location <|-- ConferenceRoom
    Location <|-- Spa
    Location <|-- Kitchen
    Location <|-- Warehouse
    Location <|-- Garage
    Location <|-- Reception

    %% Services
    Service <|-- RoomService
    Service <|-- LaundryService
    Service <|-- Catering
    Service <|-- CulturalEvent
    Service <|-- PoolAccess
    Service <|-- SpaService
    Service <|-- RoomRental

    Consumption <|-- BeverageConsumption
    Consumption <|-- LaundryConsumption

    %% Associations
    Customer "1" --> "*" Reservation : makes
    Room "1" --> "*" Reservation : reserved for
    Reservation "*" --> "*" Service : includes
    ReservationService ..> Reservation : links
    ReservationService ..> Service : links
    Reservation "1" --> "0..1" Stay : originates
    Stay "*" --> "1" KeyCard : uses
    KeyCard "1" --> "*" AccessLog : generates
    AccessLog "*" --> "1" Location : at
    Floor "1" --> "*" Location : contains
    Manager "1" --> "1" KeyCard : uses (master)
    Room "1" --> "*" Consumption : has
    Reservation "0..1" --> "*" Consumption : includes
    Customer "1" --> "*" Feedback : leaves
    Manager "0..1" --> "*" Feedback : replies to
    Supplier "1" --> "*" SupplierDelivery : sends
    SupplierDelivery "1" --> "*" DeliveryItem : contains
    DeliveryItem "*" --> "1" StockItem : refers to
    StockItem "1" --> "*" StockMovement : has
    User "1" --> "*" StockMovement : responsible for
    Reservation "1" --> "*" Payment : pays
    Payment "1" --> "1" Invoice : generates

    %% Dependencies (Interface -> Business)
    ReservationController ..> HotelFacade : uses
    CheckInController ..> HotelFacade : uses
    StockController ..> HotelFacade : uses
    HotelFacade ..> ReservationService : uses
    HotelFacade ..> StayService : uses
    HotelFacade ..> StockService : uses
    HotelFacade ..> BillingService : uses

    %% Note
    note for Business "The business layer does not use JavaFX."