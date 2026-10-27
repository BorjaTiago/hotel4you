# UML - Classes

One table per layer. Every class of [class-diagram.md](class-diagram.md) appears once. Guide: [guide.md](guide.md). Requirements: RF and RNF from [../requirements/requirements.md](../requirements/requirements.md).

Notes:

- Constructors and getters are not shown.
- Unit tests use `InMemoryUserRepository` and `InMemoryRoomRepository` (in `src/test`). They are test helpers, so they are not in this diagram.

## Interface (JavaFX controllers)

| Class | Layer | Main attributes | Main methods | Requirement |
|-------|-------|-----------------|--------------|-------------|
| LoginController | Interface | - | onLoginClicked() (private) | RF-01 |
| MainController | Interface | - | initialize(), onStaffClicked(), onRoomsClicked(), onLogoutClicked() (private) | RNF-06, RNF-07 |
| HotelApp | Interface | facade (static), currentUser (static), startError (static) | start() | RNF-06, RNF-07 |
| ScreenLoader | Interface | - | load() | RNF-06, RNF-07 |
| RoomController | Interface | - | initialize(), onSearchClicked(), onSaveRoomClicked(), onConfigureRoomItemClicked(), onSavePriceClicked() (all private) | RF-04, RF-05, RF-06, RF-08 |
| ClientController | Interface | - | onRegisterClientClicked(), onSearchClientClicked() | RF-10 |
| ReservationController | Interface | - | onCreateReservationClicked(), onCancelReservationClicked(), onDeleteReservationClicked(), onAddExtraClicked(), onRemoveExtraClicked(), onAddLocationClicked(), onRemoveLocationClicked() | RF-11, RF-12, RF-15, RF-18, RF-22 |
| LocationController | Interface | - | onSaveLocationClicked(), onRemoveLocationClicked() | RF-03, RF-18 |
| CheckInOutController | Interface | - | onCheckInClicked(), onCheckOutClicked() | RF-13, RF-14 |
| StaffController | Interface | - | initialize(), onCreateStaffClicked(), onEditStaffClicked(), onRemoveStaffClicked(), onListUsersClicked() | RF-26, RNF-01 |
| CardController | Interface | - | onIssueCardClicked(), onIssueMasterCardClicked(), onBlockMasterCardClicked(), onShowPlacesByCardClicked(), onShowPassesByRoomClicked() | RF-16, RF-20, RF-21, RF-23, RF-30 |
| StockController | Interface | - | onImportXmlClicked(), onSaveItemClicked(), onRegisterMovementClicked() | RF-28, RF-29 |
| FeedbackController | Interface | - | onSaveFeedbackClicked(), onListFeedbackClicked() | RF-27 |

## Business - Facade

| Class | Layer | Main attributes | Main methods | Requirement |
|-------|-------|-----------------|--------------|-------------|
| HotelFacade | Business - Facade | - | login(), createReservation(), cancelReservation(), deleteReservation(), addExtra(), removeExtra(), addLocation(), removeLocation(reservation, location), registerClient(), findClient(), checkIn(), checkOut(), issueCard(), issueMasterCard(), blockMasterCard(), getPlacesByCard(), getPassesByRoom(), createStaff(name, username, password, role, actor), canCreateStaff(), editStaff(), removeStaff(), listUsers(), searchRooms(), canRegisterRooms(actor), saveRoom(room, actor), listRooms(), listRoomTypes(), configureRoomItem(), setBasePrice(), setExtraPrice(), createLocation(), editLocation(), removeLocation(location), importStock(), createItem(), editItem(), registerMovement() (takes exitReason and user), saveFeedback(), listFeedback() | All RF and RNF-07 (entry point) |

## Business - Services

