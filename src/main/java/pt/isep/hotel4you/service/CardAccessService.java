package pt.isep.hotel4you.service;

import java.time.Clock;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import pt.isep.hotel4you.data.CardAccessRecordRepository;
import pt.isep.hotel4you.data.CardRepository;
import pt.isep.hotel4you.data.FloorRepository;
import pt.isep.hotel4you.data.ReservationRepository;
import pt.isep.hotel4you.data.RoomRepository;
import pt.isep.hotel4you.data.UserRepository;
import pt.isep.hotel4you.domain.CardAccessRecord;
import pt.isep.hotel4you.domain.CardPlace;
import pt.isep.hotel4you.domain.CardType;
import pt.isep.hotel4you.domain.Floor;
import pt.isep.hotel4you.domain.IssuedCard;
import pt.isep.hotel4you.domain.Location;
import pt.isep.hotel4you.domain.MagneticCard;
import pt.isep.hotel4you.domain.Permission;
import pt.isep.hotel4you.domain.Reservation;
import pt.isep.hotel4you.domain.ReservationStatus;
import pt.isep.hotel4you.domain.Role;
import pt.isep.hotel4you.domain.Room;
import pt.isep.hotel4you.domain.User;

/**
 * Manages the magnetic cards: issue, activate, deactivate and block a card, issue and replace the
 * master card, tell which doors a card opens, log the entries and read them by card or by room
 * (RF-16 to RF-21, RF-23). Every new card gets a 6 character access code. The service returns the
 * plain code once and stores only its hash. The client and the manager log in with the card number
 * and this code (H4Y-109 AC-4).
 */
public class CardAccessService {

    /** The message when the card may not enter the place. */
    public static final String ENTRY_REFUSED = "Entrada recusada.";

    /** The message when the card number or the access code is wrong. */
    public static final String LOGIN_FAILED = "Número do cartão ou código errado.";

    private static final String UNKNOWN_CARD = "Cartão desconhecido.";
    private static final String BLOCKED_CARD = "O cartão está bloqueado.";
    private static final String INACTIVE_CARD = "O cartão não está ativo.";
    private static final String NO_CHECK_IN = "A reserva do cartão não tem check-in.";
    private static final String NO_ROOM = "Este cartão não abre quartos.";
    private static final String OTHER_ROOM = "Este quarto não é o da sua reserva.";
    private static final String ROLE_ZONE = "O seu perfil não dá acesso a este local.";
    private static final String CLIENT_ZONE = "O cartão não abre este local.";
    private static final String NOT_RESERVED = "A sua reserva não inclui este local.";

    private final CardRepository cardRepository;
    private final CardAccessRecordRepository recordRepository;
    private final ReservationRepository reservationRepository;
    private final UserRepository userRepository;
    private final RoomRepository roomRepository;
    private final FloorRepository floorRepository;
    private final LocationService locationService;
    private final CardNumberGenerator numberGenerator;
    private final AccessCodeGenerator codeGenerator;
    private final AuthService authService;
    private final Clock clock;

    /**
     * Creates the service.
     *
     * @param cardRepository stores the cards
     * @param recordRepository stores the card entries
     * @param reservationRepository reads the reservation of a client card
     * @param userRepository reads the user of a staff card
     * @param roomRepository reads the rooms of the screen "O meu cartão"
     * @param floorRepository reads the zones of the screen "O meu cartão"
     * @param locationService gives the zone rule of a staff role
     * @param numberGenerator makes new card numbers
     * @param codeGenerator makes new access codes
     * @param authService hashes the access codes
     * @param clock gives the time of a block and of an entry
     */
    public CardAccessService(CardRepository cardRepository, CardAccessRecordRepository recordRepository,
            ReservationRepository reservationRepository, UserRepository userRepository,
            RoomRepository roomRepository, FloorRepository floorRepository, LocationService locationService, CardNumberGenerator numberGenerator,
            AccessCodeGenerator codeGenerator, AuthService authService, Clock clock) {
        this.cardRepository = cardRepository;
        this.recordRepository = recordRepository;
        this.reservationRepository = reservationRepository;
        this.userRepository = userRepository;
        this.roomRepository = roomRepository;
        this.floorRepository = floorRepository;
        this.locationService = locationService;
        this.numberGenerator = numberGenerator;
        this.codeGenerator = codeGenerator;
        this.authService = authService;
        this.clock = clock;
    }

