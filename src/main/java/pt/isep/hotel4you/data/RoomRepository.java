package pt.isep.hotel4you.data;

import java.util.List;
import pt.isep.hotel4you.domain.Room;

/** Stores and reads rooms. */
public interface RoomRepository {

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
     * @throws IllegalArgumentException if the floor or the room type does not exist
     * @throws DuplicateRoomException if the room number is already in use
     * @throws RoomLimitExceededException if the floor is full
     */
    Room save(Room room, int floorLimit);
}
