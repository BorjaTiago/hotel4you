package pt.isep.hotel4you.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import pt.isep.hotel4you.data.InMemoryItemRepository;
import pt.isep.hotel4you.data.InMemoryReservationRepository;
import pt.isep.hotel4you.data.InMemoryRoomRepository;
import pt.isep.hotel4you.data.InMemoryRoomTypeRepository;
import pt.isep.hotel4you.domain.Manager;
import pt.isep.hotel4you.domain.Receptionist;
import pt.isep.hotel4you.domain.Room;
import pt.isep.hotel4you.domain.RoomItem;
import pt.isep.hotel4you.domain.StockItem;
import pt.isep.hotel4you.domain.User;

/** Tests the items that a manager configures in a room. */
class RoomServiceItemTest {

    private static final String HASH = "$2a$12$abcdefghijklmnopqrstuuAbCdEfGhIjKlMnOpQrStUvWxYz01234";
    private static final String INVALID_QUANTITY = "A quantidade é inválida.";

    private InMemoryRoomRepository rooms;
    private InMemoryItemRepository items;
    private RoomService service;
    private Room room;
    private StockItem towels;
    private StockItem sofa;

    @BeforeEach
    void setUp() {
        rooms = new InMemoryRoomRepository();
        rooms.addFloor(2);
        rooms.addRoomType("duplo");
        items = new InMemoryItemRepository();
        service = new RoomService(rooms, new InMemoryRoomTypeRepository(),
                new InMemoryReservationRepository(), items);
        room = rooms.save(new Room(null, 201, 2, "duplo"), 10);
        towels = items.save(new StockItem(null, "toalhas", "un", 20));
        sofa = items.save(new StockItem(null, "sofá", "un", 5));
    }

    @Test
    @Tag("H4Y-91-AC-1")
    void onConfigureItem_addItem_whenItemInCatalogue() {
        service.configureItem(room, towels, 2, manager());

        List<RoomItem> roomItems = service.listRoomItems(room, manager());
        assertEquals(1, roomItems.size());
        assertEquals(towels.getId(), roomItems.get(0).getItem().getId());
        assertEquals(2, roomItems.get(0).getQuantity());
    }

    @Test
    @Tag("H4Y-91-AC-2")
    void onRemoveItem_removeItem_whenRoomHasItem() {
        service.configureItem(room, sofa, 1, manager());
        service.configureItem(room, towels, 2, manager());

        service.removeItem(room, sofa, manager());

        List<RoomItem> roomItems = service.listRoomItems(room, manager());
        assertEquals(1, roomItems.size());
        assertEquals(towels.getId(), roomItems.get(0).getItem().getId());
    }

    @Test
    @Tag("H4Y-91-AC-3")
    void onConfigureItem_throw_whenRoomDoesNotExist() {
        Room missing = new Room(99L, 999, 2, "duplo");

        IllegalArgumentException error = assertThrows(IllegalArgumentException.class,
                () -> service.configureItem(missing, towels, 2, manager()));

        assertEquals("O quarto não existe.", error.getMessage());
        assertTrue(rooms.findItems(99L).isEmpty());
    }

    @Test
    @Tag("H4Y-91-AC-3")
    void onRemoveItem_throw_whenRoomDoesNotExist() {
        Room missing = new Room(99L, 999, 2, "duplo");

        IllegalArgumentException error = assertThrows(IllegalArgumentException.class,
                () -> service.removeItem(missing, towels, manager()));

        assertEquals("O quarto não existe.", error.getMessage());
    }

    @Test
    @Tag("H4Y-91-AC-4")
    void onRemoveItem_throw_whenItemHasAssociations() {
        service.configureItem(room, sofa, 1, manager());
        rooms.markItemInUse(room.getId(), sofa.getId());

        RoomItemInUseException error = assertThrows(RoomItemInUseException.class,
                () -> service.removeItem(room, sofa, manager()));

        assertEquals("O item tem associações e não pode ser removido.", error.getMessage());
        assertEquals(1, service.listRoomItems(room, manager()).size());
    }

    @Test
    @Tag("H4Y-91-AC-5")
    void onConfigureItem_throw_whenActorIsReceptionist() {
        assertThrows(AccessDeniedException.class,
                () -> service.configureItem(room, towels, 2, receptionist()));

        assertTrue(rooms.findItems(room.getId()).isEmpty());
    }

