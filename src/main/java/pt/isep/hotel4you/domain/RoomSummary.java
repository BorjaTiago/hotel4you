package pt.isep.hotel4you.domain;

import java.math.BigDecimal;

/**
 * One line of the room list: the room with its floor, room type and base price per night.
 *
 * @param number the visible room number
 * @param floorNumber the floor number
 * @param roomTypeName the room type name
 * @param basePrice the base price per night of the room type, or null if it is not set yet
 */
public record RoomSummary(int number, int floorNumber, String roomTypeName, BigDecimal basePrice) {
}
