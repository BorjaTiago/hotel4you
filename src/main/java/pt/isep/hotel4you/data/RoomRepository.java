package pt.isep.hotel4you.data;

import java.util.List;
import pt.isep.hotel4you.domain.Room;

/** Stores rooms and reads the floor and room type catalogues. */
public interface RoomRepository {

    /**
     * Checks if a floor exists.
     *
     * @param floorNumber the floor number
     * @return true if the floor exists
     */
    boolean floorExists(int floorNumber);

    /**
     * Checks if a room type exists.
     *
     * @param roomTypeName the room type name
     * @return true if the room type exists
     */
    boolean roomTypeExists(String roomTypeName);

    /**
     * Returns the room type names from the catalogue.
     *
     * @return an immutable list of room type names
     */
    List<String> findRoomTypeNames();

    /**
     * Checks if a visible room number is already in use.
     *
     * @param roomNumber the visible room number
     * @return true if the number exists
     */
    boolean existsByNumber(int roomNumber);

    /**
     * Counts rooms on a floor.
     *
     * @param floorNumber the floor number
     * @return the number of rooms on the floor
     */
    int countByFloor(int floorNumber);

    /**
     * Returns all rooms.
     *
     * @return an immutable list of rooms
     */
    List<Room> findAll();

    /**
     * Saves a room only while the floor is below the limit. The check and save must be atomic.
     *
     * @param room the room to save
     * @param floorLimit the maximum number of rooms on the floor
     * @return the saved room with its identifier
     * @throws DuplicateRoomException if the room number is already in use
     * @throws RoomLimitExceededException if the floor is full
     */
    Room save(Room room, int floorLimit);
}
