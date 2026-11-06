package pt.isep.hotel4you.service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import pt.isep.hotel4you.data.ReservationRepository;
import pt.isep.hotel4you.data.RoomTypeRepository;
import pt.isep.hotel4you.domain.Client;
import pt.isep.hotel4you.domain.ClientReservation;
import pt.isep.hotel4you.domain.Permission;
import pt.isep.hotel4you.domain.Reservation;
import pt.isep.hotel4you.domain.ReservationQuote;
import pt.isep.hotel4you.domain.ReservationStatus;
import pt.isep.hotel4you.domain.Room;
import pt.isep.hotel4you.domain.RoomType;
import pt.isep.hotel4you.domain.User;

/** Applies the reservation rules. */
public class ReservationService {

    private static final String NO_PERMISSION = "Não tem permissão para gerir reservas.";

    private final ReservationRepository reservationRepository;
    private final RoomService roomService;
    private final RoomTypeRepository roomTypeRepository;

    /**
     * Creates the service.
     *
     * @param reservationRepository the reservation store
     * @param roomService the owner of the free-night rule
     * @param roomTypeRepository the room type catalogue with the base prices
     */
    public ReservationService(ReservationRepository reservationRepository, RoomService roomService,
            RoomTypeRepository roomTypeRepository) {
        this.reservationRepository = reservationRepository;
        this.roomService = roomService;
        this.roomTypeRepository = roomTypeRepository;
    }

    /**
     * Creates a reservation of a free room for a client.
     *
     * @param client the saved client
     * @param room the room to reserve
     * @param start the first night
     * @param end the departure date
     * @param actor the logged-in user who creates the reservation
     * @return the saved reservation with the state RESERVED
     * @throws AccessDeniedException if the actor may not manage reservations
     * @throws IllegalArgumentException if data is missing (the message names all missing fields)
     *         or the departure is not after the arrival
     * @throws RoomNotAvailableException if the room has a reservation in the nights
     * @throws IllegalStateException if the database is not available
     */
    public Reservation create(Client client, Room room, LocalDate start, LocalDate end, User actor) {
        checkPermission(actor);
        List<String> missing = new ArrayList<>();
        if (client == null || client.getId() == null) {
            missing.add("cliente");
        }
        if (room == null || room.getId() == null) {
            missing.add("quarto");
        }
        addMissingDates(missing, start, end);
        throwIfMissing(missing);
        Reservation.validatePeriod(start, end);
        if (!roomService.isAvailable(room, start, end)) {
            throw new RoomNotAvailableException();
        }
        return reservationRepository.save(new Reservation(null, client.getId(), room.getId(),
                start, end, ReservationStatus.RESERVED, actor.getId()));
    }

    /**
     * Gives the base price per night of the room type and the number of nights.
     *
     * @param room the chosen room
     * @param start the first night
     * @param end the departure date
     * @param actor the logged-in user who asks
     * @return the price summary; the price is null if the type has no base price yet
     * @throws AccessDeniedException if the actor may not manage reservations
     * @throws IllegalArgumentException if data is missing or the departure is not after the arrival
     * @throws IllegalStateException if the database is not available
     */
    public ReservationQuote quote(Room room, LocalDate start, LocalDate end, User actor) {
        checkPermission(actor);
        List<String> missing = new ArrayList<>();
        if (room == null) {
            missing.add("quarto");
        }
        addMissingDates(missing, start, end);
        throwIfMissing(missing);
        Reservation.validatePeriod(start, end);
        BigDecimal price = roomTypeRepository.findAll().stream()
                .filter(type -> type.getName().equalsIgnoreCase(room.getRoomTypeName()))
                .map(RoomType::getBasePrice)
                .filter(value -> value != null)
                .findFirst()
                .orElse(null);
        return new ReservationQuote(price, ChronoUnit.DAYS.between(start, end));
    }

    /**
     * Lists the reservations of a client, with the cancelled ones.
     *
     * @param client the saved client
     * @param actor the logged-in user who asks
     * @return the reservations, newest arrival first
     * @throws AccessDeniedException if the actor may not manage reservations
     * @throws IllegalArgumentException if the client is missing
     * @throws IllegalStateException if the database is not available
     */
    public List<ClientReservation> listClientReservations(Client client, User actor) {
        checkPermission(actor);
        if (client == null || client.getId() == null) {
            throwIfMissing(List.of("cliente"));
        }
        return reservationRepository.findByClient(client.getId());
    }

    private static void checkPermission(User actor) {
        if (!Permission.MANAGE_RESERVATIONS.allows(actor)) {
            throw new AccessDeniedException(NO_PERMISSION);
        }
    }

    private static void addMissingDates(List<String> missing, LocalDate start, LocalDate end) {
        if (start == null) {
            missing.add("data de entrada");
        }
        if (end == null) {
            missing.add("data de saída");
        }
    }

    private static void throwIfMissing(List<String> missing) {
        if (!missing.isEmpty()) {
            throw new IllegalArgumentException("Faltam dados: " + String.join(", ", missing) + ".");
        }
    }
}
