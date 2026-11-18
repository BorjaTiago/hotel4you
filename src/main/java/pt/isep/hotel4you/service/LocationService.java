package pt.isep.hotel4you.service;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import pt.isep.hotel4you.data.FloorRepository;
import pt.isep.hotel4you.data.LocationRepository;
import pt.isep.hotel4you.domain.Floor;
import pt.isep.hotel4you.domain.Location;
import pt.isep.hotel4you.domain.Permission;
import pt.isep.hotel4you.domain.User;

/**
 * Shows the floors of the hotel and manages their zones. Only a manager manages zones. It also
 * holds the access rule by zone for each role.
 */
public class LocationService {

    private static final int MAX_NAME_LENGTH = 50;

    /** The floors that a receptionist may enter (RF-24). */
    private static final Set<Integer> RECEPTION_FLOORS = Set.of(0, 1);

    /** The zones that a restaurant employee may enter (RF-25). */
    private static final List<String> RESTAURANT_ZONES = List.of("Cozinha", "Restaurante");

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

    /**
     * Tells if the user may enter a zone. A receptionist enters only the zones of floors 0 and 1
     * (RF-24). A restaurant employee enters only the kitchen and the restaurant (RF-25). A manager
     * enters every zone. Other roles do not log in (RF-01), so the rule refuses them. The rule
     * finds the floor of the zone by its id in the floor list.
     *
     * @param location the zone to enter, can be null
     * @param actor the logged-in user, can be null
     * @return true if the user may enter the zone; false if not, or if the zone or the user is
     *         null, or if a receptionist asks for a zone that is on no floor
     * @throws IllegalStateException if the database is not available
     */
    public boolean canAccess(Location location, User actor) {
        if (location == null || actor == null) {
            return false;
        }
        return switch (actor.getRole()) {
            case MANAGER -> true;
            case RECEPTIONIST -> floorOf(location).map(RECEPTION_FLOORS::contains).orElse(false);
            case RESTAURANT -> isRestaurantZone(location);
            default -> false;
        };
    }

    /** Finds the number of the floor that holds the zone, by the zone id. */
    private Optional<Integer> floorOf(Location location) {
        if (location.getId() == null) {
            return Optional.empty();
        }
        for (Floor floor : floorRepository.findAll()) {
            for (Location zone : floor.locations()) {
                if (location.getId().equals(zone.getId())) {
                    return Optional.of(floor.number());
                }
            }
        }
        return Optional.empty();
    }

    private static boolean isRestaurantZone(Location location) {
        String name = location.getName();
        if (name == null) {
            return false;
        }
        String cleanName = name.trim();
        for (String zone : RESTAURANT_ZONES) {
            if (zone.equalsIgnoreCase(cleanName)) {
                return true;
            }
        }
        return false;
    }

    private void checkManage(User actor) {
        if (!Permission.MANAGE_FLOORS.allows(actor)) {
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
        if (!Permission.MANAGE_FLOORS.allows(actor)) {
            throw new AccessDeniedException("Só o gestor pode consultar os pisos.");
        }
    }
}
