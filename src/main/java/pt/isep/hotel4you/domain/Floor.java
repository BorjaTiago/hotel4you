package pt.isep.hotel4you.domain;

import java.util.List;

/**
 * A floor of the hotel with its zones.
 *
 * @param number the floor number, from -1 to 3
 * @param description the floor description
 * @param locations the zones of the floor, empty if none
 */
public record Floor(int number, String description, List<Location> locations) {

    /** Keeps an unmodifiable copy of the zones. */
    public Floor {
        locations = List.copyOf(locations);
    }
}
