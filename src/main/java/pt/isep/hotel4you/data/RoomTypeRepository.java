package pt.isep.hotel4you.data;

import java.math.BigDecimal;
import java.util.List;
import pt.isep.hotel4you.domain.RoomType;

/** Finds room types and saves their base price. */
public interface RoomTypeRepository {

    /**
     * Finds all room types.
     *
     * @return the room types ordered by id, or an empty list if none exists
     * @throws IllegalStateException if the database is not available
     */
    List<RoomType> findAll();

    /**
     * Saves the base price of a room type.
     *
     * @param id the room type id
     * @param price the new base price
     * @return true if the room type was updated, false if no room type has this id
     * @throws IllegalStateException if the database is not available
     */
    boolean updateBasePrice(long id, BigDecimal price);
}
