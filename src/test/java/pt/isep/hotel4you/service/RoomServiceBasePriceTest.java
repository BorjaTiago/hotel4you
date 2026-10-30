package pt.isep.hotel4you.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.math.BigDecimal;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;
import pt.isep.hotel4you.data.InMemoryRoomTypeRepository;
import pt.isep.hotel4you.domain.Manager;
import pt.isep.hotel4you.domain.Receptionist;
import pt.isep.hotel4you.domain.RestaurantStaff;
import pt.isep.hotel4you.domain.RoomType;
import pt.isep.hotel4you.domain.User;

/** Tests that the manager can set the base price per night of each room type. */
class RoomServiceBasePriceTest {

    private static final String PRICE_MESSAGE =
            "O preço tem de estar entre 0,00 e 999999,99, com duas casas decimais.";
    private static final String NUMBER_MESSAGE = "O preço tem de ser um número.";
    private static final String NOT_FOUND_MESSAGE = "O tipo de quarto indicado não existe.";
    private static final String TYPE_MESSAGE = "Indique o tipo de quarto.";
    private static final String SET_DENIED_MESSAGE = "Só o gestor pode definir os preços.";
    private static final String LIST_DENIED_MESSAGE = "Só o gestor pode consultar os preços.";

    private static final User MANAGER = new Manager(1L, "Rui Costa", "rui", "hash");
    private static final User RECEPTIONIST = new Receptionist(2L, "Ana Silva", "ana", "hash");
    private static final User RESTAURANT_STAFF = new RestaurantStaff(3L, "Joao Lima", "joao", "hash");

    private RoomService service;
    private RoomType individual;
    private RoomType duplo;

    @BeforeEach
    void setUp() {
        InMemoryRoomTypeRepository repository = new InMemoryRoomTypeRepository();
        individual = new RoomType(1L, "individual", 1, new BigDecimal("50.00"));
        duplo = new RoomType(2L, "duplo", 2, null);
        repository.add(individual);
        repository.add(duplo);
        service = new RoomService(repository);
    }

    private BigDecimal priceOf(String name) {
        return service.listBasePrices(MANAGER).stream()
                .filter(type -> type.getName().equals(name))
                .findFirst().orElseThrow().getBasePrice();
    }

    @Test
    @Tag("H4Y-26-AC-1")
    void onSetBasePrice_savePrice_whenManagerSetsPrice() {
        service.setBasePrice(duplo, new BigDecimal("80.00"), MANAGER);

        assertEquals(new BigDecimal("80.00"), priceOf("duplo"));
    }

    @Test
    @Tag("H4Y-26-AC-1")
    void onListBasePrices_returnTypesWithoutPrice_whenPriceNotSet() {
        assertNull(priceOf("duplo"));
    }

    @Test
    @Tag("H4Y-26-AC-1")
    void onSetBasePrice_saveTwoDecimals_whenPriceHasNoDecimals() {
        service.setBasePrice(duplo, new BigDecimal("80"), MANAGER);

        assertEquals("80.00", priceOf("duplo").toPlainString());
    }

    @Test
    @Tag("H4Y-26-AC-1")
    void onSetBasePrice_savePrice_whenPriceIsZero() {
        service.setBasePrice(duplo, BigDecimal.ZERO, MANAGER);

        assertEquals(new BigDecimal("0.00"), priceOf("duplo"));
    }

    @Test
    @Tag("H4Y-26-AC-1")
    void onSetBasePrice_savePrice_whenPriceIsMaximum() {
        service.setBasePrice(duplo, new BigDecimal("999999.99"), MANAGER);

        assertEquals(new BigDecimal("999999.99"), priceOf("duplo"));
    }

    @Test
    @Tag("H4Y-26-AC-2")
    void onSetBasePrice_changePrice_whenPriceAlreadySet() {
        service.setBasePrice(individual, new BigDecimal("55.00"), MANAGER);

        assertEquals(new BigDecimal("55.00"), priceOf("individual"));
    }

    @ParameterizedTest
    @ValueSource(strings = {"-0.01", "-10", "80.123", "1000000.00"})
    @Tag("H4Y-26-AC-3")
    void onSetBasePrice_throw_whenPriceIsInvalid(String value) {
        IllegalArgumentException error = assertThrows(IllegalArgumentException.class,
                () -> service.setBasePrice(individual, new BigDecimal(value), MANAGER));

        assertEquals(PRICE_MESSAGE, error.getMessage());
        assertEquals(new BigDecimal("50.00"), priceOf("individual"));
    }

