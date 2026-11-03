package pt.isep.hotel4you.domain;

import java.util.List;
import java.util.Objects;

/**
 * Availability response for the future screen, without JavaFX dependencies.
 * @param rooms the free rooms
 * @param message the no-room message, or an empty string on success
 */
public record RoomSearchResult(List<Room> rooms, String message) {
    /**
     * Keeps the response immutable.
     * @param rooms the free rooms
     * @param message the display message
     */
    public RoomSearchResult {
        rooms = List.copyOf(rooms);
        Objects.requireNonNull(message);
    }
}
