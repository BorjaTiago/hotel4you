package pt.isep.hotel4you.service;

import java.math.BigDecimal;
import java.time.temporal.ChronoUnit;
import pt.isep.hotel4you.data.ReservationExtrasRepository;
import pt.isep.hotel4you.data.ReservationRepository;
import pt.isep.hotel4you.data.RoomRepository;
import pt.isep.hotel4you.data.RoomTypeRepository;
import pt.isep.hotel4you.domain.ExtraType;
import pt.isep.hotel4you.domain.Permission;
import pt.isep.hotel4you.domain.Reservation;
import pt.isep.hotel4you.domain.ReservationExtra;
import pt.isep.hotel4you.domain.ReservationPrice;
import pt.isep.hotel4you.domain.Room;
import pt.isep.hotel4you.domain.User;

/** Calculates the price of a saved reservation using its current inclusions. */
public class ReservationPriceService {
    private final ReservationRepository reservations;
    private final RoomRepository rooms;
    private final RoomTypeRepository roomTypes;
    private final ReservationExtrasRepository extras;

    /**
     * Creates the price service.
     * @param reservations the reservation store
     * @param rooms the room store
     * @param roomTypes the room types with the base price
     * @param extras the saved room extras and paid services
     */
    public ReservationPriceService(ReservationRepository reservations, RoomRepository rooms,
            RoomTypeRepository roomTypes, ReservationExtrasRepository extras) {
        this.reservations = reservations;
        this.rooms = rooms;
        this.roomTypes = roomTypes;
        this.extras = extras;
    }

    /**
     * Reads the room, prices and nights for a reservation.
     * @param reservationId the reservation identifier
     * @param actor the logged-in user
     * @return the price summary, with no total if a base price is missing
     * @throws AccessDeniedException if the user cannot manage reservations
     * @throws ReservationNotFoundException if the reservation does not exist
     * @throws IllegalStateException if the room is missing or the data cannot be read
     */
    public ReservationPrice quote(long reservationId, User actor) {
        if (!Permission.MANAGE_RESERVATIONS.allows(actor)) {
            throw new AccessDeniedException("Não tem permissão para gerir reservas.");
        }
        Reservation reservation = reservations.findById(reservationId)
                .orElseThrow(ReservationNotFoundException::new);
        Room room = rooms.findById(reservation.getRoomId())
                .orElseThrow(() -> new IllegalStateException("O quarto da reserva não existe."));
        BigDecimal basePrice = ReservationService.findBasePrice(roomTypes, room.getRoomTypeName());
        long nights = ChronoUnit.DAYS.between(reservation.getStartDate(), reservation.getEndDate());
        return new ReservationPrice(basePrice, sum(reservationId, ExtraType.ROOM_EXTRA),
                BigDecimal.ZERO, nights);
    }

    private BigDecimal sum(long reservationId, ExtraType type) {
        return extras.findByReservation(reservationId, type).stream()
                .map(ReservationExtra::unitPrice).reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}
