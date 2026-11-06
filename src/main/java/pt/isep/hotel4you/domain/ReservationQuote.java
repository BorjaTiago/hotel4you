package pt.isep.hotel4you.domain;

import java.math.BigDecimal;

/**
 * The price summary that the receptionist sees before confirming a reservation.
 *
 * @param pricePerNight the base price per night of the room type, or null if it is not set yet
 * @param nights the number of nights
 */
public record ReservationQuote(BigDecimal pricePerNight, long nights) {
}
