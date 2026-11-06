package pt.isep.hotel4you.domain;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

/** A type of room, for example individual or duplo, with its base price per night. */
public class RoomType {

    private final Long id;
    private final String name;
    private final int capacity;
    private final BigDecimal basePrice;

    /**
     * Creates a room type.
     *
     * @param id the identifier, or null if the type is not saved yet
     * @param name the type name
     * @param capacity the number of guests
     * @param basePrice the base price per night, or null if not set
     */
    public RoomType(Long id, String name, int capacity, BigDecimal basePrice) {
        this.id = id;
        this.name = name;
        this.capacity = capacity;
        this.basePrice = basePrice;
    }

    /**
     * Returns the identifier.
     *
     * @return the identifier, or null if the type is not saved yet
     */
    public Long getId() {
        return id;
    }

    /**
     * Returns the type name.
     *
     * @return the type name
     */
    public String getName() {
        return name;
    }

    /**
     * Returns the number of guests.
     *
     * @return the capacity
     */
    public int getCapacity() {
        return capacity;
    }

    /**
     * Returns the base price per night.
     *
     * @return the base price, or null if not set
     */
    public BigDecimal getBasePrice() {
        return basePrice;
    }

    /**
     * Returns the total price for a period: the base price times the number of nights.
     *
     * @param start the first night
     * @param end the check-out date
     * @return the total price, or null if the base price is not set
     * @throws IllegalArgumentException if the period is not valid
     */
    public BigDecimal totalFor(LocalDate start, LocalDate end) {
        Reservation.validatePeriod(start, end);
        if (basePrice == null) {
            return null;
        }
        return basePrice.multiply(BigDecimal.valueOf(ChronoUnit.DAYS.between(start, end)));
    }
}
