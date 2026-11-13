package pt.isep.hotel4you.data;

import java.util.List;
import java.util.Optional;
import pt.isep.hotel4you.domain.ReservationSummary;
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
     * Saves a new booking. The service checks the rules before the call. The save checks
     * again that no other booking of the room overlaps the nights, in the same step as the insert,
     * so two saves at the same moment cannot both succeed.
     * @param reservation the new booking, with client, room, period, state and creator
     * @return the saved booking with its id
     * @throws RoomNotAvailableException if another booking of the room overlaps the nights
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

    /**
     * Reads one booking with its client, room, period, state and creator.
     * @param id the booking identifier
     * @return the booking, or empty if it does not exist
     * @throws IllegalStateException if the database cannot be read
     */
    Optional<Reservation> findById(long id);

    /**
     * Reads the active bookings: the states RESERVED and CHECKED_IN.
     * @return the bookings with the client and the room number, first arrival first, or an empty list
     * @throws IllegalStateException if the database cannot be read
     */
    List<ReservationSummary> findActive();

    /**
     * Saves the check-in and its time. It changes the row only if the state is still RESERVED,
     * in the same step as the update.
     * @param id the booking identifier
     * @return true if the row changed, false if the state was not RESERVED
     * @throws IllegalStateException if the database cannot save the check-in
     */
    boolean checkIn(long id);

    /**
     * Saves the check-out and its time. It changes the row only if the state is still CHECKED_IN,
     * in the same step as the update.
     * @param id the booking identifier
     * @return true if the row changed, false if the state was not CHECKED_IN
     * @throws IllegalStateException if the database cannot save the check-out
     */
    boolean checkOut(long id);

    /**
     * Cancels a booking. The row stays in the database (history). It changes the row only if the
     * state is still RESERVED, in the same step as the update.
     * @param id the booking identifier
     * @return true if the row changed, false if the state changed before or the row does not exist
     * @throws IllegalStateException if the database cannot save the cancel
     */
    boolean cancel(long id);

    /**
     * Deletes a booking for good. It deletes the row only if the state is RESERVED, in the same
     * step as the delete. A cancelled booking stays in the history.
     * @param id the booking identifier
     * @return true if the row was deleted, false if the state is not RESERVED or the row does not
     *         exist
     * @throws IllegalStateException if the database cannot delete the booking
     */
    boolean delete(long id);
}