    /**
     * Issues the client card of a reservation (RF-16). The card is active and gets a new number
     * and a new access code.
     *
     * @param reservation the reservation, with or without check-in
     * @param actor the logged-in user
     * @return the saved card and its plain access code
     * @throws AccessDeniedException if the user is not a receptionist or a manager
     * @throws ReservationNotFoundException if the reservation does not exist
     * @throws IllegalStateException if the reservation is cancelled or closed, or if the database
     *         is not available
     * @throws CardAlreadyIssuedException if the reservation already has a card
     */
    public IssuedCard issueCard(Reservation reservation, User actor) {
        require(Permission.MANAGE_RESERVATIONS, actor, "Só o rececionista ou o gestor podem emitir cartões.");
        Reservation saved = Optional.ofNullable(reservation)
                .map(Reservation::getId)
                .flatMap(reservationRepository::findById)
                .orElseThrow(ReservationNotFoundException::new);
        if (saved.getStatus() != ReservationStatus.RESERVED && saved.getStatus() != ReservationStatus.CHECKED_IN) {
            throw new IllegalStateException("A reserva não está ativa.");
        }
        if (cardRepository.findByReservationId(saved.getId()).isPresent()) {
            throw new CardAlreadyIssuedException();
        }
        return issue(CardType.CLIENT, saved.getId(), null);
    }

    /**
     * Issues a master card for a manager (RF-20). The manager may have only one master card that
     * is not blocked.
     *
     * @param manager the owner of the new card
     * @param actor the logged-in user
     * @return the saved card and its plain access code
     * @throws AccessDeniedException if the user is not a manager
     * @throws IllegalArgumentException if the owner is not a saved manager
     * @throws MasterCardActiveException if the owner has a master card that is not blocked
     * @throws IllegalStateException if the database is not available
     */
    public IssuedCard issueMasterCard(User manager, User actor) {
        require(Permission.MANAGE_CARDS, actor, "Só o gestor pode emitir o cartão mestre.");
        if (manager == null || manager.getId() == null || manager.getRole() != Role.MANAGER) {
            throw new IllegalArgumentException("O cartão mestre pertence a um gestor.");
        }
        return issueMaster(manager.getId());
    }

    /**
     * Replaces a lost master card (RF-21). Block the old card first. The new card has a new
     * number and belongs to the same manager. The entries of the old card stay.
     *
     * @param oldNumber the number of the blocked master card
     * @param actor the logged-in user
     * @return the new card and its plain access code
     * @throws AccessDeniedException if the user is not a manager
     * @throws CardNotFoundException if no card has this number
     * @throws IllegalArgumentException if the card is not a master card
     * @throws MasterCardActiveException if the old card or another master card of the manager is
     *         not blocked
     * @throws IllegalStateException if the database is not available
     */
    public IssuedCard replaceMasterCard(String oldNumber, User actor) {
        require(Permission.MANAGE_CARDS, actor, "Só o gestor pode substituir o cartão mestre.");
        MagneticCard old = find(oldNumber);
        if (old.getType() != CardType.MASTER) {
            throw new IllegalArgumentException("O cartão não é um cartão mestre.");
        }
        return issueMaster(old.getUserId());
    }

    /**
     * Activates a card again, for example after a check-out by mistake.
     *
     * @param number the card number
     * @param actor the logged-in user
     * @return the active card
     * @throws AccessDeniedException if the user is not a receptionist or a manager
     * @throws CardNotFoundException if no card has this number
     * @throws CardBlockedException if the card is blocked
     * @throws IllegalStateException if the database is not available
     */
    public MagneticCard activateCard(String number, User actor) {
        require(Permission.MANAGE_RESERVATIONS, actor, "Só o rececionista ou o gestor podem ativar cartões.");
        MagneticCard card = find(number);
        if (card.isBlocked()) {
            throw new CardBlockedException();
        }
        return change(card, true, null);
    }