| Class | Layer | Main attributes | Main methods | Requirement |
|-------|-------|-----------------|--------------|-------------|
| AuthService | Business - Services | - | login(), hashPassword(), isValidPassword() (private) | RF-01, RNF-02 |
| ClientService | Business - Services | - | register(), find() | RF-10, RNF-03 |
| LocationService | Business - Services | - | createLocation(), editLocation(), removeLocation() | RF-03, RF-18 |
| RoomService | Business - Services | - | isAvailable(), searchRooms(), canRegisterRooms(actor), saveRoom(room, actor), listRooms(), listRoomTypes(), configureItem(), setBasePrice(), setExtraPrice() | RF-04, RF-05, RF-06, RF-07, RF-08, RF-09 |
| ReservationService | Business - Services | - | create(), cancel(), delete(), addExtra(), removeExtra(), addLocation(), removeLocation(), checkIn(), checkOut() | RF-05, RF-07, RF-11, RF-12, RF-13, RF-14, RF-15, RF-18, RF-22 |
| StaffService | Business - Services | - | createStaff(name, username, password, role, actor), canCreateStaff(), createFirstManager(), editStaff(), removeStaff(), listUsers() | RF-02, RF-26, RNF-01 |
| CardAccessService | Business - Services | - | issueCard(), issueMasterCard(), blockCard(), canOpen(), register(), getPlacesByCard(), getPassesByRoom(), canOpenRoom() (access rules in the diagram note) | RF-16, RF-17, RF-18, RF-19, RF-20, RF-21, RF-23, RF-24, RF-25, RF-30 |
| StockService | Business - Services | - | importStock(), createItem(), editItem(), registerMovement(), applyMovement() (both take exitReason and user) | RF-28, RF-29 |
| FeedbackService | Business - Services | - | save(), list() | RF-27 |
| LoginFailedException | Business - Exception | - | - (one fixed message for unknown user and wrong password) | RF-01 |
| UsernameTakenException | Business - Exception | - | - | RF-26 |
| AccessDeniedException | Business - Exception | - | - (the user may not do the action) | RF-26 |

## Config

| Class | Layer | Main attributes | Main methods | Requirement |
|-------|-------|-----------------|--------------|-------------|
| AppConfig | Config (record) | dbUrl, dbUser, dbPassword, managerUsername, managerPassword | load() (static), toString() (hides the passwords) | RNF-12 |
| Main | Config | - | main() (static). Uses AppConfig. Creates Database, JdbcUserRepository, JdbcRoomRepository, AuthService, StaffService, RoomService, HotelFacade. Starts HotelApp | RF-01, RNF-12 |
| FirstManagerCommand | Config | - | main() (static). Uses AppConfig and StaffService. Creates Database, JdbcUserRepository, AuthService | RF-01, RNF-12 |

## Data

| Class | Layer | Main attributes | Main methods | Requirement |
|-------|-------|-----------------|--------------|-------------|
| UserRepository | Data - Repository interface | - | findByUsername(), existsByUsername(), existsByRole(), save() | RF-01, RNF-01, RNF-12 |
| RoomRepository | Data - Repository interface | - | floorExists(), roomTypeExists(), findRoomTypeNames(), existsByNumber(), countByFloor(), findAll(), saveWithinFloorLimit() | RF-04 |
| Database | Data | NOT_AVAILABLE (static) | connect(), check(), prepare() (static) | RNF-12 |
| JdbcUserRepository | Data - Repository (SQL Server) | - | findByUsername(), existsByUsername(), existsByRole(), save() | RNF-01, RNF-12 |
| JdbcRoomRepository | Data - Repository (SQL Server) | - | floorExists(), roomTypeExists(), findRoomTypeNames(), existsByNumber(), countByFloor(), findAll(), saveWithinFloorLimit() | RF-04 |
| DuplicateRoomException | Data - Exception | - | - | RF-04 |
| RoomLimitExceededException | Data - Exception | - | - | RF-04 |

## Domain

