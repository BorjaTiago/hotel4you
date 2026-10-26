package pt.isep.hotel4you.domain;

/**
 * A zone of the hotel, for example a pool or a bar.
 *
 * @param id the identifier, or null if not saved yet
 * @param name the zone name
 * @param guestCardAccess true if the guest card opens the zone
 * @param requiresReservation true if the zone needs a reservation
 */
public record Location(Long id, String name, boolean guestCardAccess, boolean requiresReservation) {
}