    /**
     * Deactivates a card, for example at the check-out (RF-18). The card keeps its number, code and
     * entries.
     *
     * @param number the card number
     * @param actor the logged-in user
     * @return the inactive card
     * @throws AccessDeniedException if the user is not a receptionist or a manager
     * @throws CardNotFoundException if no card has this number
     * @throws IllegalStateException if the database is not available
     */
    public MagneticCard deactivateCard(String number, User actor) {
        require(Permission.MANAGE_RESERVATIONS, actor, "Só o rececionista ou o gestor podem desativar cartões.");
        MagneticCard card = find(number);
        return change(card, false, card.getBlockedAt());
    }

    /**
     * Blocks a card for good (RF-21). The card stops working and keeps its entries.
     *
     * @param number the card number
     * @param actor the logged-in user
     * @return the blocked card
     * @throws AccessDeniedException if the user is not a manager
     * @throws CardNotFoundException if no card has this number
     * @throws CardBlockedException if the card is already blocked
     * @throws IllegalStateException if the database is not available
     */
    public MagneticCard blockCard(String number, User actor) {
        require(Permission.MANAGE_CARDS, actor, "Só o gestor pode bloquear cartões.");
        MagneticCard card = find(number);
        if (card.isBlocked()) {
            throw new CardBlockedException();
        }
        return change(card, false, LocalDateTime.now(clock));
    }

    /**
     * Tells if a card opens a room (RF-17, RF-19). An active master card opens every room. A
     * client card opens only the room of its reservation, after the check-in. A staff card opens
     * no room.
     *
     * @param card the card, can be null
     * @param room the room, can be null
     * @return true if the card opens the room
     * @throws IllegalStateException if the database is not available
     */
    public boolean canOpenRoom(MagneticCard card, Room room) {
        return card != null && room != null && roomRefusal(card, room).isEmpty();
    }

    /**
     * Tells if a card opens a zone (RF-17, RF-19, Q-45). An active master card opens every zone. A
     * client card opens a zone only after the check-in, if the zone accepts client cards, and, for
     * a zone that needs a reservation, only if the reservation includes it. A staff card follows
     * the zone rule of the role of its user.
     *
     * @param card the card, can be null
     * @param location the zone, can be null
     * @return true if the card opens the zone
     * @throws IllegalStateException if the database is not available
     */
    public boolean canOpen(MagneticCard card, Location location) {
        return card != null && location != null && zoneRefusal(card, location).isEmpty();
    }

    /**
     * Logs an entry of a card into a room or a zone, with the current time (RF-23). Only entries
     * are logged: a new entry means that the person left the place before. The service reads the
     * card again by its number, so a card that is unknown, or was blocked or deactivated after the
     * login, is refused (H4Y-135).
     *
     * @param card the card that is passed at the door
     * @param room the room, or null for a zone
     * @param location the zone, or null for a room
     * @return the saved entry
     * @throws IllegalArgumentException if the card is null, or if there is no place or two places
     * @throws EntryRefusedException if the card may not enter the place, with the reason; nothing
     *         is saved
     * @throws IllegalStateException if the database is not available
     */
    public CardAccessRecord register(MagneticCard card, Room room, Location location) {
        if (card == null) {
            throw new IllegalArgumentException("O cartão é obrigatório.");
        }
        if ((room == null) == (location == null)) {
            throw new IllegalArgumentException("Escolha um quarto ou um local.");
        }
        MagneticCard saved = Optional.ofNullable(card.getNumber())
                .flatMap(cardRepository::findByNumber)
                .orElseThrow(() -> new EntryRefusedException(UNKNOWN_CARD));
        Optional<String> refusal = room != null ? roomRefusal(saved, room) : zoneRefusal(saved, location);
        if (refusal.isPresent()) {
            throw new EntryRefusedException(refusal.get());
        }
        return recordRepository.save(new CardAccessRecord(null, saved.getId(),
                room == null ? null : room.getId(), location == null ? null : location.getId(),
                LocalDateTime.now(clock)));
    }