| Class | Layer | Main attributes | Main methods | Requirement |
|-------|-------|-----------------|--------------|-------------|
| User (abstract) | Domain | id, name, username, passwordHash, role | - | RF-01, RF-02, RNF-02 |
| Receptionist | Domain | (inherits User) | (inherits User) | RF-02, RF-24 |
| Manager | Domain | (inherits User) | (inherits User) | RF-02, RF-23, RF-26, RF-30 |
| RestaurantStaff | Domain | (inherits User) | (inherits User) | RF-02, RF-25 |
| Client | Domain | id, name, document, birthday | - | RF-02, RF-10 |
| Floor | Domain | number, description | - | RF-03 |
| Location | Domain | id, name, guestCardAccess, requiresReservation | - | RF-03, RF-18 |
| Room | Domain | id, number, floorNumber, roomTypeName | - | RF-04, RF-05 |
| RoomType | Domain | id, name, capacity, basePrice | - | RF-04, RF-06 |
| RoomItem | Domain | quantity | - | RF-08 |
| Reservation | Domain | id, startDate, endDate, status, checkedInAt, checkedOutAt | checkIn(), checkOut(), cancel(), overlaps(), calculateTotal() | RF-05, RF-11, RF-12, RF-13, RF-14, RF-22 |
| ReservationExtra | Domain | quantity, unitPrice, usedAt | - | RF-07, RF-09, RF-15 |
| ExtraService | Domain | id, name, price | - | RF-07, RF-09 |
| MagneticCard | Domain | id, number, type, active, blockedAt | - | RF-16, RF-17, RF-23, RF-30 |
| CardAccessRecord | Domain | entryDateTime | - | RF-19, RF-20, RF-21 |
| StockItem | Domain | id, name, unit, stockQuantity | - | RF-28, RF-29 |
| StockMovement | Domain | type, quantity, dateTime, exitReason | - | RF-29 |
| StockImport | Domain | supplierName, importedAt | - | RF-28 |
| Feedback | Domain | type, description, createdAt (client may be null: anonymous) | - | RF-27 |

`StockService.importStock(file, user)` imports every item from a valid XML file. It creates missing catalogue items and records ENTRY movements. `StockImport` keeps supplier and time; its linked movements identify items and quantities. The PR #20 decision of 2026-10-21 removes the rejected-item field and getter. It does not add a preview API (Q-10, Q-38).

## Enums

| Class | Layer | Main attributes | Main methods | Requirement |
|-------|-------|-----------------|--------------|-------------|
| Role | Enum | CLIENT, RECEPTIONIST, MANAGER, RESTAURANT, SUPPLIER | - | RF-02 |
| ReservationStatus | Enum | RESERVED, CANCELLED, CHECKED_IN, CHECKED_OUT | - | RF-12, RF-13, RF-14 |
| CardType | Enum | CLIENT, MASTER | - | RF-16, RF-23 |
| FeedbackType | Enum | SUGGESTION, COMPLAINT | - | RF-27 |
| MovementType | Enum | ENTRY, USE, EXIT | - | RF-29 |
| ExitReason | Enum | DISCARD, RETURN, LOSS | - | RF-29 |

Created by: BorjaTiago. Updated by: pedrocostaisep. AI use: Claude Code, claude-haiku-5-5, 2026-10-16; edited by Claude Code, claude-haiku-5-5, 2026-10-16; doc-updater, Claude Code (claude-sonnet-5-5), 2026-10-17; doc-updater, Claude Code (claude-haiku-5-5), 2026-10-17; doc-updater, Claude Code (claude-haiku-5-5), 2026-10-19; PR-20 import decision review by sequence-diagram-editor, OpenAI Codex (GPT-6), 2026-10-22; merge-conflict resolution by sequence-diagram-editor, OpenAI Codex (GPT-6), 2026-10-22; doc-updater, Claude Code (claude-opus-5-5), 2026-10-23; H4Y-27 staff account update, Claude Code (claude-opus-5-5), 2026-10-23; H4Y-25 room registration, OpenAI Codex (GPT-6), 2026-10-27. Reviewed by: pending.
