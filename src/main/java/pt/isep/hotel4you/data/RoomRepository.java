package pt.isep.hotel4you.data;

import java.util.List;
import java.util.Optional;
import pt.isep.hotel4you.domain.Room;
import pt.isep.hotel4you.domain.RoomItem;
import pt.isep.hotel4you.domain.RoomSummary;

/** Stores and reads rooms. */
public interface RoomRepository {

    /**
     * Returns all rooms.
     *
     * @return an immutable list of rooms
     */
    List<Room> findAll();

    /**
     * Finds a room by id.
     *
     * @param id the room id
     * @return the room, or empty if it does not exist
     * @throws IllegalStateException if the database is not available
     */
    Optional<Room> findById(long id);

    /**
     * Returns all rooms with the floor, room type and base price, ordered by room number.
     *
     * @return an immutable list of room summaries
     */
    List<RoomSummary> findSummaries();

    /**
     * Saves a room only while the floor is below the limit. The check and save must be atomic.
     *
     * @param room the room to save
     * @param floorLimit the maximum number of rooms on the floor
     * @return the saved room with its identifier
     * @throws IllegalArgumentException if the floor or the room type does not exist
     * @throws DuplicateRoomException if the room number is already in use
     * @throws RoomLimitExceededException if the floor is full
     */
    Room save(Room room, int floorLimit);

    /**
     * Updates a room only while the other rooms on the target floor are below the limit. The
     * check and update must be atomic. The room itself does not count against the limit.
     *
     * @param room the room to update, with its identifier
     * @param floorLimit the maximum number of rooms on the floor
     * @return the saved room
     * @throws IllegalArgumentException if the room, the floor or the room type does not exist
     * @throws DuplicateRoomException if the room number is already in use
     * @throws RoomLimitExceededException if the floor is full
     */
    Room update(Room room, int floorLimit);

    /**
     * Deletes a room.
     *
     * @param id the room id
     * @return true if the room was deleted, false if other data still uses it
     * @throws IllegalArgumentException if the room does not exist
     * @throws IllegalStateException if the database is not available
     */
    boolean delete(long id);

    /**
     * Returns the items of a room with their quantities.
     *
     * @param roomId the room id
     * @return an immutable list of room items, empty if the room has none
     * @throws IllegalStateException if the database is not available
     */
    List<RoomItem> findItems(long roomId);

    /**
     * Sets the quantity of an item in a room. It adds the item or changes its quantity in one
     * statement.
     *
     * @param roomId the room id
     * @param itemId the item id
     * @param quantity the quantity in the room
     * @throws IllegalStateException if the database is not available
     */
    void saveItem(long roomId, long itemId, int quantity);

    /**
     * Removes an item from a room.
     * The refusal (false) is reserved for a later table that references room_items (open question Q-36).
     * Deleting a row that does not exist returns true.
     *
     * @param roomId the room id
     * @param itemId the item id
     * @return true if the item was removed, false if other data still uses it
     * @throws IllegalStateException if the database is not available
     */
    boolean deleteItem(long roomId, long itemId);
}