    /**
     * Lists the places of the screen "O meu cartão" (H4Y-109 AC-6). A client card gets the room
     * of its reservation. A master card gets every room. Then every zone follows, in floor order.
     * Each place tells if the card may enter it, with the rules of {@link #canOpenRoom} and
     * {@link #canOpen}.
     *
     * @param card the card of the logged-in person
     * @return the rooms first, then the zones
     * @throws IllegalArgumentException if the card is null
     * @throws IllegalStateException if the database is not available
     */
    public List<CardPlace> listPlaces(MagneticCard card) {
        if (card == null) {
            throw new IllegalArgumentException("O cartão é obrigatório.");
        }
        List<CardPlace> places = new ArrayList<>();
        for (Room room : roomsOf(card)) {
            places.add(new CardPlace("Quarto " + room.getNumber(), room, null, canOpenRoom(card, room)));
        }
        for (Floor floor : floorRepository.findAll()) {
            for (Location zone : floor.locations()) {
                places.add(new CardPlace(zone.getName(), null, zone, canOpen(card, zone)));
            }
        }
        return places;
    }

    /**
     * Passes the card at a place of the screen "O meu cartão" (H4Y-109 AC-7). The rule is checked
     * again, so an old screen cannot log a refused entry.
     *
     * @param card the card of the logged-in person
     * @param place the room or the zone that the person clicked
     * @return the saved entry
     * @throws IllegalArgumentException if the card or the place is null
     * @throws EntryRefusedException if the card may not enter the place, with the reason; nothing is saved
     * @throws IllegalStateException if the database is not available
     */
    public CardAccessRecord enter(MagneticCard card, CardPlace place) {
        if (place == null) {
            throw new IllegalArgumentException("Escolha um quarto ou um local.");
        }
        return register(card, place.room(), place.location());
    }

    /**
     * Lists the entries of a card, the oldest first (RF-23).
     *
     * @param number the card number
     * @param actor the logged-in user
     * @return the entries, or an empty list
     * @throws AccessDeniedException if the user is not a manager
     * @throws CardNotFoundException if no card has this number
     * @throws IllegalStateException if the database is not available
     */
    public List<CardAccessRecord> getPlacesByCard(String number, User actor) {
        require(Permission.MANAGE_CARDS, actor, "Só o gestor pode consultar as entradas.");
        return recordRepository.findByCardId(find(number).getId());
    }

    /**
     * Lists the entries into a room, by any card, the oldest first (RF-23).
     *
     * @param room the room
     * @param actor the logged-in user
     * @return the entries, or an empty list
     * @throws AccessDeniedException if the user is not a manager
     * @throws IllegalArgumentException if the room is null or not saved
     * @throws IllegalStateException if the database is not available
     */
    public List<CardAccessRecord> getPassesByRoom(Room room, User actor) {
        require(Permission.MANAGE_CARDS, actor, "Só o gestor pode consultar as entradas.");
        if (room == null || room.getId() == null) {
            throw new IllegalArgumentException("O quarto é obrigatório.");
        }
        return recordRepository.findByRoomId(room.getId());
    }

    /**
     * Logs in with a card, as if the card was passed in a reader (H4Y-109 AC-4). The client uses the
     * card number and the access code that was shown when the card was issued. The manager uses the
     * master card in the same way. The code is not case sensitive.
     *
     * @param number the card number
     * @param code the access code of the card
     * @return the card, to use with {@link #canOpen}, {@link #canOpenRoom} and {@link #register}
     * @throws IllegalArgumentException if the number or the code is blank
     * @throws LoginFailedException if the card does not exist, has no code or the code is wrong
     * @throws CardBlockedException if the card is blocked
     * @throws CardInactiveException if the card is not active, for example after the check-out
     */
    public MagneticCard login(String number, String code) {
        if (number == null || number.isBlank() || code == null || code.isBlank()) {
            throw new IllegalArgumentException("Preencha o número do cartão e o código.");
        }
        MagneticCard card = cardRepository.findByNumber(number.trim())
                .orElseThrow(() -> new LoginFailedException(LOGIN_FAILED));
        if (!authService.matchesPassword(code.trim().toUpperCase(Locale.ROOT), card.getAccessCodeHash())) {
            throw new LoginFailedException(LOGIN_FAILED);
        }
        if (card.isBlocked()) {
            throw new CardBlockedException();
        }
        if (!card.isActive()) {
            throw new CardInactiveException();
        }
        return card;
    }

