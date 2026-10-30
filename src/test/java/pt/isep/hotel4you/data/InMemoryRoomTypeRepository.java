package pt.isep.hotel4you.data;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import pt.isep.hotel4you.domain.RoomType;

/** Keeps room types in memory. Unit tests use it instead of the database. */
public class InMemoryRoomTypeRepository implements RoomTypeRepository {

    private final List<RoomType> types = new ArrayList<>();

    @Override
    public List<RoomType> findAll() {
        return types.stream().sorted(Comparator.comparing(RoomType::getId)).toList();
    }

    @Override
    public boolean updateBasePrice(long id, BigDecimal price) {
        for (int i = 0; i < types.size(); i++) {
            RoomType type = types.get(i);
            if (type.getId() == id) {
                types.set(i, new RoomType(id, type.getName(), type.getCapacity(), price));
                return true;
            }
        }
        return false;
    }

    /**
     * Adds a room type to the store.
     *
     * @param type the room type to add
     */
    public void add(RoomType type) {
        types.add(type);
    }
}
