package pt.isep.hotel4you.data;

import java.util.Optional;
import pt.isep.hotel4you.domain.Location;

/** Stores and finds the zones of the hotel. */
public interface LocationRepository {

    /**
     * Finds a zone by id.
     *
     * @param id the zone id
     * @return the zone, or empty if it does not exist
     * @throws IllegalStateException if the database is not available
     */
    Optional<Location> findById(long id);

    /**
     * Finds a zone by floor and name. The name match is not case sensitive.
     *
     * @param floorNumber the floor number
     * @param name the zone name
     * @return the zone, or empty if the floor has no zone with this name
     * @throws IllegalStateException if the database is not available
     */
    Optional<Location> findByFloorAndName(int floorNumber, String name);

    /**
     * Saves a new zone on a floor.
     *
     * @param floorNumber the number of the floor of the zone
     * @param location the new zone, its id is ignored
     * @return the saved zone with its new id
     * @throws IllegalStateException if the database is not available
     */
    Location save(int floorNumber, Location location);

    /**
     * Changes the name, floor and flags of a saved zone.
     *
     * @param floorNumber the number of the floor of the zone
     * @param location the zone with its id and new values
     * @throws IllegalStateException if the database is not available
     */
    void update(int floorNumber, Location location);

    /**
     * Deletes a zone.
     *
     * @param id the zone id
     * @return true if the zone was deleted, false if other data still uses it
     * @throws IllegalStateException if the database is not available
     */
    boolean delete(long id);
}
