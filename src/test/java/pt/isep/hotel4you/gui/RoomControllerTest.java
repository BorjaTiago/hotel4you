package pt.isep.hotel4you.gui;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.math.BigDecimal;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import pt.isep.hotel4you.domain.Room;
import pt.isep.hotel4you.domain.RoomSummary;

/** Tests the small helpers of the Quartos screen that run without a JavaFX toolkit. */
class RoomControllerTest {

    @Test
    @Tag("H4Y-91-AC-1")
    void onToRoom_keepIdNumberFloorAndType_whenRowIsChosen() {
        RoomSummary row = new RoomSummary(7L, 201, 2, "Segundo piso", "suite",
                new BigDecimal("120.00"));

        Room room = RoomController.toRoom(row);

        assertEquals(7L, room.getId());
        assertEquals(201, room.getNumber());
        assertEquals(2, room.getFloorNumber());
        assertEquals("suite", room.getRoomTypeName());
    }
}
