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
- `RoomRepository` stores rooms and reads the floor and room type catalogues. `JdbcRoomRepository` uses SQL Server. Tests use an in-memory implementation.
- `InMemoryLocationRepository` (in `src/test`) is a test helper, so it is not in this diagram. Its `delete` returns false when other rows use the zone.
- The stock catalogue (H4Y-51) uses `Permission.MANAGE_STOCK`, for a manager only. `StockService.createItem`, `editItem` and `listItems` throw `AccessDeniedException` for other users. A new item starts with `stockQuantity` 0. `editItem` changes only the name and the unit, so the stock stays. Two items cannot have the same name: `createItem` and `editItem` throw `StockItemNameTakenException`. `editItem` throws `StockItemNotFoundException` for an unknown item. No item is removed (Q-38).
- `StockController` is one screen for the stock catalogue (H4Y-51). The manager selects a row, and the form fills. `onClearClicked` clears the form, the table selection and the message. `onImportXmlClicked` and `onRegisterMovementClicked` are future methods. They are not in the code yet.
- `ItemRepository` is in the data layer. `JdbcItemRepository` is the SQL Server version. `InMemoryItemRepository` (in `src/test`) is a test helper, so it is not in this diagram.
- `$` marks a static member.
- `ClientService.register` checks `Permission.MANAGE_CLIENTS` (receptionist only; clients do not log in, Q-13). It trims the name and the document. A blank field throws `IllegalArgumentException` with the field name. The name has at most 100 and the document at most 20 characters (the `customers` columns). A client with the same document (not case sensitive) throws `ClientExistsException`. `JdbcClientRepository.save` also throws it when the database finds the same document (error 2601 or 2627). The client has no birthday: the `customers` table has no birthday column (team decision in `docs/database/tables.md`) (H4Y-52).
- `InMemoryClientRepository` (in `src/test`) is a test helper, so it is not in this diagram.
- `actor` is the logged-in user that `login()` returned. `Permission` holds the roles that may use each function. `Permission.allows(actor)` is false when `actor` is null. `createStaff` throws `AccessDeniedException` when `MANAGE_STAFF` does not allow the actor (H4Y-27, H4Y-60).
- `MANAGE_FLOORS` is allowed for a manager only. `listFloors` and `findFloor` throw `AccessDeniedException` for other users, and `findFloor` throws `FloorNotFoundException` for an unknown floor (H4Y-28). `LocationService` checks `MANAGE_FLOORS` and throws `AccessDeniedException` when it does not allow the actor (H4Y-28).
- `MainController` shows a menu button only when `canUse(actor, permission)` allows it. Staff uses `MANAGE_STAFF`, floors and zones use `MANAGE_FLOORS`, prices use `MANAGE_PRICES`, and the "Quartos" button uses `LIST_ROOMS`. The screen has no role check of its own (H4Y-24, H4Y-60). The top shows "Olá, <name> (<role>)" with `Role.displayName()`. Without a logged-in user, no button shows and `openLogin()` opens the login screen (H4Y-61).
- `RoomService.saveRoom` checks `Permission.MANAGE_ROOMS`. Only a manager may register a room. The service checks the floor catalogue, room floor, room type and duplicate number. The repository checks the floor limit inside its save transaction (H4Y-25).
- `RoomService.removeRoom` checks `Permission.MANAGE_ROOMS`. It throws `RoomInUseException` when `RoomRepository.delete` returns false (Q-36). `RoomRepository.delete` throws `IllegalArgumentException` for an unknown room. The manager removes a room on the "Quartos" screen ("Remover quarto", then a confirmation). "Limpar" clears the form, the table selection and the message (user decision, 2026-10-30).
- `RoomController` is one screen ("Quartos") for the room table, the type filter, the registration form, the edit form and the room summary (H4Y-50). The table and filter are for every user with `LIST_ROOMS`. The form and the summary show only when `canUse(actor, Permission.MANAGE_ROOMS)` is true, so a receptionist sees the table and the filter only. The manager selects a row. The form fills. The manager changes the number, floor or type and clicks "Guardar alterações" (`onSaveRoomChangesClicked`). The screen asks for a confirmation, then calls `HotelFacade.editRoom`. The floor box is a `ComboBox<Floor>`. For the manager it calls `listFloors(actor)` and keeps only the floors in `roomFloorLimits()`. Layout, top to bottom: title, type filter, summary, table, form with the "Registar quarto" and "Guardar alterações" buttons, message.
- `RoomController` calls `listRoomSummaries` for the table. For the manager only, it calls `listRooms` for the summary, which always counts all rooms. `listRooms(actor)` and `listRoomTypes(actor)` require `MANAGE_ROOMS`. The controller passes the logged-in user through the facade.
- `RoomService.listRoomSummaries` checks `Permission.LIST_ROOMS`. A receptionist or a manager sees each room with type, floor and base price. A blank type shows all rooms. `basePrice` is null until the manager sets the price of the type (H4Y-26). The table shows "—" (H4Y-50). The floor shows as number and description, for example "3 - Quartos". Floors 2 and 3 have the same description, so the number tells them apart.
- `RoomService` holds the statement rules as constants: floors 2 and 3 allow 10 rooms each. `roomFloorLimits()` returns the immutable map used by validation, floor choices and the summary. These rules are not application settings.
- Q-01 is a team decision, not confirmed by the teacher. The 7/10/3 split is a provisional summary target and test scenario. `RoomService` does not enforce room type totals.
- `Floor` is a record: the floors are fixed (RF-03). `Location` is a class: the manager creates, edits and removes zones (Q-32). `FloorController` is the one screen for floors and zones (H4Y-28): it has the zone form, so there is no `LocationController` for zone CRUD. `createLocation`, `editLocation` and `removeLocation` need a manager. `removeLocation` throws `LocationInUseException` when `LocationRepository.delete` returns false (Q-36). `HotelFacade.removeLocation(reservation, location)` is the reservation method and stays.

