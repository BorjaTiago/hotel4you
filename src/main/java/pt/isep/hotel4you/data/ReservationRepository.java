package pt.isep.hotel4you.data;

import java.util.List;
import pt.isep.hotel4you.domain.ClientReservation;
import pt.isep.hotel4you.domain.Reservation;

/** Reads and saves room bookings. */
public interface ReservationRepository {
    /**
     * Reads all booking periods of a room, including cancelled bookings.
     * The domain owns the overlap and cancellation rule.
     * @param roomId the room identifier
     * @return the periods, or an empty list
     * @throws IllegalStateException if the database cannot be read
     */
    List<Reservation> findByRoom(long roomId);

    /**
     * Reads all booking periods of several rooms in one query, including cancelled bookings.
     * An empty id list gives an empty result and no query.
     * @param roomIds the room identifiers
     * @return the periods ordered by room and start date, or an empty list
     * @throws IllegalStateException if the database cannot be read
     */
    List<Reservation> findByRooms(List<Long> roomIds);

    /**
     * Saves a new booking. The service checks the rules before the call.
     * @param reservation the new booking, with client, room, period, state and creator
     * @return the saved booking with its id
     * @throws IllegalStateException if the database cannot save the booking
     */
    Reservation save(Reservation reservation);

    /**
     * Reads all bookings of a client, including cancelled bookings (history).
     * @param clientId the client identifier
     * @return the bookings with the room number, newest arrival first, or an empty list
     * @throws IllegalStateException if the database cannot be read
     */
    List<ClientReservation> findByClient(long clientId);
}
