package pt.isep.hotel4you.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.time.LocalDate;
import java.util.List;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import pt.isep.hotel4you.data.InMemoryReservationRepository;
import pt.isep.hotel4you.data.InMemoryRoomRepository;
import pt.isep.hotel4you.data.InMemoryRoomTypeRepository;
import pt.isep.hotel4you.domain.Receptionist;
import pt.isep.hotel4you.domain.Room;
import pt.isep.hotel4you.domain.RoomType;
import pt.isep.hotel4you.domain.User;

/** Tests the API that the separate free-room screen can call. */
class RoomAvailabilityFacadeTest {
    @Test
    @Tag("H4Y-54-AC-1")
    @Tag("H4Y-54-AC-4")
    void onSearchRooms_returnFreeRoom_whenFacadeReceivesValidRequest() {
        var rooms = new InMemoryRoomRepository();
        rooms.addFloor(2);
        rooms.addRoomType("duplo");
        Room room = rooms.save(new Room(null, 202, 2, "duplo"), 10);
        RoomType type = new RoomType(1L, "duplo", 2, null);
        var types = new InMemoryRoomTypeRepository();
        types.add(type);
        var service = new RoomService(rooms, types, new InMemoryReservationRepository());
        var facade = new HotelFacade(null, null, null, service, null);
        User actor = new Receptionist(1L, "Ana", "ana", "hash");
        LocalDate start = LocalDate.of(2026, 11, 10);

        assertEquals(List.of(room), facade.searchRooms(start, start.plusDays(1), type, actor));
        assertEquals(List.of(type), facade.listSearchRoomTypes(actor));
        assertEquals("", facade.searchRoomsWithMessage(start, start.plusDays(1), type, actor).message());
        assertThrows(IllegalArgumentException.class,
                () -> facade.searchRooms(start, start, type, actor));
        assertThrows(AccessDeniedException.class,
                () -> facade.searchRooms(start, start.plusDays(1), type, null));
    }
}
