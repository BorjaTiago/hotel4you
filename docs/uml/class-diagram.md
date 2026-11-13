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
- `StockController` is one screen for the stock catalogue (H4Y-51). The manager selects a row, and the form fills. `onClearClicked` clears the form, the table selection and the message. `onImportStockFileClicked` (XML or JSON file, H4Y-84) and `onRegisterMovementClicked` are future methods. They are not in the code yet.
- `ItemRepository` is in the data layer. `JdbcItemRepository` is the SQL Server version. `InMemoryItemRepository` (in `src/test`) is a test helper, so it is not in this diagram.
- `$` marks a static member.
- `ClientService` checks `Permission.MANAGE_CLIENTS` first in every method (receptionist and manager; clients do not log in, Q-13, Q-54). `register` and `editClient` trim the texts and check each field: a missing or wrong field throws `IllegalArgumentException` and the message names the field. Required: name (at most 100), `documentType`, document (at most 20), `birthDate` (not in the future), phone (digits, spaces and one leading '+', at most 20) and nationality (ISO code from `Locale.getISOCountries()`). Optional: email (at most 100) and tax number (9 digits). A client with the same document type and number (not case sensitive) throws `ClientExistsException`. `JdbcClientRepository.save` and `update` also throw it when the database finds the same document (error 2601 or 2627). `editClient` and `removeClient` throw `ClientNotFoundException` for an unknown client. `JdbcClientRepository.delete` throws `ClientInUseException` on SQL error 547, so a client with reservations is not removed (Q-36). `searchClients` takes a `ClientSearchField` (`DOCUMENT`, `NAME` or `PHONE`) and the text. A null field throws `IllegalArgumentException` ("Escolha o campo de pesquisa."). A blank text lists all clients (H4Y-52).
- `ClientController` is one screen like the rooms screen: a search box and the client table, and the form beside it. A click on a row fills the form. "Remover" asks for a confirmation. The field that the message names turns red (`field-error`). The search has a `ComboBox<ClientSearchField>` ("Documento", "Nome", "Telefone"; "Documento" by default). "Continuar com este cliente" gives the selected client to `ReservationController` (`setOnClientChosen`). The screen is tab "1. Cliente" of "Reservar quarto", not a menu item.
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

H4Y-73 (RF-05, RNF-07) adds the screen "Quartos livres" with its own controller, `FreeRoomController`, and `free-rooms.fxml`. One controller class cannot serve two FXML files with different fields. The screen calls `HotelFacade.searchRoomsWithMessage` and shows the message of the service. It has no date rule (RNF-07). `RoomType.totalFor(start, end)` gives the total for the nights. It returns null when the base price is not set. Since H4Y-76 the screen is tab "2. Quarto" of "Reservar quarto" and has no menu button. The chosen room and dates go to `ReservationController` by callback, not through `HotelApp`.

H4Y-56 (RF-11, RF-05, RNF-07) adds the reservation create. `ReservationService` checks `Permission.MANAGE_RESERVATIONS` first in every method. The receptionist and the manager have it (the manager can use every function, H4Y-23 AC-1). `create` refuses missing data with one `IllegalArgumentException` that names all missing fields (client, room, check-in date, check-out date, AC-3). After the period check, it refuses an arrival date before today with "A data de entrada não pode ser antes de hoje." (Q-55, AC-5). An arrival date of today is allowed. Then it uses `Reservation.validatePeriod` and `RoomService.isAvailable`, the same overlap rule as the free-room search. A busy room throws `RoomNotAvailableException` ("O quarto já está reservado nessas datas.", AC-2). `JdbcReservationRepository.save` runs the overlap check and the insert in one SQL statement (`INSERT ... SELECT ... WHERE NOT EXISTS` with `UPDLOCK, HOLDLOCK`). The SQL repeats the rule of `Reservation.overlaps` (half-open nights, cancelled bookings do not count), because only the database can stop two saves at the same moment. When the insert adds no row, `save` throws `RoomNotAvailableException`. So two receptionists cannot reserve the same room for the same nights. `RoomNotAvailableException` is in the data package, next to `DuplicateRoomException`. The new reservation has the state `RESERVED` and keeps the client and the user who created it (`created_by_user_id`, AC-1). `quote` returns a `ReservationQuote` with the base price per night of the room type (null if not set) and the number of nights (AC-4). `listClientReservations` returns the `ClientReservation` rows of one client, with the cancelled ones (history). The actor is a parameter of `create`, `quote` and `listClientReservations`, as in the other services. `ReservationController` is the screen "Reservar quarto" (`reservations.fxml`) with three tabs. Tab "1. Cliente" includes `client.fxml` (`ClientController`). Tab "2. Quarto" includes `free-rooms.fxml` (`FreeRoomController`). Tab "3. Confirmar" shows the client, room, dates, price summary and total, the "Confirmar reserva" button, and the client reservations table. Tabs 2 and 3 stay disabled until a client and a room are chosen. The list button "Reservas" in the menu shows only when `canUse(actor, Permission.MANAGE_RESERVATIONS)` is true (H4Y-81). `ReservationController` gets the two included controllers with `fx:include` and gives them callbacks (`onClientChosen`, `onRoomChosen`). If the quote throws in `onRoomChosen`, the room tab shows the message. The static `summaryText(roomNumber, quote)` builds the price summary that the screen shows before the confirmation (AC-4). After the save, the screen shows "Reserva criada para o quarto N." and forgets the room choice. It does not read the price again.