    private static void require(Permission permission, User actor, String message) {
        if (!permission.allows(actor)) {
            throw new AccessDeniedException(message);
        }
    }

    private MagneticCard find(String number) {
        if (number == null || number.isBlank()) {
            throw new CardNotFoundException();
        }
        return cardRepository.findByNumber(number.trim()).orElseThrow(CardNotFoundException::new);
    }

    private IssuedCard issueMaster(long userId) {
        boolean hasActiveMaster = cardRepository.findByUserId(userId).stream()
                .anyMatch(card -> card.getType() == CardType.MASTER && !card.isBlocked());
        if (hasActiveMaster) {
            throw new MasterCardActiveException();
        }
        return issue(CardType.MASTER, null, userId);
    }

    private IssuedCard issue(CardType type, Long reservationId, Long userId) {
        String code = codeGenerator.next();
        MagneticCard card = cardRepository.save(new MagneticCard(null, numberGenerator.next(), type,
                reservationId, userId, true, null, authService.hashPassword(code)));
        return new IssuedCard(card, code);
    }

    private MagneticCard change(MagneticCard card, boolean active, LocalDateTime blockedAt) {
        MagneticCard changed = new MagneticCard(card.getId(), card.getNumber(), card.getType(),
                card.getReservationId(), card.getUserId(), active, blockedAt, card.getAccessCodeHash());
        if (!cardRepository.update(changed)) {
            throw new CardNotFoundException();
        }
        return changed;
    }

    private static Optional<String> cardRefusal(MagneticCard card) {
        if (card.isBlocked()) {
            return Optional.of(BLOCKED_CARD);
        }
        if (!card.isActive()) {
            return Optional.of(INACTIVE_CARD);
        }
        return Optional.empty();
    }

    private Optional<String> roomRefusal(MagneticCard card, Room room) {
        Optional<String> refusal = cardRefusal(card);
        if (refusal.isPresent() || card.getType() == CardType.MASTER) {
            return refusal;
        }
        if (card.getReservationId() == null) {
            return Optional.of(NO_ROOM);
        }
        return checkedInReservation(card)
                .map(reservation -> room.getId() != null && reservation.getRoomId() == room.getId()
                        ? Optional.<String>empty()
                        : Optional.of(OTHER_ROOM))
                .orElse(Optional.of(NO_CHECK_IN));
    }

    private Optional<String> zoneRefusal(MagneticCard card, Location location) {
        Optional<String> refusal = cardRefusal(card);
        if (refusal.isPresent() || card.getType() == CardType.MASTER) {
            return refusal;
        }
        if (card.getUserId() != null) {
            boolean allowed = userRepository.findById(card.getUserId())
                    .map(user -> locationService.canAccess(location, user))
                    .orElse(false);
            return allowed ? Optional.empty() : Optional.of(ROLE_ZONE);
        }
        return checkedInReservation(card)
                .map(reservation -> clientZoneRefusal(reservation, location))
                .orElse(Optional.of(NO_CHECK_IN));
    }

    private static Optional<String> clientZoneRefusal(Reservation reservation, Location location) {
        if (!location.isGuestCardAccess()) {
            return Optional.of(CLIENT_ZONE);
        }
        if (location.isRequiresReservation() && !reservation.getLocationIds().contains(location.getId())) {
            return Optional.of(NOT_RESERVED);
        }
        return Optional.empty();
    }

    private List<Room> roomsOf(MagneticCard card) {
        if (card.getType() == CardType.MASTER) {
            return roomRepository.findAll();
        }
        if (card.getReservationId() == null) {
            return List.of();
        }
        return reservationRepository.findById(card.getReservationId())
                .flatMap(reservation -> roomRepository.findById(reservation.getRoomId()))
                .stream()
                .toList();
    }

    private Optional<Reservation> checkedInReservation(MagneticCard card) {
        if (card.getReservationId() == null) {
            return Optional.empty();
        }
        return reservationRepository.findById(card.getReservationId())
                .filter(reservation -> reservation.getStatus() == ReservationStatus.CHECKED_IN);
    }
}