H4Y-72 (RF-05) adds read-only reservation periods and one half-open overlap rule. `searchRooms` and the search type catalogue require `SEARCH_ROOMS` (receptionist or manager). The actor parameter protects the public entry point. `isAvailable` is an internal business query. `Reservation` implements only the listed read getters, period validation and `overlaps` in this task; its write/lifecycle methods remain planned. `checkedInAt` and `checkedOutAt` remain planned domain fields and are not read by this projection. The new `RoomSearchResult` supplies the AC-5 message without JavaFX.

H4Y-73 (RF-05, RNF-07) adds the screen "Quartos livres" with its own controller, `FreeRoomController`, and `free-rooms.fxml`. One controller class cannot serve two FXML files with different fields. The screen calls `HotelFacade.searchRoomsWithMessage` and shows the message of the service. It has no date rule (RNF-07). `RoomType.totalFor(start, end)` gives the total for the nights. It returns null when the base price is not set. The menu button shows only when `canUse(actor, Permission.SEARCH_ROOMS)` is true. `HotelApp` keeps the chosen room and dates for the reservation screen (H4Y-76).

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
        -onRoomsClicked() void
        -onFreeRoomsClicked() void
        -onPricesClicked() void
        -onStockClicked() void
        -onLogoutClicked() void
        -openLogin() void
    }
    class HotelApp {
        +facade: HotelFacade$
        +currentUser: User$
        +startError: String$
        +chosenRoom: Room$
        +chosenStart: LocalDate$
        +chosenEnd: LocalDate$
        +start(stage: Stage) void
    }
    class ScreenLoader {
        +load(fxmlName: String) Parent
    }
    class RoomController {
        -initialize() void
        -onSaveRoomClicked() void
        -onSaveRoomChangesClicked() void
        -onRemoveRoomClicked() void
        -onClearClicked() void
        +onConfigureRoomItemClicked() void
    }
    class ClientController {
        +onRegisterClientClicked() void
        %% future: not in ClientController yet (H4Y-71)
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
        -initialize() void
        %% future: not in StockController yet (RF-28)
        -onImportXmlClicked() void
        -onSaveItemClicked() void
        -onEditItemClicked() void
        -onClearClicked() void
        %% future: not in StockController yet (RF-29)
        -onRegisterMovementClicked() void
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
    class FreeRoomController {
        -initialize() void
        -onSearchClicked() void
        -onChooseRoomClicked() void
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
        +registerClient(name: String, document: String, actor: User) Client
        %% future: not in HotelFacade yet (H4Y-71)
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
        +searchRooms(start: LocalDate, end: LocalDate, type: RoomType, actor: User) List~Room~
        +searchRoomsWithMessage(start: LocalDate, end: LocalDate, type: RoomType, actor: User) RoomSearchResult
        +listSearchRoomTypes(actor: User) List~RoomType~
        +roomFloorLimits() Map
        +roomTypeTargets() List~RoomTypeTarget~
        +saveRoom(room: Room, actor: User) Room
        +editRoom(room: Room, actor: User) Room
        +removeRoom(id: long, actor: User) void
        +listRooms(actor: User) List~Room~
        +listRoomSummaries(roomTypeName: String, actor: User) List~RoomSummary~
        +listRoomTypes(actor: User) List~String~
        +configureRoomItem(room: Room, item: StockItem, quantity: int) void
        +setBasePrice(type: RoomType, price: BigDecimal, actor: User) void
        +listBasePrices(actor: User) List~RoomType~
        +parsePrice(text: String) BigDecimal
        +setExtraPrice(service: ExtraService, price: BigDecimal) void
        +createLocation(floorNumber: int, name: String, guestCardAccess: boolean, requiresReservation: boolean, actor: User) Location
        +editLocation(id: long, floorNumber: int, name: String, guestCardAccess: boolean, requiresReservation: boolean, actor: User) Location
        +removeLocation(id: long, actor: User) void
        +importStock(file: String, user: User) StockImport
        +createItem(name: String, unit: String, actor: User) StockItem
        +editItem(item: StockItem, actor: User) void
        +listItems(actor: User) List~StockItem~
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
        +ClientService(clientRepository: ClientRepository)
        +register(name: String, document: String, actor: User) Client
        %% future: not in ClientService yet (H4Y-71)
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
        -ROOMS_PER_FLOOR: int$
        -ROOM_FLOOR_LIMITS: Map$
        -ROOM_TYPE_TARGETS: List~RoomTypeTarget~$
        +RoomService(roomRepository: RoomRepository, roomTypeRepository: RoomTypeRepository, reservationRepository: ReservationRepository)
        +isAvailable(room: Room, start: LocalDate, end: LocalDate) boolean
        +searchRooms(start: LocalDate, end: LocalDate, type: RoomType, actor: User) List~Room~
        +searchRoomsWithMessage(start: LocalDate, end: LocalDate, type: RoomType, actor: User) RoomSearchResult
        +listSearchRoomTypes(actor: User) List~RoomType~
        +roomFloorLimits() Map
        +roomTypeTargets() List~RoomTypeTarget~
        +saveRoom(room: Room, actor: User) Room
        +editRoom(room: Room, actor: User) Room
        +removeRoom(id: long, actor: User) void
        +listRooms(actor: User) List~Room~
        +listRoomSummaries(roomTypeName: String, actor: User) List~RoomSummary~
        +listRoomTypes(actor: User) List~String~
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
        +StockService(itemRepository: ItemRepository)
        +importStock(file: String, user: User) StockImport
        +createItem(name: String, unit: String, actor: User) StockItem
        +editItem(item: StockItem, actor: User) void
        +listItems(actor: User) List~StockItem~
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
    class RoomInUseException {
        <<exception>>
    }
    class StockItemNameTakenException {
        <<exception>>
    }
    class StockItemNotFoundException {
        <<exception>>
    }
    class DuplicateItemException {
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
    class RoomRepository {
        <<interface>>
        +findAll() List~Room~
        +findSummaries() List~RoomSummary~
        +save(room: Room, floorLimit: int) Room
        +update(room: Room, floorLimit: int) Room
        +delete(id: long) boolean
    }
    class ClientRepository {
        <<interface>>
        +findByDocument(document: String) Optional~Client~
        +save(client: Client) Client
    }
    class JdbcClientRepository
    class ReservationRepository {
        <<interface>>
        +findByRoom(roomId: long) List~Reservation~
        +findByRooms(roomIds: List~Long~) List~Reservation~
    }
    class JdbcReservationRepository {
        +findByRoom(roomId: long) List~Reservation~
        +findByRooms(roomIds: List~Long~) List~Reservation~
    }
    class Database {
        +NOT_AVAILABLE String$
        +connect() Connection
        +check() void
        +prepare(connection: Connection, sql: String, values: Object...) PreparedStatement$
    }
    class RoomTypeRepository {
        <<interface>>
        +findAll() List~RoomType~
        +updateBasePrice(id: long, price: BigDecimal) boolean
    }
    class ItemRepository {
        <<interface>>
        +findById(id: long) Optional~StockItem~
        +findByName(name: String) Optional~StockItem~
        +findAll() List~StockItem~
        +save(item: StockItem) StockItem
        +update(item: StockItem) boolean
    }
    class JdbcUserRepository
    class JdbcFloorRepository
    class JdbcLocationRepository
    class JdbcRoomRepository {
        +update(room: Room, floorLimit: int) Room
        +delete(id: long) boolean
    }
    class DuplicateRoomException
    class ClientExistsException
    class RoomLimitExceededException
    class JdbcRoomTypeRepository
    class JdbcItemRepository

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
        +MAX_NUMBER: int$
        +Room(id: Long, number: int, floorNumber: int, roomTypeName: String)
        +getId() Long
        +getNumber() int
        +getFloorNumber() int
        +getRoomTypeName() String
        -id: Long
        -number: int
        -floorNumber: int
        -roomTypeName: String
    }
    class RoomTypeTarget {
        <<record>>
        -name: String
        -label: String
        -total: int
        +name() String
        +label() String
        +total() int
    }
    class RoomSearchResult {
        <<record>>
        -rooms: List~Room~
        -message: String
        +rooms() List~Room~
        +message() String
    }
    class RoomSummary {
        <<record>>
        -id: long
        -number: int
        -floorNumber: int
        -floorDescription: String
        -roomTypeName: String
        -basePrice: BigDecimal
    }
    class RoomType {
        -id: Long
        -name: String
        -capacity: int
        -basePrice: BigDecimal
        +totalFor(start: LocalDate, end: LocalDate) BigDecimal
    }
    class RoomItem {
        -quantity: int
    }
    class Reservation {
        -id: Long
        -roomId: long
        +Reservation(id: Long, roomId: long, startDate: LocalDate, endDate: LocalDate, status: ReservationStatus)
        +getId() Long
        +getRoomId() long
        +getStartDate() LocalDate
        +getEndDate() LocalDate
        +getStatus() ReservationStatus
        +validatePeriod(start: LocalDate, end: LocalDate) void$
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
        -displayName: String
        +displayName() String
    }
    class Permission {
        <<enumeration>>
        MANAGE_STAFF
        MANAGE_FLOORS
        MANAGE_ROOMS
        MANAGE_PRICES
        LIST_ROOMS
        MANAGE_STOCK
        MANAGE_CLIENTS
        SEARCH_ROOMS
        -allowedRoles: Set~Role~
        +allows(actor: User) boolean
    }
    class ReservationStatus {
        +fromDatabase(value: String) ReservationStatus$
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
    Room "*" --> "1" Floor : assigned to
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
    FreeRoomController ..> HotelFacade : uses

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
    ClientService ..> ClientRepository : uses
    ClientRepository ..> Client : stores
    JdbcClientRepository ..|> ClientRepository : implements
    JdbcClientRepository ..> Database : uses
    ClientService ..> ClientExistsException : throws
    ClientRepository ..> ClientExistsException : throws
    ClientService ..> AccessDeniedException : throws
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
    RoomService ..> RoomTypeTarget : supplies fixed summary data
    RoomController ..> RoomTypeTarget : displays
    RoomController ..> RoomSummary : displays
    RoomController ..> Floor : displays
    RoomService ..> RoomSearchResult : returns
    RoomSearchResult ..> Room : contains
    FreeRoomController ..> RoomSearchResult : displays
    FreeRoomController ..> RoomType : displays
    RoomService ..> ReservationRepository : reads booking periods
    ReservationRepository ..> Reservation : reads
    JdbcReservationRepository ..|> ReservationRepository : implements
    JdbcReservationRepository ..> Database : uses
    RoomService ..> RoomRepository : uses
    RoomRepository ..> Room : stores
    JdbcRoomRepository ..|> RoomRepository : implements
    JdbcRoomRepository ..> Database : uses
    JdbcRoomTypeRepository ..|> RoomTypeRepository : implements
    JdbcRoomTypeRepository ..> Database : uses
    JdbcItemRepository ..|> ItemRepository : implements
    JdbcItemRepository ..> Database : uses
    ItemRepository ..> StockItem : stores
    StockService ..> ItemRepository : uses
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
    RoomService ..> RoomInUseException : throws
    StockService ..> AccessDeniedException : throws
    StockService ..> StockItemNameTakenException : throws
    StockService ..> StockItemNotFoundException : throws
    StockService ..> DuplicateItemException : catches
    RoomRepository ..> DuplicateRoomException : throws
    RoomRepository ..> RoomLimitExceededException : throws
    JdbcItemRepository ..> DuplicateItemException : throws

    HotelFacade ..> Permission : uses
    StaffService ..> Permission : uses
    RoomService ..> Permission : uses
    LocationService ..> Permission : uses
    StockService ..> Permission : uses
    ClientService ..> Permission : uses
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
    Main ..> JdbcReservationRepository : creates
    Main ..> JdbcRoomRepository : creates
    Main ..> AuthService : creates
    Main ..> StaffService : creates
    Main ..> JdbcRoomTypeRepository : creates
    Main ..> JdbcItemRepository : creates
    Main ..> LocationService : creates
    Main ..> RoomService : creates
    Main ..> StockService : creates
    Main ..> JdbcClientRepository : creates
    Main ..> ClientService : creates
    Main ..> HotelFacade : creates
    HotelApp ..> ScreenLoader : uses
    LoginController ..> ScreenLoader : opens main window
    MainController ..> ScreenLoader : opens login on logout or without session
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
    note for StockService "importStock accepts all items from a valid XML file.<br/>Create missing catalogue items, add stock and ENTRY movements.<br/>Save or undo items, import, movements and balances together (RF-28, Q-10, Q-38).<br/>createItem, editItem and listItems need MANAGE_STOCK (manager only). A new item starts with stock 0. editItem keeps the stock (RF-28, RF-29, Q-38)."
    note for RoomService "MANAGE_ROOMS allows a manager to save a room.<br/>Constants enforce floors 2 and 3 with 10 rooms each, as stated in the course statement.<br/>Only the repository checks the room count, inside its save transaction.<br/>Q-01 targets 7/10/3 appear in the summary but are not enforced."
```

Created by: antonioprmartinho. Updated by: BorjaTiago. AI use: Claude Code, claude-sonnet-5-5, 2026-10-16; doc-updater, Claude Code (claude-sonnet-5-5), 2026-10-17; doc-updater, Claude Code (claude-haiku-5-5), 2026-10-17; doc-updater, Claude Code (claude-haiku-5-5), 2026-10-19; PR-20 import decision review by sequence-diagram-editor, OpenAI Codex (GPT-6), 2026-10-22; merge-conflict resolution by sequence-diagram-editor, OpenAI Codex (GPT-6), 2026-10-22; doc-updater, Claude Code (claude-opus-5-5), 2026-10-23; H4Y-27 staff account update, Claude Code (claude-opus-5-5), 2026-10-23; H4Y-28 floors view by doc-updater, Claude Code (claude-haiku-5-5), 2026-10-26; first-manager removal by doc-updater, Claude Code (claude-haiku-5-5), 2026-10-26; Floor record note by doc-updater, Claude Code (claude-haiku-5-5), 2026-10-26; H4Y-28 zone merge by doc-updater, Claude Code (claude-sonnet-5-5), 2026-10-26; H4Y-60 role permission rule, Claude Code (claude-opus-5-5), 2026-10-27; PR #33 review change, Claude Code (claude-opus-5-5), 2026-10-27; merge with main by Claude Code (claude-opus-5-5), 2026-10-27; H4Y-28 floors permission by doc-updater, Claude Code (claude-haiku-5-5), 2026-10-27. H4Y-25 merge with latest main, OpenAI Codex (GPT-6), 2026-10-28; H4Y-25 review corrections, OpenAI Codex (gpt-6.1-sol), 2026-10-29; statement room constants review, OpenAI Codex (gpt-6.1-sol), 2026-10-29; room read permission and JDBC review, OpenAI Codex (gpt-6.1-sol), 2026-10-30; H4Y-28 floors permission by doc-updater, Claude Code (claude-haiku-5-5), 2026-10-27. H4Y-26 base price by doc-updater, Claude Code (claude-haiku-5-5), 2026-10-30. H4Y-26 review fix by pr-fixer, Claude Code (claude-opus-5-5), 2026-10-30; H4Y-25 room type integration, OpenAI Codex (gpt-6.1-sol), 2026-10-30; H4Y-25 entity and room type integration review, OpenAI Codex (gpt-6.1-sol), 2026-10-30; prepare signature fix by ecc:doc-updater, Claude Code (claude-haiku-5-5), 2026-10-30; review fixes by ecc:doc-updater, Claude Code (claude-haiku-5-5), 2026-10-30; H4Y-50 room list by doc-updater, Claude Code (claude-opus-5-5), 2026-10-29; H4Y-50 merge with main after H4Y-25 and H4Y-26 (room list) by Claude Code (claude-opus-5-5), 2026-10-30; Database.prepare with Object values by Claude Code (claude-opus-5-5), 2026-10-30; H4Y-61 role display name by main session, Claude Code (claude-opus-5-5), 2026-10-27; H4Y-61 main window header and no-session check by main session, Claude Code (claude-opus-5-5), 2026-10-27; H4Y-61 review fixes by main session, Claude Code (claude-opus-5-5), 2026-10-30; H4Y-50 merged rooms screen by doc-updater, Claude Code (claude-haiku-5-5), 2026-10-30; H4Y-50 room removal and clear by doc-updater, Claude Code (claude-haiku-5-5), 2026-10-30; doc-updater, Claude Code (claude-haiku-5-5), 2026-11-03; doc-updater, Claude Code (claude-haiku-5-5), 2026-11-03; doc-updater, Claude Code (claude-haiku-5-5), 2026-11-03; room-availability-implementation, OpenAI Codex (gpt-6.1-sol), 2026-11-03; merge with main after H4Y-72 by Claude Code (claude-opus-5-5), 2026-11-04; H4Y-70 merge with main after H4Y-68 and H4Y-73 review by Claude Code (claude-opus-5-5), 2026-11-04; H4Y-70 review fixes by ecc:doc-updater, Claude Code (claude-haiku-5-5), 2026-11-04; H4Y-73 merge with main after H4Y-68 by Claude Code (claude-opus-5-5), 2026-11-04; H4Y-73 v9 reservations and one-query search by ecc:doc-updater, Claude Code (claude-haiku-5-5), 2026-11-04; doc-updater, Claude Code (claude-haiku-5-5), 2026-11-04. Reviewed by: pending.