    @Test
    @Tag("H4Y-91-AC-5")
    void onRemoveItem_throw_whenActorIsReceptionist() {
        service.configureItem(room, sofa, 1, manager());

        assertThrows(AccessDeniedException.class,
                () -> service.removeItem(room, sofa, receptionist()));

        assertEquals(1, rooms.findItems(room.getId()).size());
    }

    @Test
    @Tag("H4Y-91-AC-6")
    void onConfigureItem_changeQuantity_whenRoomHasItem() {
        service.configureItem(room, towels, 2, manager());

        service.configureItem(room, towels, 3, manager());

        List<RoomItem> roomItems = service.listRoomItems(room, manager());
        assertEquals(1, roomItems.size());
        assertEquals(3, roomItems.get(0).getQuantity());
    }

    @ParameterizedTest
    @ValueSource(ints = {0, -1, 256})
    @Tag("H4Y-91-AC-7")
    void onConfigureItem_throw_whenQuantityIsInvalid(int quantity) {
        service.configureItem(room, towels, 2, manager());

        IllegalArgumentException error = assertThrows(IllegalArgumentException.class,
                () -> service.configureItem(room, towels, quantity, manager()));

        assertEquals(INVALID_QUANTITY, error.getMessage());
        assertEquals(2, service.listRoomItems(room, manager()).get(0).getQuantity());
    }

    @Test
    @Tag("H4Y-91-AC-7")
    void onParseQuantity_throw_whenTextIsNotANumber() {
        IllegalArgumentException error = assertThrows(IllegalArgumentException.class,
                () -> service.parseQuantity("abc"));

        assertEquals(INVALID_QUANTITY, error.getMessage());
    }

    @ParameterizedTest
    @ValueSource(strings = {" ", "2.5"})
    @Tag("H4Y-91-AC-7")
    void onParseQuantity_throw_whenTextIsBlankOrDecimal(String text) {
        IllegalArgumentException error = assertThrows(IllegalArgumentException.class,
                () -> service.parseQuantity(text));

        assertEquals(INVALID_QUANTITY, error.getMessage());
    }

    @Test
    @Tag("H4Y-91-AC-7")
    void onParseQuantity_throw_whenTextIsNull() {
        assertThrows(IllegalArgumentException.class, () -> service.parseQuantity(null));
    }

    @Test
    @Tag("H4Y-91-AC-7")
    void onParseQuantity_returnNumber_whenTextIsAnInteger() {
        assertEquals(3, service.parseQuantity("3"));
    }

    @Test
    @Tag("H4Y-91-AC-8")
    void onConfigureItem_throw_whenItemNotInCatalogue() {
        service.configureItem(room, towels, 2, manager());
        StockItem piano = new StockItem(999L, "piano", "un", 1);

        IllegalArgumentException error = assertThrows(IllegalArgumentException.class,
                () -> service.configureItem(room, piano, 1, manager()));

        assertEquals("O item não existe no catálogo.", error.getMessage());
        assertEquals(1, service.listRoomItems(room, manager()).size());
    }

    @Test
    @Tag("H4Y-91-AC-8")
    void onRemoveItem_throw_whenItemIsNull() {
        IllegalArgumentException error = assertThrows(IllegalArgumentException.class,
                () -> service.removeItem(room, null, manager()));

        assertEquals("O item não existe no catálogo.", error.getMessage());
    }

    @Test
    @Tag("H4Y-91-AC-5")
    void onListRoomItems_throw_whenActorIsReceptionist() {
        assertThrows(AccessDeniedException.class, () -> service.listRoomItems(room, receptionist()));
    }

    @Test
    @Tag("H4Y-91-AC-3")
    void onListRoomItems_throw_whenRoomDoesNotExist() {
        Room missing = new Room(99L, 999, 2, "duplo");

        IllegalArgumentException error = assertThrows(IllegalArgumentException.class,
                () -> service.listRoomItems(missing, manager()));

        assertEquals("O quarto não existe.", error.getMessage());
    }

    @Test
    @Tag("H4Y-91-AC-7")
    void onParseQuantity_throw_whenNumberIsTooBig() {
        IllegalArgumentException error = assertThrows(IllegalArgumentException.class,
                () -> service.parseQuantity("99999999999"));

        assertEquals(INVALID_QUANTITY, error.getMessage());
    }

    private static User manager() {
        return new Manager(1L, "Rui Costa", "rui", HASH);
    }

    private static User receptionist() {
        return new Receptionist(2L, "Ana Silva", "ana", HASH);
    }
}
