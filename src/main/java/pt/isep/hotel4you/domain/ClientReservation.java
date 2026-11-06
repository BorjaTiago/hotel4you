package pt.isep.hotel4you.domain;

import java.time.LocalDate;
import java.util.Objects;

/**
 * One line of the reservation list of a client, with the visible room number.
 *
 * @param id the reservation identifier
 * @param roomNumber the visible room number
 * @param startDate the first occupied night
 * @param endDate the departure date
 * @param status the reservation state
 */
public record ClientReservation(long id, int roomNumber, LocalDate startDate, LocalDate endDate,
        ReservationStatus status) {
    /**
     * Checks that the dates and the state are present.
     *
     * @param id the reservation identifier
     * @param roomNumber the visible room number
     * @param startDate the first occupied night
     * @param endDate the departure date
     * @param status the reservation state
     */
    public ClientReservation {
        Objects.requireNonNull(startDate);
        Objects.requireNonNull(endDate);
        Objects.requireNonNull(status);
    }
}
