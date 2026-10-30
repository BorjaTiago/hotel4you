# Hotel4You - Class Diagram (Organized)

## Reading notes

- Constructors and getters are not shown.
- `RoomTypeRepository` is in the data layer. `JdbcRoomTypeRepository` is the SQL Server version.
- `MANAGE_PRICES` is allowed for a manager only (H4Y-26).
- `setBasePrice` and `listBasePrices` throw `AccessDeniedException` for other users (H4Y-26).
- `parsePrice` turns the screen text into a price. The invalid price error comes from the service, so the screen has no price rule (RNF-07).
- `PriceController` is a separate screen from `RoomController`. One controller class cannot serve two FXML files with different fields.
- Unit tests use `InMemoryUserRepository` (in `src/test`). It is a test helper, so it is not in this diagram.
- `InMemoryFloorRepository` (in `src/test`) is a test helper, so it is not in this diagram.
- `UserRepository` is in the data layer. `JdbcUserRepository` is the SQL Server version.
- `InMemoryLocationRepository` (in `src/test`) is a test helper, so it is not in this diagram. Its `delete` returns false when other rows use the zone.
- `$` marks a static member.
- `actor` is the logged-in user that `login()` returned. `Permission` holds the roles that may use each function. `Permission.allows(actor)` is false when `actor` is null. `createStaff` throws `AccessDeniedException` when `MANAGE_STAFF` does not allow the actor (H4Y-27, H4Y-60).
- `MANAGE_FLOORS` is allowed for a manager only. `listFloors` and `findFloor` throw `AccessDeniedException` for other users, and `findFloor` throws `FloorNotFoundException` for an unknown floor (H4Y-28). `LocationService` checks `MANAGE_FLOORS` and throws `AccessDeniedException` when it does not allow the actor (H4Y-28).
- `MainController` shows a menu button only when a facade method allows it. The Staff button uses `canUse(actor, MANAGE_STAFF)`. The "Pisos e zonas" button uses `canUse(actor, MANAGE_FLOORS)` (H4Y-28). The "Preços" button uses `canUse(actor, MANAGE_PRICES)` (H4Y-26). The screen has no role check of its own (H4Y-24, H4Y-60).
- `Floor` is a record: the floors are fixed (RF-03). `Location` is a class: the manager creates, edits and removes zones (Q-32). `FloorController` is the one screen for floors and zones (H4Y-28): it has the zone form, so there is no `LocationController` for zone CRUD. `createLocation`, `editLocation` and `removeLocation` need a manager. `removeLocation` throws `LocationInUseException` when `LocationRepository.delete` returns false (Q-36). `HotelFacade.removeLocation(reservation, location)` is the reservation method and stays.