H4Y-81 (RF-11, RF-13, RF-14) makes the screen "Reservas" (`reservations-list.fxml`) the main screen. The list button "Reservas" in the menu shows for users with `MANAGE_RESERVATIONS`. For these users, the screen opens by default after login. It replaces the H4Y-82 check-in and check-out screen. `ReservationsController` lists the `ReservationSummary` rows of `HotelFacade.listActiveReservations`. The static `filter` searches by client name or document and applies the quick filters. The static `canCheckIn` and `canCheckOut` decide which button is on. `canCheckIn` also checks the stay period (Q-61). The "+ Reservar quarto" button opens `reservations.fxml` in the same area. It is the only way to open that screen. The return to the list after a save comes with PR #53 (H4Y-81 AC-9).

H4Y-55 (RF-12, Q-05) adds the cancel. The receptionist and the manager cancel a RESERVED reservation with the "Cancelar" button on the screen "Reservas". The static `canCancel` turns the button on only for a RESERVED row. The screen asks first with `cancelQuestion` (room and dates). `ReservationService.cancel(reservationId, actor)` checks `MANAGE_RESERVATIONS`, reads the reservation and calls `Reservation.checkCanCancel()`. It refuses a cancelled reservation ("A reserva já está cancelada."), a reservation with a check-in ("A reserva já tem check-in.") and a reservation with a check-out ("A reserva já tem check-out."). `ReservationRepository.cancel(id)` runs `UPDATE ... SET status = 'cancelled' WHERE id = ? AND status = 'reserved'`. The row stays in the database, so the client history shows it. A cancelled reservation does not block the room (`Reservation.overlaps`). An update that changes no row gives "A reserva mudou de estado. Atualize a lista.". The planned `Reservation.cancel()` is replaced by `checkCanCancel()`, as for the check-in and the check-out. The cancel button is on the screen "Reservas", so `onCancelReservationClicked` moves from `ReservationController` to `ReservationsController`.

