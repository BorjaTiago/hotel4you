package pt.isep.hotel4you.data;

import java.util.List;
import java.util.Optional;
import pt.isep.hotel4you.domain.Floor;

/** Finds the floors of the hotel with their zones. */
public interface FloorRepository {

    /**
     * Finds all floors, ordered by number.
     *
     * @return the floors, or an empty list if none exists
     */
    List<Floor> findAll();

    /**
     * Finds one floor.
     *
     * @param number the floor number
     * @return the floor, or empty if it does not exist
     */
    Optional<Floor> findByNumber(int number);
}
