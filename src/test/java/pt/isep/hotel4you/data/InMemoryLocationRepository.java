package pt.isep.hotel4you.data;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import pt.isep.hotel4you.domain.Location;

/** Keeps zones in memory. Unit tests use it instead of the database. */
public class InMemoryLocationRepository implements LocationRepository {

    private final Map<Long, Location> locations = new HashMap<>();
    private final Map<Long, Integer> floorNumbers = new HashMap<>();
    private final Set<Long> inUse = new HashSet<>();
    private long nextId = 1;

    @Override
    public Optional<Location> findById(long id) {
        return Optional.ofNullable(locations.get(id));
    }

    @Override
    public Optional<Location> findByFloorAndName(int floorNumber, String name) {
        return locations.values().stream()
                .filter(location -> floorNumbers.get(location.getId()) == floorNumber)
                .filter(location -> location.getName().equalsIgnoreCase(name))
                .findFirst();
    }

    @Override
    public Location save(int floorNumber, Location location) {
        Location saved = new Location(nextId++, location.getName(),
                location.isGuestCardAccess(), location.isRequiresReservation());
        locations.put(saved.getId(), saved);
        floorNumbers.put(saved.getId(), floorNumber);
        return saved;
    }

    @Override
    public void update(int floorNumber, Location location) {
        locations.put(location.getId(), location);
        floorNumbers.put(location.getId(), floorNumber);
    }

    @Override
    public boolean delete(long id) {
        if (inUse.contains(id)) {
            return false;
        }
        locations.remove(id);
        floorNumbers.remove(id);
        return true;
    }

    /**
     * Marks a zone as used by other data, so that delete refuses it.
     *
     * @param id the zone id
     */
    public void markInUse(long id) {
        inUse.add(id);
    }

    /**
     * Returns how many zones are stored.
     *
     * @return the number of zones
     */
    public int count() {
        return locations.size();
    }

    /**
     * Returns the floor number of a stored zone.
     *
     * @param id the zone id
     * @return the floor number
     */
    public int floorOf(long id) {
        return floorNumbers.get(id);
    }
}