    @Test
    @Tag("H4Y-26-AC-3")
    void onSetBasePrice_throw_whenPriceIsNull() {
        IllegalArgumentException error = assertThrows(IllegalArgumentException.class,
                () -> service.setBasePrice(individual, null, MANAGER));

        assertEquals(PRICE_MESSAGE, error.getMessage());
        assertEquals(new BigDecimal("50.00"), priceOf("individual"));
    }

    @Test
    @Tag("H4Y-26-AC-3")
    void onSetBasePrice_throw_whenTypeDoesNotExist() {
        RoomType unknown = new RoomType(99L, "suite", 2, null);

        IllegalArgumentException error = assertThrows(IllegalArgumentException.class,
                () -> service.setBasePrice(unknown, new BigDecimal("80.00"), MANAGER));

        assertEquals(NOT_FOUND_MESSAGE, error.getMessage());
    }

    @Test
    @Tag("H4Y-26-AC-3")
    void onSetBasePrice_throw_whenTypeIsNull() {
        IllegalArgumentException error = assertThrows(IllegalArgumentException.class,
                () -> service.setBasePrice(null, new BigDecimal("80.00"), MANAGER));

        assertEquals(TYPE_MESSAGE, error.getMessage());
    }

    @Test
    @Tag("H4Y-26-AC-3")
    void onSetBasePrice_throw_whenTypeHasNoId() {
        RoomType noId = new RoomType(null, "suite", 2, null);

        IllegalArgumentException error = assertThrows(IllegalArgumentException.class,
                () -> service.setBasePrice(noId, new BigDecimal("80.00"), MANAGER));

        assertEquals(TYPE_MESSAGE, error.getMessage());
    }

    @ParameterizedTest
    @ValueSource(strings = {"80,50", "80.50", " 80.50 "})
    @Tag("H4Y-26-AC-3")
    void onParsePrice_returnPrice_whenTextIsNumber(String text) {
        assertEquals(new BigDecimal("80.50"), service.parsePrice(text));
    }

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {" ", "abc", "1.000,50"})
    @Tag("H4Y-26-AC-3")
    void onParsePrice_throw_whenTextIsNotNumber(String text) {
        IllegalArgumentException error = assertThrows(IllegalArgumentException.class,
                () -> service.parsePrice(text));

        assertEquals(NUMBER_MESSAGE, error.getMessage());
    }

    @Test
    @Tag("H4Y-26-AC-4")
    void onSetBasePrice_throwAccessDenied_whenActorIsReceptionist() {
        AccessDeniedException error = assertThrows(AccessDeniedException.class,
                () -> service.setBasePrice(individual, new BigDecimal("60.00"), RECEPTIONIST));

        assertEquals(SET_DENIED_MESSAGE, error.getMessage());
        assertEquals(new BigDecimal("50.00"), priceOf("individual"));
    }

    @Test
    @Tag("H4Y-26-AC-4")
    void onSetBasePrice_throwAccessDenied_whenActorIsRestaurantStaff() {
        assertThrows(AccessDeniedException.class,
                () -> service.setBasePrice(individual, new BigDecimal("60.00"), RESTAURANT_STAFF));
        assertEquals(new BigDecimal("50.00"), priceOf("individual"));
    }

    @Test
    @Tag("H4Y-26-AC-4")
    void onSetBasePrice_throwAccessDenied_whenActorIsNull() {
        assertThrows(AccessDeniedException.class,
                () -> service.setBasePrice(individual, new BigDecimal("60.00"), null));
        assertEquals(new BigDecimal("50.00"), priceOf("individual"));
    }

    @Test
    @Tag("H4Y-26-AC-4")
    void onListBasePrices_throwAccessDenied_whenActorIsReceptionist() {
        AccessDeniedException error = assertThrows(AccessDeniedException.class,
                () -> service.listBasePrices(RECEPTIONIST));

        assertEquals(LIST_DENIED_MESSAGE, error.getMessage());
    }

    @Test
    @Tag("H4Y-26-AC-4")
    void onListBasePrices_throwAccessDenied_whenActorIsRestaurantStaff() {
        assertThrows(AccessDeniedException.class, () -> service.listBasePrices(RESTAURANT_STAFF));
    }

    @Test
    @Tag("H4Y-26-AC-4")
    void onListBasePrices_throwAccessDenied_whenActorIsNull() {
        assertThrows(AccessDeniedException.class, () -> service.listBasePrices(null));
    }
}
