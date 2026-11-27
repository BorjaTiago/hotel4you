package pt.isep.hotel4you.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.math.BigDecimal;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.junit.jupiter.params.provider.ValueSource;
import pt.isep.hotel4you.data.InMemoryRoomServiceRequestRepository;
import pt.isep.hotel4you.domain.Manager;
import pt.isep.hotel4you.domain.Receptionist;
import pt.isep.hotel4you.domain.RestaurantStaff;
import pt.isep.hotel4you.domain.RoomServiceRequest;
import pt.isep.hotel4you.domain.RoomServiceType;
import pt.isep.hotel4you.domain.User;

/** Tests the room service request rules (H4Y-110, RF-15). */
class RoomServiceRequestServiceTest {
    private static final User RECEPTIONIST = new Receptionist(2L, "Ana Silva", "ana", "hash");

    private InMemoryRoomServiceRequestRepository repository;
    private RoomServiceRequestService service;

    @BeforeEach
    void setUp() {
        repository = new InMemoryRoomServiceRequestRepository();
        repository.checkIn(202, 9L);
        service = new RoomServiceRequestService(repository);
    }

    @ParameterizedTest
    @EnumSource(RoomServiceType.class)
    @Tag("H4Y-110-AC-1")
    void onRegister_saveRoomTypeAndTime_whenRoomHasCheckIn(RoomServiceType type) {
        RoomServiceRequest request = service.register("202", type, RECEPTIONIST);

        assertEquals(202, request.getRoomNumber());
        assertEquals(9L, request.getReservationId());
        assertEquals(type, request.getType());
        assertEquals(new BigDecimal("4.50"), request.getUnitPrice());
        assertEquals(InMemoryRoomServiceRequestRepository.NOW, request.getRequestedAt());
        assertEquals(1, repository.saved().size());
    }

    @Test
    @Tag("H4Y-110-AC-1")
    void onRegister_acceptSpaces_whenRoomNumberHasSpaces() {
        assertEquals(202, service.register(" 202 ", RoomServiceType.FOOD, RECEPTIONIST).getRoomNumber());
    }

    @Test
    @Tag("H4Y-110-AC-2")
    void onRegister_throwNotOccupied_whenRoomHasNoCheckIn() {
        RoomNotOccupiedException error = assertThrows(RoomNotOccupiedException.class,
                () -> service.register("203", RoomServiceType.FOOD, RECEPTIONIST));

        assertEquals("O quarto 203 não está ocupado. Só um quarto com check-in pode pedir serviço de quarto.",
                error.getMessage());
        assertTrue(repository.saved().isEmpty());
    }

    @Test
    @Tag("H4Y-110-AC-3")
    void onRegister_throwAndSaveNothing_whenTypeIsMissing() {
        IllegalArgumentException error = assertThrows(IllegalArgumentException.class,
                () -> service.register("202", null, RECEPTIONIST));

        assertEquals("Escolha o tipo de serviço: comida, bebida ou lavandaria.", error.getMessage());
        assertTrue(repository.saved().isEmpty());
    }

    @ParameterizedTest
    @ValueSource(strings = {"", "  "})
    @Tag("H4Y-110-AC-2")
    void onRegister_throwAndSaveNothing_whenRoomNumberIsBlank(String roomNumber) {
        IllegalArgumentException error = assertThrows(IllegalArgumentException.class,
                () -> service.register(roomNumber, RoomServiceType.FOOD, RECEPTIONIST));

        assertEquals("Indique o número do quarto.", error.getMessage());
        assertTrue(repository.saved().isEmpty());
    }

    @Test
    @Tag("H4Y-110-AC-2")
    void onRegister_throwAndSaveNothing_whenRoomNumberIsNull() {
        assertThrows(IllegalArgumentException.class,
                () -> service.register(null, RoomServiceType.FOOD, RECEPTIONIST));
        assertTrue(repository.saved().isEmpty());
    }

    @ParameterizedTest
    @ValueSource(strings = {"abc", "-202", "2.02", "99999", "123456"})
    @Tag("H4Y-110-AC-2")
    void onRegister_throwAndSaveNothing_whenRoomNumberIsInvalid(String roomNumber) {
        IllegalArgumentException error = assertThrows(IllegalArgumentException.class,
                () -> service.register(roomNumber, RoomServiceType.FOOD, RECEPTIONIST));

        assertEquals("O número do quarto é inválido.", error.getMessage());
        assertTrue(repository.saved().isEmpty());
    }

    @Test
    @Tag("H4Y-110-AC-4")
    void onRegister_throwAccessDenied_whenActorIsRestaurantStaff() {
        User actor = new RestaurantStaff(3L, "Joao Lima", "joao", "hash");

        AccessDeniedException error = assertThrows(AccessDeniedException.class,
                () -> service.register("202", RoomServiceType.FOOD, actor));

        assertEquals("Não tem permissão para registar pedidos de serviço de quarto.", error.getMessage());
        assertTrue(repository.saved().isEmpty());
    }

    @Test
    @Tag("H4Y-110-AC-4")
    void onRegister_throwAccessDenied_whenNoUserIsLoggedIn() {
        assertThrows(AccessDeniedException.class,
                () -> service.register("202", RoomServiceType.FOOD, null));
        assertTrue(repository.saved().isEmpty());
    }

    @Test
    @Tag("H4Y-110-AC-4")
    void onRegister_checkPermissionFirst_whenDataIsAlsoInvalid() {
        User actor = new RestaurantStaff(3L, "Joao Lima", "joao", "hash");

        assertThrows(AccessDeniedException.class, () -> service.register("", null, actor));
    }

    @Test
    @Tag("H4Y-110-AC-1")
    void onRegister_saveRequest_whenActorIsManager() {
        User manager = new Manager(1L, "Rui Costa", "rui", "hash");

        assertEquals(202, service.register("202", RoomServiceType.LAUNDRY, manager).getRoomNumber());
    }
}
