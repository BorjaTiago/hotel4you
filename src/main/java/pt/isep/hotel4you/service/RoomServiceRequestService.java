package pt.isep.hotel4you.service;

import pt.isep.hotel4you.data.RoomServiceRequestRepository;
import pt.isep.hotel4you.domain.Permission;
import pt.isep.hotel4you.domain.Room;
import pt.isep.hotel4you.domain.RoomServiceRequest;
import pt.isep.hotel4you.domain.RoomServiceType;
import pt.isep.hotel4you.domain.User;

/** Applies the rules of the room service requests (RF-15, Q-33). */
public class RoomServiceRequestService {
    private final RoomServiceRequestRepository repository;

    /**
     * Creates the service.
     *
     * @param repository the room service request store
     */
    public RoomServiceRequestService(RoomServiceRequestRepository repository) {
        this.repository = repository;
    }

    /**
     * Registers a food, drink or laundry request for a room with check-in. Invalid data saves
     * nothing. The request does not open the room door (Q-08).
     *
     * @param roomNumber the room number as typed, for example "202"
     * @param type the service type
     * @param actor the logged-in user
     * @return the saved request with the date and time
     * @throws AccessDeniedException if the actor may not register room service requests
     * @throws IllegalArgumentException if the room number is missing or invalid, or the type is
     *         missing
     * @throws RoomNotOccupiedException if the room has no reservation with check-in
     * @throws IllegalStateException if the database is not available
     */
    public RoomServiceRequest register(String roomNumber, RoomServiceType type, User actor) {
        if (!Permission.REGISTER_ROOM_SERVICE.allows(actor)) {
            throw new AccessDeniedException("Não tem permissão para registar pedidos de serviço de quarto.");
        }
        int number = parseRoomNumber(roomNumber);
        if (type == null) {
            throw new IllegalArgumentException("Escolha o tipo de serviço: comida, bebida ou lavandaria.");
        }
        return repository.save(number, type).orElseThrow(() -> new RoomNotOccupiedException(number));
    }

    private static int parseRoomNumber(String text) {
        if (text == null || text.isBlank()) {
            throw new IllegalArgumentException("Indique o número do quarto.");
        }
        String value = text.strip();
        if (!value.matches("\\d{1,5}") || Integer.parseInt(value) > Room.MAX_NUMBER) {
            throw new IllegalArgumentException("O número do quarto é inválido.");
        }
        return Integer.parseInt(value);
    }
}
