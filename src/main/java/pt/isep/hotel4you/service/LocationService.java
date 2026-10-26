package pt.isep.hotel4you.service;

import java.util.List;
import java.util.Optional;
import pt.isep.hotel4you.data.FloorRepository;
import pt.isep.hotel4you.data.LocationRepository;
import pt.isep.hotel4you.domain.Floor;
import pt.isep.hotel4you.domain.Location;
import pt.isep.hotel4you.domain.Manager;
import pt.isep.hotel4you.domain.User;

/** Shows the floors of the hotel and manages their zones. Only a manager may use it. */
public class LocationService {

    private static final int MAX_NAME_LENGTH = 50;

    private final FloorRepository floorRepository;
    private final LocationRepository locationRepository;

    /**
     * Creates the service.
     *
     * @param floorRepository the floor store
     * @param locationRepository the zone store
     */
    public LocationService(FloorRepository floorRepository, LocationRepository locationRepository) {
        this.floorRepository = floorRepository;
        this.locationRepository = locationRepository;
    }

    /**
     * Tells if the user may view the floors. Only a manager may. The GUI also uses this method
     * to show or hide the floors button.
     *
     * @param actor the logged-in user, can be null
     * @return true if the actor is a manager
     */
    public boolean canViewFloors(User actor) {
        return actor instanceof Manager;
    }

    /**
     * Lists all floors with their zones.
     *
     * @param actor the logged-in user who asks
     * @return the floors ordered by number
     * @throws AccessDeniedException if the actor is not a manager
     * @throws IllegalStateException if the database is not available
     */
    public List<Floor> listFloors(User actor) {
        checkAccess(actor);
        return floorRepository.findAll();
    }

    /**
     * Finds one floor with its zones.
     *
     * @param number the floor number
     * @param actor the logged-in user who asks
     * @return the floor
     * @throws AccessDeniedException if the actor is not a manager
     * @throws FloorNotFoundException if the floor does not exist
     * @throws IllegalStateException if the database is not available
     */
    public Floor findFloor(int number, User actor) {
        checkAccess(actor);
        return floorRepository.findByNumber(number).orElseThrow(FloorNotFoundException::new);
    }

    /**
     * Creates a zone on a floor.
     *
     * @param floorNumber the floor number
     * @param name the zone name, the service trims it
     * @param guestCardAccess true if the guest card opens the zone
     * @param requiresReservation true if the zone needs a reservation
     * @param actor the logged-in user who asks
     * @return the new zone with its id
     * @throws AccessDeniedException if the actor is not a manager
     * @throws IllegalArgumentException if the name is blank or longer than 50 characters
     * @throws FloorNotFoundException if the floor does not exist
     * @throws LocationNameTakenException if the floor already has a zone with this name
     * @throws IllegalStateException if the database is not available
     */
    public Location createLocation(int floorNumber, String name, boolean guestCardAccess,
            boolean requiresReservation, User actor) {
        checkManage(actor);
        String cleanName = checkName(name);
        checkFloor(floorNumber);
        checkNameFree(floorNumber, cleanName, null);
        return locationRepository.save(floorNumber,
                new Location(null, cleanName, guestCardAccess, requiresReservation));
    }

    /**
     * Changes a zone.
     *
     * @param id the zone id
     * @param floorNumber the new floor number
     * @param name the new zone name, the service trims it
     * @param guestCardAccess true if the guest card opens the zone
     * @param requiresReservation true if the zone needs a reservation
     * @param actor the logged-in user who asks
     * @return the changed zone
     * @throws AccessDeniedException if the actor is not a manager
     * @throws IllegalArgumentException if the name is blank or longer than 50 characters
     * @throws FloorNotFoundException if the floor does not exist
     * @throws LocationNotFoundException if the zone does not exist
     * @throws LocationNameTakenException if the floor has another zone with this name
     * @throws IllegalStateException if the database is not available
     */
    public Location editLocation(long id, int floorNumber, String name, boolean guestCardAccess,
            boolean requiresReservation, User actor) {
        checkManage(actor);
        String cleanName = checkName(name);
        checkFloor(floorNumber);
        locationRepository.findById(id).orElseThrow(LocationNotFoundException::new);
        checkNameFree(floorNumber, cleanName, id);
        Location location = new Location(id, cleanName, guestCardAccess, requiresReservation);
        locationRepository.update(floorNumber, location);
        return location;
    }

    /**
     * Removes a zone.
     *
     * @param id the zone id
     * @param actor the logged-in user who asks
     * @throws AccessDeniedException if the actor is not a manager
     * @throws LocationNotFoundException if the zone does not exist
     * @throws LocationInUseException if other data still uses the zone
     * @throws IllegalStateException if the database is not available
     */
    public void removeLocation(long id, User actor) {
        checkManage(actor);
        locationRepository.findById(id).orElseThrow(LocationNotFoundException::new);
        if (!locationRepository.delete(id)) {
            throw new LocationInUseException();
        }
    }

    private void checkManage(User actor) {
        if (!canViewFloors(actor)) {
            throw new AccessDeniedException("Só o gestor pode gerir as zonas.");
        }
    }

    private static String checkName(String name) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Escreva o nome da zona.");
        }
        String cleanName = name.trim();
        if (cleanName.length() > MAX_NAME_LENGTH) {
            throw new IllegalArgumentException("O nome da zona tem no máximo 50 caracteres.");
        }
        return cleanName;
    }

    private void checkFloor(int floorNumber) {
        floorRepository.findByNumber(floorNumber).orElseThrow(FloorNotFoundException::new);
    }

    /** Throws if another zone (not the one with {@code ownId}) has this name on the floor. */
    private void checkNameFree(int floorNumber, String name, Long ownId) {
        Optional<Location> same = locationRepository.findByFloorAndName(floorNumber, name);
        if (same.isPresent() && !same.get().getId().equals(ownId)) {
            throw new LocationNameTakenException();
        }
    }

    private void checkAccess(User actor) {
        if (!canViewFloors(actor)) {
            throw new AccessDeniedException("Só o gestor pode consultar os pisos.");
        }
    }
}