```mermaid
classDiagram
    %% ==========================================
    %% LAYER 1: INTERFACE (JavaFX Controllers)
    %% ==========================================
    class LoginController {
        -onLoginClicked() void
    }
    class MainController {
        -initialize() void
        -onStaffClicked() void
        -onFloorsClicked() void
        -onPricesClicked() void
        -onLogoutClicked() void
    }
    class HotelApp {
        +facade: HotelFacade$
        +currentUser: User$
        +startError: String$
        +start(stage: Stage) void
    }
    class ScreenLoader {
        +load(fxmlName: String) Parent
    }
    class RoomController {
        +onSearchClicked() void
        +onSaveRoomClicked() void
        +onConfigureRoomItemClicked() void
    }
    class ClientController {
        +onRegisterClientClicked() void
        +onSearchClientClicked() void
    }
    class ReservationController {
        +onCreateReservationClicked() void
        +onCancelReservationClicked() void
        +onDeleteReservationClicked() void
        +onAddExtraClicked() void
        +onRemoveExtraClicked() void
        +onAddLocationClicked() void
        +onRemoveLocationClicked() void
    }
    class CheckInOutController {
        +onCheckInClicked() void
        +onCheckOutClicked() void
    }
    class StaffController {
        -initialize() void
        -onCreateStaffClicked() void
        +onEditStaffClicked() void
        +onRemoveStaffClicked() void
        +onListUsersClicked() void
    }
    class CardController {
        +onIssueCardClicked() void
        +onIssueMasterCardClicked() void
        +onBlockMasterCardClicked() void
        +onShowPlacesByCardClicked() void
        +onShowPassesByRoomClicked() void
    }
    class StockController {
        +onImportXmlClicked() void
        +onSaveItemClicked() void
        +onRegisterMovementClicked() void
    }
    class FeedbackController {
        +onSaveFeedbackClicked() void
        +onListFeedbackClicked() void
    }
    class FloorController {
        -initialize() void
        -onSearchClicked() void
        -onCreateLocationClicked() void
        -onSaveLocationClicked() void
        -onRemoveLocationClicked() void
    }
    class PriceController {
        -initialize() void
        -onSavePriceClicked() void
    }

    %% ==========================================
    %% LAYER 2: BUSINESS - FACADE
    %% ==========================================
    class HotelFacade {
        +login(username: String, password: String) User
        +createReservation(client: Client, room: Room, start: LocalDate, end: LocalDate) Reservation
        +cancelReservation(reservation: Reservation) void
        +deleteReservation(reservation: Reservation, user: User) void
        +addExtra(reservation: Reservation, service: ExtraService, quantity: int) ReservationExtra
        +removeExtra(reservation: Reservation, extra: ReservationExtra) void
        +addLocation(reservation: Reservation, location: Location) void
        +removeLocation(reservation: Reservation, location: Location) void
        +registerClient(name: String, document: String, birthday: LocalDate) Client
        +findClient(document: String) Client
        +checkIn(reservation: Reservation) void
        +checkOut(reservation: Reservation) void
        +issueCard(reservation: Reservation) MagneticCard
        +issueMasterCard(manager: Manager) MagneticCard
        +blockMasterCard(card: MagneticCard) void
        +getPlacesByCard(card: MagneticCard) List~CardAccessRecord~
        +getPassesByRoom(room: Room) List~CardAccessRecord~
        +createStaff(name: String, username: String, password: String, role: Role, actor: User) void
        +canUse(actor: User, permission: Permission) boolean
        +editStaff(user: User) void
        +removeStaff(user: User) void
        +listUsers() List~User~
        +searchRooms(start: LocalDate, end: LocalDate, type: RoomType) List~Room~
        +saveRoom(room: Room) Room
        +configureRoomItem(room: Room, item: StockItem, quantity: int) void
        +setBasePrice(type: RoomType, price: BigDecimal, actor: User) void
        +listBasePrices(actor: User) List~RoomType~
        +parsePrice(text: String) BigDecimal
        +setExtraPrice(service: ExtraService, price: BigDecimal) void
        +createLocation(floorNumber: int, name: String, guestCardAccess: boolean, requiresReservation: boolean, actor: User) Location
        +editLocation(id: long, floorNumber: int, name: String, guestCardAccess: boolean, requiresReservation: boolean, actor: User) Location
        +removeLocation(id: long, actor: User) void
        +importStock(file: String, user: User) StockImport
        +createItem(name: String, unit: String) StockItem
        +editItem(item: StockItem) void
        +registerMovement(item: StockItem, type: MovementType, quantity: int, exitReason: ExitReason, user: User) StockMovement
        +saveFeedback(client: Client, type: FeedbackType, description: String) Feedback
        +listFeedback() List~Feedback~
        +listFloors(actor: User) List~Floor~
        +findFloor(number: int, actor: User) Floor
    }

    %% ==========================================
    %% LAYER 2: BUSINESS - SERVICES
    %% ==========================================
    class AuthService {
        +login(username: String, password: String) User
        +hashPassword(password: String) String
        -isValidPassword(password: String, passwordHash: String) boolean
    }
    class ClientService {
        +register(name: String, document: String, birthday: LocalDate) Client
        +find(document: String) Client
    }
    class LocationService {
        +createLocation(floorNumber: int, name: String, guestCardAccess: boolean, requiresReservation: boolean, actor: User) Location
        +editLocation(id: long, floorNumber: int, name: String, guestCardAccess: boolean, requiresReservation: boolean, actor: User) Location
        +removeLocation(id: long, actor: User) void
        +listFloors(actor: User) List~Floor~
        +findFloor(number: int, actor: User) Floor
    }
    class RoomService {
        +isAvailable(room: Room, start: LocalDate, end: LocalDate) boolean
        +searchRooms(start: LocalDate, end: LocalDate, type: RoomType) List~Room~
        +saveRoom(room: Room) Room
        +configureItem(room: Room, item: StockItem, quantity: int) void
        +setBasePrice(type: RoomType, price: BigDecimal, actor: User) void
        +listBasePrices(actor: User) List~RoomType~
        +parsePrice(text: String) BigDecimal
        +setExtraPrice(service: ExtraService, price: BigDecimal) void
    }
    class ReservationService {
        +create(client: Client, room: Room, start: LocalDate, end: LocalDate) Reservation
        +cancel(reservation: Reservation) void
        +delete(reservation: Reservation, user: User) void
        +addExtra(reservation: Reservation, service: ExtraService, quantity: int) ReservationExtra
        +removeExtra(reservation: Reservation, extra: ReservationExtra) void
        +addLocation(reservation: Reservation, location: Location) void
        +removeLocation(reservation: Reservation, location: Location) void
        +checkIn(reservation: Reservation) void
        +checkOut(reservation: Reservation) void
    }
    class StaffService {
        +createStaff(name: String, username: String, password: String, role: Role, actor: User) void
        +editStaff(user: User) void
        +removeStaff(user: User) void
        +listUsers() List~User~
    }
    class CardAccessService {
        +issueCard(reservation: Reservation) MagneticCard
        +issueMasterCard(manager: Manager) MagneticCard
        +blockCard(card: MagneticCard) void
        +canOpen(card: MagneticCard, location: Location) boolean
        +canOpenRoom(card: MagneticCard, room: Room) boolean
        +register(card: MagneticCard, room: Room, location: Location) CardAccessRecord
        +getPlacesByCard(card: MagneticCard) List~CardAccessRecord~
        +getPassesByRoom(room: Room) List~CardAccessRecord~
    }
    class StockService {
        +importStock(file: String, user: User) StockImport
        +createItem(name: String, unit: String) StockItem
        +editItem(item: StockItem) void
        +registerMovement(item: StockItem, type: MovementType, quantity: int, exitReason: ExitReason, user: User) StockMovement
        +applyMovement(item: StockItem, type: MovementType, quantity: int, exitReason: ExitReason, user: User) StockMovement
    }
    class FeedbackService {
        +save(client: Client, type: FeedbackType, description: String) Feedback
        +list() List~Feedback~
    }
    class LoginFailedException {
        <<exception>>
    }
    class AccessDeniedException {
        <<exception>>
    }
    class UsernameTakenException {
        <<exception>>
    }
    class FloorNotFoundException {
        <<exception>>
    }
    class LocationNotFoundException {
        <<exception>>
    }
    class LocationNameTakenException {
        <<exception>>
    }
    class LocationInUseException {
        <<exception>>
    }

    %% ==========================================
    %% LAYER: CONFIG
    %% ==========================================
    class AppConfig {
        <<record>>
        +dbUrl: String
        +dbUser: String
        +dbPassword: String
        +load(file: Path) AppConfig$
        +toString() String
    }
    class Main {
        +main(args: String[]) void$
    }

    %% ==========================================
    %% LAYER: DATA
    %% ==========================================
    class UserRepository {
        <<interface>>
        +findAll() List~User~
        +findByUsername(username: String) Optional~User~
        +existsByUsername(username: String) boolean
        +save(user: User) User
    }
    class FloorRepository {
        <<interface>>
        +findAll() List~Floor~
        +findByNumber(number: int) Optional~Floor~
    }
    class LocationRepository {
        <<interface>>
        +findById(id: long) Optional~Location~
        +findByFloorAndName(floorNumber: int, name: String) Optional~Location~
        +save(floorNumber: int, location: Location) Location
        +update(floorNumber: int, location: Location) void
        +delete(id: long) boolean
    }
    class Database {
        +NOT_AVAILABLE String$
        +connect() Connection
        +check() void
        +prepare(connection: Connection, sql: String, values: String...) PreparedStatement$
    }
    class RoomTypeRepository {
        <<interface>>
        +findAll() List~RoomType~
        +updateBasePrice(id: long, price: BigDecimal) boolean
    }
    class JdbcUserRepository
    class JdbcFloorRepository
    class JdbcLocationRepository
    class JdbcRoomTypeRepository

    %% ==========================================
    %% LAYER 3: DOMAIN - USERS
    %% ==========================================
    class User {
        <<abstract>>
        -id: Long
        -name: String
        -username: String
        -passwordHash: String
        -role: Role
    }
    class Receptionist
    class Manager
    class RestaurantStaff
    class Client {
        -id: Long
        -name: String
        -document: String
        -birthday: LocalDate
    }

    %% ==========================================
    %% LAYER 3: DOMAIN - HOTEL
    %% ==========================================
    class Floor {
        <<record>>
        +number: int
        +description: String
    }
    class Location {
        -id: Long
        -name: String
        -guestCardAccess: boolean
        -requiresReservation: boolean
    }
    class Room {
        -id: Long
        -number: int
    }
    class RoomType {
        -id: Long
        -name: String
        -capacity: int
        -basePrice: BigDecimal
    }
    class RoomItem {
        -quantity: int
    }
    class Reservation {
        -id: Long
        -startDate: LocalDate
        -endDate: LocalDate
        -status: ReservationStatus
        -checkedInAt: LocalDateTime
        -checkedOutAt: LocalDateTime
        +checkIn() void
        +checkOut() void
        +cancel() void
        +overlaps(start: LocalDate, end: LocalDate) boolean
        +calculateTotal() BigDecimal
    }
    class ReservationExtra {
        -quantity: int
        -unitPrice: BigDecimal
        -usedAt: LocalDateTime
    }
    class MagneticCard {
        -id: Long
        -number: String
        -type: CardType
        -active: boolean
        -blockedAt: LocalDateTime
    }
    class CardAccessRecord {
        -entryDateTime: LocalDateTime
    }

    %% ==========================================
    %% LAYER 3: DOMAIN - EXTRAS
    %% ==========================================
    class ExtraService {
        -id: Long
        -name: String
        -price: BigDecimal
    }

    %% ==========================================
    %% LAYER 3: DOMAIN - STOCK
    %% ==========================================
    class StockItem {
        -id: Long
        -name: String
        -unit: String
        -stockQuantity: int
    }
    class StockMovement {
        -type: MovementType
        -quantity: int
        -dateTime: LocalDateTime
        -exitReason: ExitReason
    }
    class StockImport {
        -supplierName: String
        -importedAt: LocalDateTime
    }

    %% ==========================================
    %% LAYER 3: DOMAIN - FEEDBACK
    %% ==========================================
    class Feedback {
        -type: FeedbackType
        -description: String
        -createdAt: LocalDateTime
    }

    %% ==========================================
    %% ENUMS
    %% ==========================================
    class Role {
        <<enumeration>>
        CLIENT
        RECEPTIONIST
        MANAGER
        RESTAURANT
        SUPPLIER
    }
    class Permission {
        <<enumeration>>
        MANAGE_STAFF
        MANAGE_FLOORS
        MANAGE_PRICES
        -allowedRoles: Set~Role~
        +allows(actor: User) boolean
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

    %% ==========================================
    %% INHERITANCES
    %% ==========================================
    User <|-- Receptionist
    User <|-- Manager
    User <|-- RestaurantStaff

    %% ==========================================
    %% ASSOCIATIONS
    %% ==========================================
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

    %% ==========================================
    %% DEPENDENCIES (Controllers -> Facade)
    %% ==========================================
    LoginController ..> HotelFacade : uses
    MainController ..> HotelFacade : uses
    RoomController ..> HotelFacade : uses
    ClientController ..> HotelFacade : uses
    ReservationController ..> HotelFacade : uses
    CheckInOutController ..> HotelFacade : uses
    StaffController ..> HotelFacade : uses
    CardController ..> HotelFacade : uses
    StockController ..> HotelFacade : uses
    FeedbackController ..> HotelFacade : uses
    FloorController ..> HotelFacade : uses
    PriceController ..> HotelFacade : uses

    %% ==========================================
    %% DEPENDENCIES (Facade -> Services)
    %% ==========================================
    HotelFacade ..> AuthService : uses
    HotelFacade ..> ClientService : uses
    HotelFacade ..> RoomService : uses
    HotelFacade ..> LocationService : uses
    HotelFacade ..> ReservationService : uses
    HotelFacade ..> StaffService : uses
    HotelFacade ..> CardAccessService : uses
    HotelFacade ..> StockService : uses
    HotelFacade ..> FeedbackService : uses
    StaffService ..> AuthService : uses
    AuthService ..> UserRepository : uses
    StaffService ..> UserRepository : uses
    LocationService ..> FloorRepository : uses
    LocationService ..> LocationRepository : uses
    RoomService ..> RoomTypeRepository : uses
    UserRepository ..> User : stores
    FloorRepository ..> Floor : stores
    LocationRepository ..> Location : stores
    RoomTypeRepository ..> RoomType : stores
    JdbcUserRepository ..|> UserRepository : implements
    JdbcUserRepository ..> Database : uses
    JdbcFloorRepository ..|> FloorRepository : implements
    JdbcFloorRepository ..> Database : uses
    JdbcLocationRepository ..|> LocationRepository : implements
    JdbcLocationRepository ..> Database : uses
    JdbcRoomTypeRepository ..|> RoomTypeRepository : implements
    JdbcRoomTypeRepository ..> Database : uses
    Database ..> AppConfig : uses
    AuthService ..> LoginFailedException : throws
    StaffService ..> UsernameTakenException : throws
    StaffService ..> AccessDeniedException : throws
    LocationService ..> FloorNotFoundException : throws
    LocationService ..> AccessDeniedException : throws
    LocationService ..> LocationNotFoundException : throws
    LocationService ..> LocationNameTakenException : throws
    LocationService ..> LocationInUseException : throws
    RoomService ..> AccessDeniedException : throws
    HotelFacade ..> Permission : uses
    StaffService ..> Permission : uses
    LocationService ..> Permission : uses
    RoomService ..> Permission : uses
    Permission ..> Role : uses

    %% ==========================================
    %% DEPENDENCIES (Start-up)
    %% ==========================================
    Main ..> AppConfig : uses
    Main ..> HotelApp : starts
    Main ..> Database : creates
    Main ..> JdbcUserRepository : creates
    Main ..> JdbcFloorRepository : creates
    Main ..> JdbcLocationRepository : creates
    Main ..> JdbcRoomTypeRepository : creates
    Main ..> AuthService : creates
    Main ..> StaffService : creates
    Main ..> RoomService : creates
    Main ..> HotelFacade : creates
    HotelApp ..> ScreenLoader : uses
    LoginController ..> ScreenLoader : opens main window
    MainController ..> ScreenLoader : opens login on logout
    ScreenLoader ..> HotelFacade : gives to controllers

    %% ==========================================
    %% NOTE
    %% ==========================================
    note for HotelFacade "The business layer does not use JavaFX."
    note for FeedbackService "client may be null (anonymous feedback)"
    note for CardAccessRecord "Each record has a Room OR a Location, never both."
    note for ReservationService "addLocation and removeLocation accept only locations with guestCardAccess = true and requiresReservation = true. Allowed only before check-in."
    note for CardAccessService "Master card opens every room and every zone.<br/>Client card opens only its own room and zones with guestCardAccess = true. If requiresReservation = true, only zones included in its reservation.<br/>A blocked or inactive card opens nothing.<br/>Receptionist card opens only zones on floors 0 and 1 and no guest room.<br/>Restaurant staff card opens only the kitchen and the restaurant and no guest room.<br/>A staff card never opens a guest room. Only the master card does."
    note for MagneticCard "A card belongs to a Reservation OR a staff User, never both."
    note for StockService "importStock accepts all items from a valid XML file.<br/>Create missing catalogue items, add stock and ENTRY movements.<br/>Save or undo items, import, movements and balances together (RF-28, Q-10, Q-38)."
```

