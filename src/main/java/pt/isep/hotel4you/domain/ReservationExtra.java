package pt.isep.hotel4you.domain;

import java.math.BigDecimal;

/**
 * An extra saved in a reservation at the price agreed when it was added.
 *
 * @param extraPriceId the identifier of the entry in the price catalogue
 * @param name the name shown to the receptionist
 * @param unitPrice the saved charge per night
 */
public record ReservationExtra(long extraPriceId, String name, BigDecimal unitPrice) {
}
