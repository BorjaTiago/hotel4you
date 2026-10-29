package pt.isep.hotel4you.service;

import java.util.List;
import java.util.Map;
import pt.isep.hotel4you.data.DuplicateRoomException;
import pt.isep.hotel4you.data.RoomLimitExceededException;
import pt.isep.hotel4you.data.RoomRepository;
import pt.isep.hotel4you.domain.Permission;
import pt.isep.hotel4you.domain.Room;
import pt.isep.hotel4you.domain.User;

/** Applies room registration rules. */
public class RoomService {

    private final RoomRepository roomRepository;
    private final Map<Integer, Integer> roomFloorLimits;

    /**
     * Creates the service.
     *
     * @param roomRepository the room store
     * @param roomFloorLimits the room floors and their positive room limits
     */
    public RoomService(RoomRepository roomRepository, Map<Integer, Integer> roomFloorLimits) {
        this.roomRepository = roomRepository;
        this.roomFloorLimits = Map.copyOf(roomFloorLimits);
        if (this.roomFloorLimits.values().stream().anyMatch(limit -> limit < 1)) {
            throw new IllegalArgumentException("O limite de quartos deve ser positivo.");
        }
    }

    /**
     * Returns the configured room floors and limits for the screen.
     *
     * @return an immutable map from floor number to maximum room count
     */
    public Map<Integer, Integer> roomFloorLimits() {
        return roomFloorLimits;
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
     * @throws RoomLimitExceededException if the floor has reached its configured limit
     */
    public synchronized Room saveRoom(Room room, User actor) {
        if (!Permission.MANAGE_ROOMS.allows(actor)) {
            throw new AccessDeniedException("Só o gestor pode registar quartos.");
        }
        if (room == null) {
            throw new IllegalArgumentException("Indique os dados do quarto.");
        }
        if (!roomRepository.floorExists(room.floorNumber())) {
            throw new IllegalArgumentException("O piso indicado não existe.");
        }
        Integer floorLimit = roomFloorLimits.get(room.floorNumber());
        if (floorLimit == null) {
            throw new IllegalArgumentException("O piso indicado não está configurado para quartos.");
        }
        if (!roomRepository.roomTypeExists(room.roomTypeName())) {
            throw new IllegalArgumentException("O tipo de quarto indicado não existe.");
        }
        if (roomRepository.existsByNumber(room.number())) {
            throw new DuplicateRoomException();
        }
        if (roomRepository.countByFloor(room.floorNumber()) >= floorLimit) {
            throw new RoomLimitExceededException(floorLimit);
        }
        return roomRepository.save(room, floorLimit);
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
