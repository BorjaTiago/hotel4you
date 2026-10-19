# UML - Traceability

Requirements from [../requirements/requirements.md](../requirements/requirements.md) to the classes and methods of [class-diagram.md](class-diagram.md). "Not covered" means no class or method in the diagram. Guide: [guide.md](guide.md).

## Functional requirements

| Requirement | Classes and methods that cover it |
|-------------|-----------------------------------|
| RF-01 Login with user name and password | LoginController.onLoginClicked, HotelFacade.login, AuthService.login, User.checkPassword |
| RF-02 Five actor types | Role (enum), User, Receptionist, Manager, RestaurantStaff, Client |
| RF-03 Building floors and zones | Floor, Location, LocationController.onSaveLocationClicked, LocationService.createLocation |
| RF-04 Three room types, 20 rooms | Room, RoomType, RoomController.onSaveRoomClicked |
| RF-05 Client chooses a free room | RoomController.onSearchClicked, HotelFacade.searchRooms, RoomService.isAvailable, RoomService.searchRooms, Reservation.overlaps, ReservationService.create |
| RF-06 Price per room type | RoomType.basePrice, RoomService.setBasePrice, HotelFacade.setBasePrice, RoomController.onSavePriceClicked, Reservation.calculateTotal |
| RF-07 Extra costs (view, drinks, laundry) | ExtraService.price, ReservationExtra, ReservationService.addExtra, RoomService.setExtraPrice, ReservationController.onAddExtraClicked |
| RF-08 Room items set by manager | RoomItem.quantity, RoomService.configureItem, HotelFacade.configureRoomItem, RoomController.onConfigureRoomItemClicked |
| RF-09 Other paid services | ExtraService, ReservationService.addExtra, RoomService.setExtraPrice |
| RF-10 Reception registers clients | ClientController.onRegisterClientClicked, ClientService.register, HotelFacade.registerClient, Client |
| RF-11 Reception makes reservations | ReservationController.onCreateReservationClicked, ReservationService.create, HotelFacade.createReservation, Reservation, ReservationController.onRemoveExtraClicked, ReservationService.removeExtra, HotelFacade.removeExtra (extras change until check-in), ReservationController.onAddLocationClicked, ReservationController.onRemoveLocationClicked, ReservationService.addLocation, ReservationService.removeLocation, HotelFacade.addLocation, HotelFacade.removeLocation (zones change until check-in) |
| RF-12 Reception cancels reservations | ReservationController.onCancelReservationClicked, ReservationService.cancel, Reservation.cancel, ReservationStatus.CANCELLED |
| RF-13 Check-in | CheckInOutController.onCheckInClicked, ReservationService.checkIn, Reservation.checkIn, ReservationStatus.CHECKED_IN |
| RF-14 Check-out | CheckInOutController.onCheckOutClicked, ReservationService.checkOut, Reservation.checkOut, ReservationStatus.CHECKED_OUT |
| RF-15 Room services (food, drink, laundry) | ReservationController.onAddExtraClicked, ReservationService.addExtra, ReservationExtra.usedAt |
| RF-16 Card given at check-in, one per reservation | CardController.onIssueCardClicked, HotelFacade.issueCard, CardAccessService.issueCard, MagneticCard, CardType.CLIENT |
| RF-17 Card opens client room | CardAccessService.canOpenRoom (client card opens its own room only), CardAccessService.register (room entry), CardAccessRecord.room, MagneticCard, CardType.CLIENT |
| RF-18 Card opens restaurant and pool only with reservation | Location.guestCardAccess, Location.requiresReservation, Reservation (includes Location), ReservationController.onAddLocationClicked, ReservationController.onRemoveLocationClicked, ReservationService.addLocation, ReservationService.removeLocation (only zones with both flags), CardAccessService.canOpen, LocationController.onSaveLocationClicked |
| RF-19 Card entry log (place, time) | CardAccessService.register, CardAccessRecord.entryDateTime |
| RF-20 Places of one card | CardController.onShowPlacesByCardClicked, HotelFacade.getPlacesByCard, CardAccessService.getPlacesByCard |
| RF-21 Passes of one room (with master card) | CardController.onShowPassesByRoomClicked, HotelFacade.getPassesByRoom, CardAccessService.getPassesByRoom |
| RF-22 Only manager deletes, before check-in | ReservationController.onDeleteReservationClicked, ReservationService.delete(reservation, user), HotelFacade.deleteReservation, Manager. Note: the role check is not shown in the diagram. |
| RF-23 Master card for all rooms and zones | CardController.onIssueMasterCardClicked, CardAccessService.issueMasterCard(manager), HotelFacade.issueMasterCard, CardAccessService.canOpenRoom (master card opens all rooms), CardType.MASTER, Manager, MagneticCard. Note: master access to zones is in the CardAccessService note (access rules), not in a method signature. |
| RF-24 Reception access only floors 0 and 1 | Receptionist, Floor, CardAccessService.canOpen (receptionist card rule in the CardAccessService note: floors 0 and 1, no guest room) |
| RF-25 Restaurant staff access only kitchen and restaurant | RestaurantStaff, Location, CardAccessService.canOpen (restaurant staff card rule in the CardAccessService note: kitchen and restaurant, no guest room) |
| RF-26 Manager manages staff accounts | StaffController.onCreateStaffClicked, onEditStaffClicked, onRemoveStaffClicked, StaffService.createStaff, editStaff, removeStaff, HotelFacade.createStaff |
| RF-27 Suggestions and complaints | FeedbackController.onSaveFeedbackClicked, onListFeedbackClicked, FeedbackService.save, FeedbackService.list, Feedback, FeedbackType |
| RF-28 Stock import from supplier XML | StockController.onImportXmlClicked, onConfirmImportClicked; HotelFacade.previewStock, confirmStockImport; StockService.previewStock, confirmStockImport; StockImportPreview; StockImport.getRejectedItems; StockItem. SD-06 shows review, cancel, confirm and report. New review contracts are documented designs (G-05). Existing HotelFacade.importStock and StockService.importStock remain the one-step API, outside this UI flow. |
| RF-29 Entry, use and exit of stock | StockController.onRegisterMovementClicked, HotelFacade.registerMovement, StockService.registerMovement, StockService.applyMovement (both take exitReason and user), StockMovement, MovementType, ExitReason, StockItem.stockQuantity |
| RF-30 Block lost master card, issue new one | CardController.onBlockMasterCardClicked, CardAccessService.blockCard, HotelFacade.blockMasterCard, MagneticCard.active, MagneticCard.blockedAt. Note: the log of the block is not shown in the diagram. |

