package pt.isep.hotel4you.domain;

import java.math.BigDecimal;

/**
 * The price of a saved reservation.
 * @param basePrice the current base price per night, or null if unset
 * @param extrasPerNight the sum of the saved room extra prices
 * @param servicesPerNight the sum of the saved paid service prices
 * @param nights the number of nights in the reservation
 */
public record ReservationPrice(BigDecimal basePrice, BigDecimal extrasPerNight,
        BigDecimal servicesPerNight, long nights) {

    /**
     * Adds the base price and the included nightly charges.
     * @return the price per night, or null if the base price is unset
     */
    public BigDecimal pricePerNight() {
        return basePrice == null ? null : basePrice.add(extrasPerNight).add(servicesPerNight);
    }

    /**
     * Multiplies the price per night by the nights in the reservation.
     * @return the total stay price, or null if the base price is unset
     */
    public BigDecimal total() {
        BigDecimal nightly = pricePerNight();
        return nightly == null ? null : nightly.multiply(BigDecimal.valueOf(nights));
    }
}