Created by: BorjaTiago. Updated by: BorjaTiago. AI use: Claude Code, claude-sonnet-5-5, 2026-10-16; doc-updater, Claude Code (claude-sonnet-5-5), 2026-10-17; doc-updater, Claude Code (claude-haiku-5-5), 2026-10-17; doc-updater, Claude Code (claude-haiku-5-5), 2026-10-19; PR-20 import decision review by sequence-diagram-editor, OpenAI Codex (GPT-6), 2026-10-22; merge-conflict resolution by sequence-diagram-editor, OpenAI Codex (GPT-6), 2026-10-22; doc-updater, Claude Code (claude-opus-5-5), 2026-10-23; H4Y-27 staff account update, Claude Code (claude-opus-5-5), 2026-10-23; H4Y-28 floors view by doc-updater, Claude Code (claude-haiku-5-5), 2026-10-26; first-manager removal by doc-updater, Claude Code (claude-haiku-5-5), 2026-10-26; Floor record note by doc-updater, Claude Code (claude-haiku-5-5), 2026-10-26; H4Y-28 zone merge by doc-updater, Claude Code (claude-sonnet-5-5), 2026-10-26; H4Y-60 role permission rule, Claude Code (claude-opus-5-5), 2026-10-27; PR #33 review change, Claude Code (claude-opus-5-5), 2026-10-27; merge with main by Claude Code (claude-opus-5-5), 2026-10-27; H4Y-28 floors permission by doc-updater, Claude Code (claude-haiku-5-5), 2026-10-27. H4Y-26 base price by doc-updater, Claude Code (claude-haiku-5-5), 2026-10-30. Reviewed by: pending.
