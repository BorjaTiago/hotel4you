package pt.isep.hotel4you.service;

import java.util.List;
import java.util.Set;
import pt.isep.hotel4you.data.DuplicateRoomException;
import pt.isep.hotel4you.data.RoomLimitExceededException;
import pt.isep.hotel4you.data.RoomRepository;
import pt.isep.hotel4you.domain.Manager;
import pt.isep.hotel4you.domain.Room;
import pt.isep.hotel4you.domain.User;

/** Applies room registration rules. */
public class RoomService {

    private static final Set<Integer> ROOM_FLOORS = Set.of(2, 3);
    private static final int ROOMS_PER_FLOOR = 10;

    private final RoomRepository roomRepository;

    /**
     * Creates the service.
     *
     * @param roomRepository the room store
     */
    public RoomService(RoomRepository roomRepository) {
        this.roomRepository = roomRepository;
    }

    /**
     * Tells if a user may register rooms.
     *
     * @param actor the logged-in user
     * @return true only for a manager
     */
    public boolean canRegisterRooms(User actor) {
        return actor instanceof Manager;
    }

    /**
     * Saves a room. Only a manager may register a room. Calls are serialized in this service, and
     * the repository checks the floor limit again in its save operation.
     *
     * @param room the room to save
     * @param actor the logged-in user
     * @return the saved room
     * @throws AccessDeniedException if the actor is not a manager
     * @throws IllegalArgumentException if the floor or room type is invalid
     * @throws DuplicateRoomException if the room number is already in use
     * @throws RoomLimitExceededException if the floor already has 10 rooms
     */
    public synchronized Room saveRoom(Room room, User actor) {
        if (!canRegisterRooms(actor)) {
            throw new AccessDeniedException("Só o gestor pode registar quartos.");
        }
        if (room == null) {
            throw new IllegalArgumentException("Indique os dados do quarto.");
        }
        if (!roomRepository.floorExists(room.floorNumber())) {
            throw new IllegalArgumentException("O piso indicado não existe.");
        }
        if (!ROOM_FLOORS.contains(room.floorNumber())) {
            throw new IllegalArgumentException("Só os pisos 2 e 3 têm quartos.");
        }
        if (!roomRepository.roomTypeExists(room.roomTypeName())) {
            throw new IllegalArgumentException("O tipo de quarto indicado não existe.");
        }
        if (roomRepository.existsByNumber(room.number())) {
            throw new DuplicateRoomException();
        }
        if (roomRepository.countByFloor(room.floorNumber()) >= ROOMS_PER_FLOOR) {
            throw new RoomLimitExceededException(ROOMS_PER_FLOOR);
        }
        return roomRepository.saveWithinFloorLimit(room, ROOMS_PER_FLOOR);
    }

    /**
     * Lists saved rooms.
     *
     * @return all saved rooms
     */
    public List<Room> listRooms() {
        return roomRepository.findAll();
    }

    /**
     * Lists the room types from the catalogue.
     *
     * @return all room type names
     */
    public List<String> listRoomTypes() {
        return roomRepository.findRoomTypeNames();
    }
}