## Non-functional requirements

| Requirement | Classes and methods that cover it |
|-------------|-----------------------------------|
| RNF-01 All users saved and accessible | StaffController.onListUsersClicked, HotelFacade.listUsers, StaffService.listUsers |
| RNF-02 Passwords saved encrypted | AuthService.hashPassword, User.passwordHash, User.checkPassword |
| RNF-03 Client registration and reservation in 30 seconds | Not covered by a class (process and usability test) |
| RNF-04 Simple and intuitive GUI | Not covered by a class (process and usability test) |
| RNF-05 Software in Java | Not covered. The diagram shows no class for a language rule. |
| RNF-06 JavaFX GUI | All Interface classes (LoginController to FeedbackController) |
| RNF-07 Business logic separate from GUI | HotelFacade (note: "The business layer does not use JavaFX"), Interface layer calls only the facade |
| RNF-08 Maintainable class structure | Layers: Interface, Facade, Services, Domain, Enums. Facade and services split by topic. |
| RNF-09 Javadoc | Not covered. The diagram shows no class for documentation. |
| RNF-10 JUnit unit tests | Not covered. The diagram shows no test classes. |

Created by: BorjaTiago. Updated by: pedrocostaisep. AI use: Claude Code, claude-haiku-5-5, 2026-10-16; edited by Claude Code, claude-haiku-5-5, 2026-10-16; PR-20 stock import review by sequence-diagram-editor, OpenAI Codex (GPT-6), 2026-10-19. Reviewed by: pending.
