package pt.isep.hotel4you.data;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import pt.isep.hotel4you.domain.Floor;

/** Keeps floors in memory. Unit tests use it instead of the database. */
public class InMemoryFloorRepository implements FloorRepository {

    private final List<Floor> floors = new ArrayList<>();

    @Override
    public List<Floor> findAll() {
        return floors.stream().sorted(Comparator.comparingInt(Floor::number)).toList();
    }

    @Override
    public Optional<Floor> findByNumber(int number) {
        return floors.stream().filter(floor -> floor.number() == number).findFirst();
    }

    /**
     * Adds a floor to the store.
     *
     * @param floor the floor to add
     */
    public void add(Floor floor) {
        floors.add(floor);
    }
}
