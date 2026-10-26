package pt.isep.hotel4you.service;

import java.util.List;
import pt.isep.hotel4you.data.FloorRepository;
import pt.isep.hotel4you.domain.Floor;
import pt.isep.hotel4you.domain.Manager;
import pt.isep.hotel4you.domain.User;

/** Shows the floors of the hotel and their zones. Only a manager may use it. */
public class LocationService {

    private final FloorRepository floorRepository;

    /**
     * Creates the service.
     *
     * @param floorRepository the floor store
     */
    public LocationService(FloorRepository floorRepository) {
        this.floorRepository = floorRepository;
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

    private void checkAccess(User actor) {
        if (!canViewFloors(actor)) {
            throw new AccessDeniedException("Só o gestor pode consultar os pisos.");
        }
    }
}
