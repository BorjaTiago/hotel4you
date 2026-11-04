package pt.isep.hotel4you.data;

import java.util.List;
import pt.isep.hotel4you.domain.Reservation;

/** Reads persisted booking periods. Writing reservations is outside H4Y-54. */
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
}