H4Y-57 (RF-22, Q-05, Q-36) adds the permanent delete of a reservation before the check-in. `Permission.DELETE_RESERVATIONS` allows only the manager. `ReservationService.delete(reservationId, actor)` checks it first and throws `AccessDeniedException` ("Só o gestor pode apagar reservas.") for other users. An unknown id throws `ReservationNotFoundException` ("A reserva não existe.", AC-3). `JdbcReservationRepository.delete` runs `DELETE FROM reservations WHERE id = ? AND status = 'reserved'`, so a check-in or a cancel at the same moment stops the delete. Only a RESERVED reservation is deleted (Q-05). The service reads the state first. For a cancelled, checked-in or checked-out reservation it throws the same message and does not call the repository, so a cancelled reservation stays in the history. When no row is deleted, the service throws "A reserva mudou de estado. Atualize a lista.". The deleted row is gone from the active list and the client history (AC-1), and the room is free again for the same nights (AC-2). On the "Reservas" screen, `ReservationsController` shows the "Apagar" button (`action-button, danger-button`) only when `canUse(actor, Permission.DELETE_RESERVATIONS)` is true. The static `canDelete` turns it on only for a `RESERVED` row (AC-4). The static `deleteQuestion` gives the confirmation text. The refusal for other roles (H4Y-58) and the refusal after the check-in with its own message (story block-delete-after-check-in) add guards to the same method. The ids replace the old planned `delete(reservation, user)` signature, as in `checkIn` and `checkOut`.

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
        -onReservationsListClicked() void
        -onPricesClicked() void
        -onStockClicked() void
        -onLogoutClicked() void
        -openLogin() void
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
        -initialize() void
        -onSaveRoomClicked() void
        -onSaveRoomChangesClicked() void
        -onRemoveRoomClicked() void
        -onClearClicked() void
        +onConfigureRoomItemClicked() void
    }
    class ClientController {
        -initialize() void
        -onSearchClientClicked() void
        -onRegisterClientClicked() void
        -onSaveClientChangesClicked() void
        -onRemoveClientClicked() void
        -onContinueClicked() void
        -onClearClicked() void
        +setOnClientChosen(onClientChosen: Consumer~Client~) void
    }
    class ReservationController {
        +ReservationController(facade: HotelFacade)
        -initialize() void
        -onClientChosen(client: Client) void
        -onRoomChosen(choice: Choice) void
        -onCreateReservationClicked() void
        ~summaryText(roomNumber: int, quote: ReservationQuote) String$
        +onDeleteReservationClicked() void
        +onCancelReservationClicked() void
        +onAddExtraClicked() void
        +onRemoveExtraClicked() void
        +onAddLocationClicked() void
        +onRemoveLocationClicked() void
    }
    class ReservationsController {
        +ReservationsController(facade: HotelFacade)
        ~canCheckIn(row: ReservationSummary, today: LocalDate) boolean$
        ~canCheckOut(row: ReservationSummary) boolean$
        ~canCancel(row: ReservationSummary) boolean$
        ~cancelQuestion(row: ReservationSummary) String$
        ~canDelete(row: ReservationSummary) boolean$
        ~deleteQuestion(row: ReservationSummary) String$
        ~statusText(status: ReservationStatus) String$
        ~filter(rows: List~ReservationSummary~, filter: QuickFilter, text: String, today: LocalDate) List~ReservationSummary~$
        -initialize() void
        -onNewReservationClicked() void
        -onCheckInClicked() void
        -onCheckOutClicked() void
        -onCancelReservationClicked() void
        -onDeleteReservationClicked() void
        -loadReservations() boolean
        -applyFilter() void
        -currentFilter() QuickFilter
        -updateButtons(chosen: ReservationSummary) void
        -matchesFilter(row: ReservationSummary, filter: QuickFilter, today: LocalDate) boolean$
        -showMessage(message: String, success: boolean) void
    }
    class QuickFilter {
        <<enumeration>>
        ALL_ACTIVE
        ARRIVING_TODAY
        CHECKED_IN
    }
    class DateCell {
        -updateItem(date: LocalDate, empty: boolean) void
    }
    class StaffController {
        -initialize() void
        -onCreateStaffClicked() void
        -onSaveStaffChangesClicked() void
        -onClearClicked() void
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
        %% future: not in StockController yet (RF-28, XML or JSON)
        -onImportStockFileClicked() void
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
        +setOnRoomChosen(onRoomChosen: Consumer~Choice~) void
    }
    class Choice {
        <<record>>
        +room: Room
        +start: LocalDate
        +end: LocalDate
    }
    note for Choice "Nested record FreeRoomController.Choice"

    %% ==========================================
    %% LAYER 2: BUSINESS - FACADE
    %% ==========================================
    class HotelFacade {
        +login(username: String, password: String) User
        +createReservation(client: Client, room: Room, start: LocalDate, end: LocalDate, actor: User) Reservation
        +quoteReservation(room: Room, start: LocalDate, end: LocalDate, actor: User) ReservationQuote
        +listClientReservations(client: Client, actor: User) List~ClientReservation~
        +cancelReservation(reservationId: long, actor: User) void
        +deleteReservation(reservationId: long, actor: User) void
        +addExtra(reservation: Reservation, service: ExtraService, quantity: int) ReservationExtra
        +removeExtra(reservation: Reservation, extra: ReservationExtra) void
        +addLocation(reservation: Reservation, location: Location) void
        +removeLocation(reservation: Reservation, location: Location) void
        +registerClient(client: Client, actor: User) Client
        +listClients(actor: User) List~Client~
        +searchClients(field: ClientSearchField, text: String, actor: User) List~Client~
        +editClient(client: Client, actor: User) void
        +removeClient(id: long, actor: User) void
        +checkIn(reservationId: long, actor: User) void
        +checkOut(reservationId: long, actor: User) void
        +listActiveReservations(actor: User) List~ReservationSummary~
        +issueCard(reservation: Reservation) MagneticCard
        +issueMasterCard(manager: Manager) MagneticCard
        +blockMasterCard(card: MagneticCard) void
        +getPlacesByCard(card: MagneticCard) List~CardAccessRecord~
        +getPassesByRoom(room: Room) List~CardAccessRecord~
        +createStaff(name: String, username: String, password: String, role: Role, actor: User) void
        +canUse(actor: User, permission: Permission) boolean
        +editStaff(id: long, name: String, username: String, role: Role, actor: User) void
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
        +register(client: Client, actor: User) Client
        +listClients(actor: User) List~Client~
        +searchClients(field: ClientSearchField, text: String, actor: User) List~Client~
        +editClient(client: Client, actor: User) void
        +removeClient(id: long, actor: User) void
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
        +ReservationService(reservationRepository: ReservationRepository, roomService: RoomService, roomTypeRepository: RoomTypeRepository)
        +create(client: Client, room: Room, start: LocalDate, end: LocalDate, actor: User) Reservation
        +quote(room: Room, start: LocalDate, end: LocalDate, actor: User) ReservationQuote
        +listClientReservations(client: Client, actor: User) List~ClientReservation~
        +cancel(reservationId: long, actor: User) void
        +delete(reservationId: long, actor: User) void
        +addExtra(reservation: Reservation, service: ExtraService, quantity: int) ReservationExtra
        +removeExtra(reservation: Reservation, extra: ReservationExtra) void
        +addLocation(reservation: Reservation, location: Location) void
        +removeLocation(reservation: Reservation, location: Location) void
        +checkIn(reservationId: long, actor: User) void
        +checkOut(reservationId: long, actor: User) void
        +listActive(actor: User) List~ReservationSummary~
    }
    class StaffService {
        +createStaff(name: String, username: String, password: String, role: Role, actor: User) void
        +editStaff(id: long, name: String, username: String, role: Role, actor: User) void
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
    class StaffNotFoundException {
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
    class ClientNotFoundException {
        <<exception>>
    }
    class ReservationNotFoundException {
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
        +findById(id: long) Optional~User~
        +existsByUsername(username: String) boolean
        +save(user: User) User
        +update(user: User) boolean
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
        +findById(id: long) Optional~Client~
        +findByDocument(documentType: DocumentType, document: String) Optional~Client~
        +findAll() List~Client~
        +search(field: ClientSearchField, text: String) List~Client~
        +save(client: Client) Client
        +update(client: Client) boolean
        +delete(id: long) boolean
    }
    class JdbcClientRepository
    class ReservationRepository {
        <<interface>>
        +findByRoom(roomId: long) List~Reservation~
        +findByRooms(roomIds: List~Long~) List~Reservation~
        +save(reservation: Reservation) Reservation
        +findByClient(clientId: long) List~ClientReservation~
        +findById(id: long) Optional~Reservation~
        +findActive() List~ReservationSummary~
        +checkIn(id: long) boolean
        +checkOut(id: long) boolean
        +cancel(id: long) boolean
        +delete(id: long) boolean
    }
    class JdbcReservationRepository {
        +findByRoom(roomId: long) List~Reservation~
        +findByRooms(roomIds: List~Long~) List~Reservation~
        +save(reservation: Reservation) Reservation
        +findByClient(clientId: long) List~ClientReservation~
        +findById(id: long) Optional~Reservation~
        +findActive() List~ReservationSummary~
        +checkIn(id: long) boolean
        +checkOut(id: long) boolean
        +cancel(id: long) boolean
        +delete(id: long) boolean
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
    class RoomNotAvailableException
    class ClientExistsException
    class ClientInUseException
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
        -documentType: DocumentType
        -document: String
        -birthDate: LocalDate
        -phone: String
        -nationality: String
        -email: String
        -taxNumber: String
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
    class ClientReservation {
        <<record>>
        -id: long
        -roomNumber: int
        -startDate: LocalDate
        -endDate: LocalDate
        -status: ReservationStatus
    }
    class ReservationSummary {
        <<record>>
        -id: long
        -clientName: String
        -document: String
        -roomNumber: int
        -startDate: LocalDate
        -endDate: LocalDate
        -status: ReservationStatus
    }
    class ReservationQuote {
        <<record>>
        -pricePerNight: BigDecimal
        -nights: long
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
        -clientId: Long
        -roomId: long
        -createdByUserId: Long
        +Reservation(id: Long, roomId: long, startDate: LocalDate, endDate: LocalDate, status: ReservationStatus)
        +Reservation(id: Long, clientId: Long, roomId: long, startDate: LocalDate, endDate: LocalDate, status: ReservationStatus, createdByUserId: Long)
        +getId() Long
        +getClientId() Long
        +getRoomId() long
        +getCreatedByUserId() Long
        +getStartDate() LocalDate
        +getEndDate() LocalDate
        +getStatus() ReservationStatus
        +validatePeriod(start: LocalDate, end: LocalDate) void$
        -startDate: LocalDate
        -endDate: LocalDate
        -status: ReservationStatus
        -checkedInAt: LocalDateTime
        -checkedOutAt: LocalDateTime
        +checkCanCheckIn(today: LocalDate) void
        +checkCanCheckOut() void
        +checkCanCancel() void
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
        -fileFormat: StockFileFormat
    }
    class StockFileFormat {
        <<enumeration>>
        XML
        JSON
        +fromFileName(fileName: String) StockFileFormat$
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
        MANAGE_RESERVATIONS
        DELETE_RESERVATIONS
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
    class DocumentType {
        +displayName() String
        +fromDatabase(value: String) DocumentType$
        <<enumeration>>
        CC
        PASSPORT
        OTHER
    }
    class ClientSearchField {
        <<enumeration>>
        DOCUMENT
        NAME
        PHONE
        -displayName: String
        +displayName() String
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
    %% future: supplier file readers (RF-28, H4Y-84, Q-59)
    class StockFileReader {
        <<interface>>
        +format() StockFileFormat
        +read(content: String) List~StockFileLine~
    }
    class XmlStockFileReader {
        +format() StockFileFormat
        +read(content: String) List~StockFileLine~
    }
    class JsonStockFileReader {
        +format() StockFileFormat
        +read(content: String) List~StockFileLine~
    }
    class StockFileLine {
        <<record>>
        +supplierName: String
        +itemName: String
        +unit: String
        +quantity: int
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
    StockImport "*" --> "1" StockFileFormat : read from
    XmlStockFileReader ..|> StockFileReader : implements
    JsonStockFileReader ..|> StockFileReader : implements
    StockFileReader ..> StockFileLine : reads
    StockService ..> StockFileReader : picks by format

    %% ==========================================
    %% DEPENDENCIES (Controllers -> Facade)
    %% ==========================================
    LoginController ..> HotelFacade : uses
    MainController ..> HotelFacade : uses
    RoomController ..> HotelFacade : uses
    ClientController ..> HotelFacade : uses
    ReservationController ..> HotelFacade : uses
    ReservationsController ..> HotelFacade : uses
    ReservationsController ..> ScreenLoader : opens reservations.fxml
    ReservationsController ..> QuickFilter : uses
    ReservationsController ..> DateCell : uses
    StaffController ..> HotelFacade : uses
    CardController ..> HotelFacade : uses
    StockController ..> HotelFacade : uses
    FeedbackController ..> HotelFacade : uses
    FloorController ..> HotelFacade : uses
    PriceController ..> HotelFacade : uses
    FreeRoomController ..> HotelFacade : uses
    FreeRoomController ..> Choice : creates
    ClientController ..> ClientSearchField : uses
    ReservationController "1" *-- "1" ClientController : includes clientStep
    ReservationController "1" *-- "1" FreeRoomController : includes roomStep

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
    ClientRepository ..> ClientSearchField : searches by
    ClientService ..> ClientSearchField : uses
    JdbcClientRepository ..|> ClientRepository : implements
    JdbcClientRepository ..> Database : uses
    ClientService ..> ClientExistsException : throws
    ClientRepository ..> ClientExistsException : throws
    ClientService ..> AccessDeniedException : throws
    ClientService ..> ClientNotFoundException : throws
    ClientRepository ..> ClientInUseException : throws
    Client --> DocumentType : has
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
    ReservationRepository ..> Reservation : saves and reads
    ReservationRepository ..> ClientReservation : reads
    ReservationRepository ..> ReservationSummary : reads
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
    StaffService ..> StaffNotFoundException : throws
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
    ReservationRepository ..> RoomNotAvailableException : throws
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
    ReservationService ..> ReservationRepository : saves and reads
    ReservationService ..> RoomService : checks free nights
    ReservationService ..> RoomTypeRepository : reads base price
    ReservationService ..> Permission : uses
    ReservationService ..> AccessDeniedException : throws
    ReservationService ..> ReservationNotFoundException : throws
    ReservationService ..> RoomNotAvailableException : throws
    ReservationService ..> ReservationQuote : returns
    ReservationService ..> ClientReservation : returns
    ReservationService ..> ReservationSummary : returns
    ReservationController ..> ClientReservation : displays
    ReservationsController ..> ReservationSummary : displays
    ReservationController ..> ReservationQuote : displays
    Main ..> ReservationService : creates
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
    note for StockService "importStock accepts all items from a valid XML or JSON file.<br/>The file name gives StockFileFormat. The reader of that format gives the same StockFileLine rows (H4Y-84, Q-59).<br/>Create missing catalogue items, add stock and ENTRY movements.<br/>Save or undo items, import, movements and balances together (RF-28, Q-10, Q-38).<br/>createItem, editItem and listItems need MANAGE_STOCK (manager only). A new item starts with stock 0. editItem keeps the stock (RF-28, RF-29, Q-38)."
    note for RoomService "MANAGE_ROOMS allows a manager to save a room.<br/>Constants enforce floors 2 and 3 with 10 rooms each, as stated in the course statement.<br/>Only the repository checks the room count, inside its save transaction.<br/>Q-01 targets 7/10/3 appear in the summary but are not enforced."
```

Created by: antonioprmartinho. Updated by: BorjaTiago. AI use: Claude Code, claude-sonnet-5-5, 2026-10-16; doc-updater, Claude Code (claude-sonnet-5-5), 2026-10-17; doc-updater, Claude Code (claude-haiku-5-5), 2026-10-17; doc-updater, Claude Code (claude-haiku-5-5), 2026-10-19; PR-20 import decision review by sequence-diagram-editor, OpenAI Codex (GPT-6), 2026-10-22; merge-conflict resolution by sequence-diagram-editor, OpenAI Codex (GPT-6), 2026-10-22; doc-updater, Claude Code (claude-opus-5-5), 2026-10-23; H4Y-27 staff account update, Claude Code (claude-opus-5-5), 2026-10-23; H4Y-28 floors view by doc-updater, Claude Code (claude-haiku-5-5), 2026-10-26; first-manager removal by doc-updater, Claude Code (claude-haiku-5-5), 2026-10-26; Floor record note by doc-updater, Claude Code (claude-haiku-5-5), 2026-10-26; H4Y-28 zone merge by doc-updater, Claude Code (claude-sonnet-5-5), 2026-10-26; H4Y-60 role permission rule, Claude Code (claude-opus-5-5), 2026-10-27; PR #33 review change, Claude Code (claude-opus-5-5), 2026-10-27; merge with main by Claude Code (claude-opus-5-5), 2026-10-27; H4Y-28 floors permission by doc-updater, Claude Code (claude-haiku-5-5), 2026-10-27; H4Y-25 merge with latest main, OpenAI Codex (GPT-6), 2026-10-28; H4Y-25 review corrections, OpenAI Codex (gpt-6.1-sol), 2026-10-29; statement room constants review, OpenAI Codex (gpt-6.1-sol), 2026-10-29; room read permission and JDBC review, OpenAI Codex (gpt-6.1-sol), 2026-10-30; H4Y-28 floors permission by doc-updater, Claude Code (claude-haiku-5-5), 2026-10-27; H4Y-26 base price by doc-updater, Claude Code (claude-haiku-5-5), 2026-10-30; H4Y-26 review fix by pr-fixer, Claude Code (claude-opus-5-5), 2026-10-30; H4Y-25 room type integration, OpenAI Codex (gpt-6.1-sol), 2026-10-30; H4Y-25 entity and room type integration review, OpenAI Codex (gpt-6.1-sol), 2026-10-30; prepare signature fix by ecc:doc-updater, Claude Code (claude-haiku-5-5), 2026-10-30; review fixes by ecc:doc-updater, Claude Code (claude-haiku-5-5), 2026-10-30; H4Y-50 room list by doc-updater, Claude Code (claude-opus-5-5), 2026-10-29; H4Y-50 merge with main after H4Y-25 and H4Y-26 (room list) by Claude Code (claude-opus-5-5), 2026-10-30; Database.prepare with Object values by Claude Code (claude-opus-5-5), 2026-10-30; H4Y-61 role display name by main session, Claude Code (claude-opus-5-5), 2026-10-27; H4Y-61 main window header and no-session check by main session, Claude Code (claude-opus-5-5), 2026-10-27; H4Y-61 review fixes by main session, Claude Code (claude-opus-5-5), 2026-10-30; H4Y-50 merged rooms screen by doc-updater, Claude Code (claude-haiku-5-5), 2026-10-30; H4Y-50 room removal and clear by doc-updater, Claude Code (claude-haiku-5-5), 2026-10-30; doc-updater, Claude Code (claude-haiku-5-5), 2026-11-03; doc-updater, Claude Code (claude-haiku-5-5), 2026-11-03; doc-updater, Claude Code (claude-haiku-5-5), 2026-11-03; room-availability-implementation, OpenAI Codex (gpt-6.1-sol), 2026-11-03; merge with main after H4Y-72 by Claude Code (claude-opus-5-5), 2026-11-04; H4Y-70 merge with main after H4Y-68 and H4Y-73 review by Claude Code (claude-opus-5-5), 2026-11-04; H4Y-70 review fixes by ecc:doc-updater, Claude Code (claude-haiku-5-5), 2026-11-04; H4Y-52 client CRUD (Q-54) by Claude Code (claude-opus-5-5), 2026-11-04; H4Y-73 merge with main after H4Y-68 by Claude Code (claude-opus-5-5), 2026-11-04; H4Y-73 v9 reservations and one-query search by ecc:doc-updater, Claude Code (claude-haiku-5-5), 2026-11-04; doc-updater, Claude Code (claude-haiku-5-5), 2026-11-04; H4Y-56 merge with H4Y-73 by Claude Code (claude-opus-5-5), 2026-11-06; H4Y-56 create reservation by Claude Code (claude-opus-5-5), 2026-11-06; PR #51 review fixes by Claude Code (claude-opus-5-5), 2026-11-06; H4Y-73 merge with main after H4Y-52 by Claude Code (claude-opus-5-5), 2026-11-06; H4Y-56 merge with H4Y-73 after H4Y-52 by Claude Code (claude-opus-5-5), 2026-11-06; PR #51 review round 2 by Claude Code (claude-opus-5-5), 2026-11-06; H4Y-82 check-in and check-out by Claude Code (claude-opus-5-5), 2026-11-08; H4Y-82 PR #55 review fixes by Claude Code (claude-opus-5-5), 2026-11-08; doc-updater, Claude Code (claude-haiku-5-5), 2026-11-09; doc-updater, Claude Code (claude-haiku-5-5), 2026-11-09 (sync with 6faf8f8); rename by general-purpose agent, Claude Code (claude-haiku-5-5), 2026-11-09; doc-updater, Claude Code (claude-haiku-5-5), 2026-11-09.; doc-updater, Claude Code (claude-haiku-5-5), 2026-11-06; merge with main after H4Y-82 by Claude Code (claude-opus-5-5), 2026-11-09; screen rename to "Reservar quarto" by Claude Code (claude-opus-5-5), 2026-11-09; H4Y-85 edit staff account by Claude Code (claude-opus-5-5), 2026-11-09; H4Y-85 rebase on main after H4Y-82 and review fixes by Claude Code (claude-opus-5-5), 2026-11-10; H4Y-85 rebase on main after H4Y-80 and H4Y-81 by Claude Code (claude-opus-5-5), 2026-11-11; H4Y-57 delete reservation by Claude Code (claude-opus-5-5), 2026-11-12; H4Y-84 JSON stock import docs by Claude Code (claude-opus-5-5), 2026-11-12; Created by: antonioprmartinho. Updated by: BorjaTiago. AI use: Claude Code, claude-sonnet-5-5, 2026-10-16; doc-updater, Claude Code (claude-sonnet-5-5), 2026-11-13; pr-fixer merge by Claude Code (claude-sonnet-5-5), 2026-11-13. Reviewed by: pending.
