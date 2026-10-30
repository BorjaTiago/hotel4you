package pt.isep.hotel4you.domain;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

/** Tests the room entity and its SMALLINT number bounds. */
@Tag("H4Y-25-AC-1")
class RoomTest {

    @Test
    void onCreateRoom_keepFields_whenRoomIsValid() {
        Room room = new Room(12L, 202, 2, " duplo ");

        assertFalse(Room.class.isRecord());
        assertEquals(12L, room.getId());
        assertEquals(202, room.getNumber());
        assertEquals(2, room.getFloorNumber());
        assertEquals("duplo", room.getRoomTypeName());
    }

    @ParameterizedTest
    @ValueSource(ints = {1, Short.MAX_VALUE})
    void onCreateRoom_acceptNumber_whenAtSchemaBoundary(int number) {
        Room room = new Room(null, number, 3, "familiar");

        assertNull(room.getId());
        assertEquals(number, room.getNumber());
        assertEquals(Short.MAX_VALUE, Room.MAX_NUMBER);
    }

    @ParameterizedTest
    @ValueSource(ints = {Integer.MIN_VALUE, -1, 0, 32768, Integer.MAX_VALUE})
    void onCreateRoom_throw_whenNumberIsOutsideSchemaRange(int number) {
        assertThrows(IllegalArgumentException.class,
                () -> new Room(null, number, 2, "individual"));
    }

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {" ", "\t", "\n"})
    void onCreateRoom_throw_whenTypeIsBlank(String type) {
        assertThrows(IllegalArgumentException.class, () -> new Room(null, 202, 2, type));
    }
}
