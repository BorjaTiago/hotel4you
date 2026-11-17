package pt.isep.hotel4you.service;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.math.BigDecimal;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.junit.jupiter.params.provider.ValueSource;
import pt.isep.hotel4you.data.InMemoryExtraPriceRepository;
import pt.isep.hotel4you.domain.ExtraService;
import pt.isep.hotel4you.domain.ExtraType;
import pt.isep.hotel4you.domain.Manager;
import pt.isep.hotel4you.domain.Role;
import pt.isep.hotel4you.domain.User;

/** Tests the fixed extra prices per night, without a database or a screen. */
class ExtraPriceServiceTest {
    private static final User MANAGER = new Manager(1L, "Gestor", "gestor", "hash");
    private InMemoryExtraPriceRepository repository;
    private ExtraPriceService service;

    @BeforeEach
    void setUp() {
        repository = new InMemoryExtraPriceRepository();
        repository.add(new ExtraService(1L, "Vista mar", ExtraType.ROOM_EXTRA, null));
        repository.add(new ExtraService(2L, "Spa", ExtraType.PAID_SERVICE, null));
        service = new ExtraPriceService(repository);
    }

    private ExtraService extra(long id) {
        return repository.findAll().stream().filter(e -> e.getId() == id)
                .findFirst().orElseThrow();
    }

    @Test
    @Tag("H4Y-93-AC-1")
    void onSetPrice_saveSeaViewPrice_whenManagerSetsPrice() {
        service.setPrice(extra(1), new BigDecimal("15.00"), MANAGER);
        ExtraService saved = new ExtraPriceService(repository).listPrices(MANAGER).getFirst();
        assertAll(() -> assertEquals(new BigDecimal("15.00"), saved.getPrice()),
                () -> assertEquals("Vista mar", saved.getName()),
                () -> assertEquals(ExtraType.ROOM_EXTRA, saved.getType()));
    }

    @Test
    @Tag("H4Y-93-AC-2")
    void onSetPrice_saveSpaPrice_whenManagerSetsPrice() {
        service.setPrice(extra(2), new BigDecimal("40.00"), MANAGER);
        assertEquals(new BigDecimal("40.00"), extra(2).getPrice());
        assertEquals(ExtraType.PAID_SERVICE, extra(2).getType());
        assertNull(extra(1).getPrice());
    }

    @Test
    @Tag("H4Y-93-AC-3")
    void onListPrices_returnNineItemsWithoutPrice_whenCatalogueContainsRequiredExtras() {
        var catalogue = new InMemoryExtraPriceRepository();
        List<ExtraService> expected = List.of(
                new ExtraService(1L, "Vista mar", ExtraType.ROOM_EXTRA, null),
                new ExtraService(2L, "Vista montanha", ExtraType.ROOM_EXTRA, null),
                new ExtraService(3L, "Bebidas", ExtraType.ROOM_EXTRA, null),
                new ExtraService(4L, "Lavandaria", ExtraType.ROOM_EXTRA, null),
                new ExtraService(5L, "Comida", ExtraType.PAID_SERVICE, null),
                new ExtraService(6L, "Eventos culturais", ExtraType.PAID_SERVICE, null),
                new ExtraService(7L, "Piscinas privadas", ExtraType.PAID_SERVICE, null),
                new ExtraService(8L, "Spa", ExtraType.PAID_SERVICE, null),
                new ExtraService(9L, "Salas de conferências", ExtraType.PAID_SERVICE, null));
        expected.forEach(catalogue::add);
        List<ExtraService> extras = new ExtraPriceService(catalogue).listPrices(MANAGER);
        assertEquals(expected, extras);
        assertTrue(extras.stream().allMatch(e -> e.getPrice() == null));
    }

    @ParameterizedTest
    @ValueSource(strings = {"0", "15", "15.5", "999999.99"})
    @Tag("H4Y-93-AC-1")
    void onSetPrice_saveTwoDecimals_whenPriceIsValid(String value) {
        service.setPrice(extra(1), new BigDecimal(value), MANAGER);
        assertEquals(new BigDecimal(value).setScale(2), extra(1).getPrice());
    }

    @ParameterizedTest
    @ValueSource(strings = {"-0.01", "-15", "15.001", "1000000", "1.000"})
    @Tag("H4Y-93-AC-4")
    void onSetPrice_keepPreviousPrice_whenPriceIsInvalid(String value) {
        repository.updatePrice(1L, new BigDecimal("15.00"));
        var error = assertThrows(IllegalArgumentException.class,
                () -> service.setPrice(extra(1), new BigDecimal(value), MANAGER));
        assertEquals("O preço tem de estar entre 0,00 e 999999,99, com duas casas decimais.",
                error.getMessage());
        assertEquals(new BigDecimal("15.00"), extra(1).getPrice());
        assertNull(extra(2).getPrice());
    }

    @Test
    @Tag("H4Y-93-AC-4")
    void onSetPrice_keepPreviousPrice_whenPriceIsNull() {
        repository.updatePrice(1L, new BigDecimal("15.00"));
        assertThrows(IllegalArgumentException.class, () -> service.setPrice(extra(1), null, MANAGER));
        assertEquals(new BigDecimal("15.00"), extra(1).getPrice());
    }

    @Test
    @Tag("H4Y-93-AC-4")
    void onSetPrice_rejectUnknownExtra_whenIdIsNotInCatalogue() {
        var unknown = new ExtraService(99L, "Unknown", ExtraType.ROOM_EXTRA, null);
        var error = assertThrows(IllegalArgumentException.class,
                () -> service.setPrice(unknown, BigDecimal.TEN, MANAGER));
        assertEquals("O extra indicado não existe.", error.getMessage());
        assertEquals(2, repository.findAll().size());
        assertTrue(repository.findAll().stream().allMatch(e -> e.getPrice() == null));
    }

    @Test
    @Tag("H4Y-93-AC-4")
    void onSetPrice_rejectSelection_whenExtraIsMissingOrUnsaved() {
        assertThrows(IllegalArgumentException.class, () -> service.setPrice(null, BigDecimal.TEN, MANAGER));
        assertThrows(IllegalArgumentException.class, () -> service.setPrice(
                new ExtraService(null, "Vista mar", ExtraType.ROOM_EXTRA, null), BigDecimal.TEN, MANAGER));
    }

    @ParameterizedTest
    @EnumSource(value = Role.class, names = "MANAGER", mode = EnumSource.Mode.EXCLUDE)
    @Tag("H4Y-93-AC-5")
    void onManagePrices_denyAndKeepPrices_whenActorIsNotManager(Role role) {
        User actor = new User(2L, "Outro", "outro", "hash", role) { };
        assertThrows(AccessDeniedException.class, () -> service.listPrices(actor));
        assertThrows(AccessDeniedException.class, () -> service.setPrice(extra(1), BigDecimal.TEN, actor));
        assertThrows(AccessDeniedException.class, () -> service.setPrice(null, null, actor));
        assertNull(extra(1).getPrice());
    }

    @Test
    @Tag("H4Y-93-AC-5")
    void onManagePrices_denyAndKeepPrices_whenActorIsNull() {
        assertThrows(AccessDeniedException.class, () -> service.listPrices(null));
        assertThrows(AccessDeniedException.class, () -> service.setPrice(extra(1), BigDecimal.TEN, null));
        assertNull(extra(1).getPrice());
    }
}
