package pt.isep.hotel4you.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import pt.isep.hotel4you.data.InMemoryRoomServiceRequestRepository;
import pt.isep.hotel4you.domain.Permission;
import pt.isep.hotel4you.domain.Receptionist;
import pt.isep.hotel4you.domain.RestaurantStaff;
import pt.isep.hotel4you.domain.RoomServiceType;
import pt.isep.hotel4you.domain.User;

/** Tests the room service request at the facade boundary (H4Y-110). */
class RoomServiceRequestFacadeTest {
    private final User receptionist = new Receptionist(2L, "Ana Silva", "ana", "hash");
    private final User restaurantStaff = new RestaurantStaff(3L, "Joao Lima", "joao", "hash");
    private InMemoryRoomServiceRequestRepository repository;
    private HotelFacade facade;

    @BeforeEach
    void setUp() {
        repository = new InMemoryRoomServiceRequestRepository();
        repository.checkIn(202, 9L);
        facade = new HotelFacade(null, null, null, null, null, null, null, null, null,
                new RoomServiceRequestService(repository));
    }

    @Test
    @Tag("H4Y-110-AC-1")
    void onRegisterRoomService_saveRequest_whenReceptionistRegisters() {
        assertEquals(RoomServiceType.FOOD,
                facade.registerRoomService("202", RoomServiceType.FOOD, receptionist).getType());
        assertEquals(1, repository.saved().size());
    }

    @Test
    @Tag("H4Y-110-AC-2")
    void onRegisterRoomService_throwNotOccupied_whenRoomHasNoCheckIn() {
        assertThrows(RoomNotOccupiedException.class,
                () -> facade.registerRoomService("203", RoomServiceType.FOOD, receptionist));
    }

    @Test
    @Tag("H4Y-110-AC-4")
    void onRegisterRoomService_throwAccessDenied_whenActorIsRestaurantStaff() {
        assertThrows(AccessDeniedException.class,
                () -> facade.registerRoomService("202", RoomServiceType.FOOD, restaurantStaff));
        assertTrue(repository.saved().isEmpty());
    }

    @Test
    @Tag("H4Y-110-AC-4")
    void onCanUse_showMenuOnlyToReception_whenRoomServicePermission() {
        assertTrue(facade.canUse(receptionist, Permission.REGISTER_ROOM_SERVICE));
        assertFalse(facade.canUse(restaurantStaff, Permission.REGISTER_ROOM_SERVICE));
    }
}
