package pt.isep.hotel4you.domain;

/** A zone of the hotel, for example a pool or a bar. */
public class Location {

    private final Long id;
    private final String name;
    private final boolean guestCardAccess;
    private final boolean requiresReservation;

    /**
     * Creates a zone.
     *
     * @param id the identifier, or null if the zone is not saved yet
     * @param name the zone name
     * @param guestCardAccess true if the guest card opens the zone
     * @param requiresReservation true if the zone needs a reservation
     */
    public Location(Long id, String name, boolean guestCardAccess, boolean requiresReservation) {
        this.id = id;
        this.name = name;
        this.guestCardAccess = guestCardAccess;
        this.requiresReservation = requiresReservation;
    }

    /**
     * Returns the identifier.
     *
     * @return the identifier, or null if the zone is not saved yet
     */
    public Long getId() {
        return id;
    }

    /**
     * Returns the zone name.
     *
     * @return the zone name
     */
    public String getName() {
        return name;
    }

    /**
     * Tells if the guest card opens the zone.
     *
     * @return true if the guest card opens the zone
     */
    public boolean isGuestCardAccess() {
        return guestCardAccess;
    }

    /**
     * Tells if the zone needs a reservation.
     *
     * @return true if the zone needs a reservation
     */
    public boolean isRequiresReservation() {
        return requiresReservation;
    }
}
