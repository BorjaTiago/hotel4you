package pt.isep.hotel4you.domain;

import java.time.LocalDate;
import java.util.Objects;

/**
 * One line of the active reservation list of the hotel, with the client and the visible room number.
 *
 * @param id the reservation identifier
 * @param clientName the name of the client
 * @param document the identity document of the client
 * @param roomNumber the visible room number
 * @param startDate the first occupied night
 * @param endDate the departure date
 * @param status the reservation state
 */
public record ActiveReservation(long id, String clientName, String document, int roomNumber,
        LocalDate startDate, LocalDate endDate, ReservationStatus status) {
    /**
     * Checks that the text, the dates and the state are present.
     *
     * @param id the reservation identifier
     * @param clientName the name of the client
     * @param document the identity document of the client
     * @param roomNumber the visible room number
     * @param startDate the first occupied night
     * @param endDate the departure date
     * @param status the reservation state
     */
    public ActiveReservation {
        Objects.requireNonNull(clientName);
        Objects.requireNonNull(document);
        Objects.requireNonNull(startDate);
        Objects.requireNonNull(endDate);
        Objects.requireNonNull(status);
    }
}
