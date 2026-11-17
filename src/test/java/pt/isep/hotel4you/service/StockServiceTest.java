package pt.isep.hotel4you.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import pt.isep.hotel4you.data.DuplicateItemException;
import pt.isep.hotel4you.data.InMemoryItemRepository;
import pt.isep.hotel4you.domain.Manager;
import pt.isep.hotel4you.domain.MovementType;
import pt.isep.hotel4you.domain.Receptionist;
import pt.isep.hotel4you.domain.RestaurantStaff;
import pt.isep.hotel4you.domain.StockItem;
import pt.isep.hotel4you.domain.StockMovement;
import pt.isep.hotel4you.domain.User;

/** Tests that the manager can create, edit and list stock items. */
class StockServiceTest {

    private static final String DENIED_MESSAGE = "Só o gestor pode gerir o stock.";

    private StockService service;
    private InMemoryItemRepository repository;

    @BeforeEach
    void setUp() {
        repository = new InMemoryItemRepository();
        service = new StockService(repository);
    }

    private User manager() {
        return new Manager(1L, "Rui Costa", "rui", "hash");
    }

    private User receptionist() {
        return new Receptionist(2L, "Ana Silva", "ana", "hash");
    }

    @Test
    @Tag("H4Y-51-AC-1")
    void onCreateItem_returnItemWithZeroStock_whenNameAndUnitGiven() {
        StockItem item = service.createItem("  toalhas ", " unidade ", manager());

        assertNotNull(item.getId());
        assertEquals("toalhas", item.getName());
        assertEquals("unidade", item.getUnit());
        assertEquals(0, item.getStockQuantity());
        assertEquals("toalhas", repository.findById(item.getId()).get().getName());
    }

    @Test
    @Tag("H4Y-51-AC-2")
    void onCreateItem_throw_whenNameBlank() {
        IllegalArgumentException blank = assertThrows(IllegalArgumentException.class,
                () -> service.createItem("  ", "unidade", manager()));
        IllegalArgumentException missing = assertThrows(IllegalArgumentException.class,
                () -> service.createItem(null, "unidade", manager()));

        assertTrue(blank.getMessage().contains("nome"));
        assertTrue(missing.getMessage().contains("nome"));
        assertEquals(0, repository.count());
    }

    @Test
    @Tag("H4Y-51-AC-2")
    void onCreateItem_throw_whenUnitBlank() {
        IllegalArgumentException blank = assertThrows(IllegalArgumentException.class,
                () -> service.createItem("toalhas", "", manager()));
        IllegalArgumentException missing = assertThrows(IllegalArgumentException.class,
                () -> service.createItem("toalhas", null, manager()));

        assertTrue(blank.getMessage().contains("unidade"));
        assertTrue(missing.getMessage().contains("unidade"));
        assertEquals(0, repository.count());
    }

    @Test
    @Tag("H4Y-51-AC-2")
    void onCreateItem_throw_whenNameTooLong() {
        String longName = "a".repeat(101);

        IllegalArgumentException error = assertThrows(IllegalArgumentException.class,
                () -> service.createItem(longName, "unidade", manager()));

        assertTrue(error.getMessage().contains("nome"));
        assertEquals(0, repository.count());
    }

    @Test
    @Tag("H4Y-51-AC-2")
    void onCreateItem_acceptName_whenNameHas100Chars() {
        String name = "a".repeat(100);

        StockItem item = service.createItem(" " + name + " ", "unidade", manager());

        assertEquals(name, item.getName());
        assertEquals(1, repository.count());
    }

    @Test
    @Tag("H4Y-51-AC-2")
    void onCreateItem_throw_whenUnitTooLong() {
        String longUnit = "u".repeat(21);

        IllegalArgumentException error = assertThrows(IllegalArgumentException.class,
                () -> service.createItem("toalhas", longUnit, manager()));

        assertTrue(error.getMessage().contains("unidade"));
        assertEquals(0, repository.count());
    }

    @Test
    @Tag("H4Y-51-AC-2")
    void onCreateItem_acceptUnit_whenUnitHas20Chars() {
        String unit = "u".repeat(20);

        StockItem item = service.createItem("toalhas", " " + unit + " ", manager());

        assertEquals(unit, item.getUnit());
        assertEquals(1, repository.count());
    }

    @Test
    @Tag("H4Y-51-AC-6")
    void onEditItem_throw_whenNameTooLong() {
        StockItem item = service.createItem("toalhas", "unidade", manager());
        String longName = "a".repeat(101);

        IllegalArgumentException error = assertThrows(IllegalArgumentException.class,
                () -> service.editItem(new StockItem(item.getId(), longName, "unidade", 0), manager()));

        assertTrue(error.getMessage().contains("nome"));
        assertEquals("toalhas", repository.findById(item.getId()).get().getName());
    }

    @Test
    @Tag("H4Y-51-AC-3")
    void onCreateItem_throwNameTaken_whenNameExists() {
        service.createItem("toalhas", "unidade", manager());

        assertThrows(StockItemNameTakenException.class,
                () -> service.createItem("toalhas", "caixa", manager()));
        assertEquals(1, repository.count());
    }

    @Test
    @Tag("H4Y-51-AC-4")
    void onCreateItem_throwAccessDenied_whenNotManager() {
        User actor = receptionist();

        AccessDeniedException error = assertThrows(AccessDeniedException.class,
                () -> service.createItem("toalhas", "unidade", actor));

        assertEquals(DENIED_MESSAGE, error.getMessage());
        assertEquals(0, repository.count());
    }

    @Test
    @Tag("H4Y-51-AC-5")
    void onEditItem_updateNameAndUnit_whenValid() {
        StockItem item = service.createItem("toalhas", "unidade", manager());

        service.editItem(new StockItem(item.getId(), " lençóis ", " caixa ", 0), manager());

        StockItem stored = repository.findById(item.getId()).get();
        assertEquals("lençóis", stored.getName());
        assertEquals("caixa", stored.getUnit());
    }

    @Test
    @Tag("H4Y-51-AC-5")
    void onEditItem_keepStock_whenEdited() {
        StockItem saved = repository.save(new StockItem(null, "toalhas", "unidade", 15));

        service.editItem(new StockItem(saved.getId(), "lençóis", "caixa", 99), manager());

        assertEquals(15, repository.findById(saved.getId()).get().getStockQuantity());
    }

    @Test
    @Tag("H4Y-51-AC-5")
    void onEditItem_acceptSameName_whenNameUnchanged() {
        StockItem item = service.createItem("toalhas", "unidade", manager());

        service.editItem(new StockItem(item.getId(), "toalhas", "caixa", 0), manager());

        assertEquals("caixa", repository.findById(item.getId()).get().getUnit());
    }

    @Test
    @Tag("H4Y-51-AC-7")
    void onEditItem_throwNameTaken_whenOtherItemHasName() {
        StockItem towels = service.createItem("toalhas", "unidade", manager());
        StockItem sheets = service.createItem("lençóis", "unidade", manager());

        assertThrows(StockItemNameTakenException.class,
                () -> service.editItem(new StockItem(sheets.getId(), "toalhas", "unidade", 0), manager()));
        assertEquals("lençóis", repository.findById(sheets.getId()).get().getName());
        assertEquals("toalhas", repository.findById(towels.getId()).get().getName());
    }

    @Test
    @Tag("H4Y-51-AC-8")
    void onEditItem_throwNotFound_whenMissing() {
        assertThrows(StockItemNotFoundException.class,
                () -> service.editItem(new StockItem(99L, "toalhas", "unidade", 0), manager()));
    }

    @Test
    @Tag("H4Y-51-AC-8")
    void onEditItem_throwNotFound_whenIdNull() {
        assertThrows(StockItemNotFoundException.class,
                () -> service.editItem(new StockItem(null, "toalhas", "unidade", 0), manager()));
    }

    @Test
    @Tag("H4Y-51-AC-6")
    void onEditItem_throw_whenNameBlank() {
        StockItem item = service.createItem("toalhas", "unidade", manager());

        IllegalArgumentException error = assertThrows(IllegalArgumentException.class,
                () -> service.editItem(new StockItem(item.getId(), " ", "unidade", 0), manager()));

        assertTrue(error.getMessage().contains("nome"));
        assertEquals("toalhas", repository.findById(item.getId()).get().getName());
    }

    @Test
    @Tag("H4Y-51-AC-6")
    void onEditItem_throw_whenUnitBlank() {
        StockItem item = service.createItem("toalhas", "unidade", manager());

        IllegalArgumentException error = assertThrows(IllegalArgumentException.class,
                () -> service.editItem(new StockItem(item.getId(), "toalhas", " ", 0), manager()));

        assertTrue(error.getMessage().contains("unidade"));
        assertEquals("unidade", repository.findById(item.getId()).get().getUnit());
    }

    @Test
    @Tag("H4Y-51-AC-9")
    void onEditItem_throwAccessDenied_whenNotManager() {
        StockItem item = service.createItem("toalhas", "unidade", manager());
        User actor = receptionist();

        AccessDeniedException error = assertThrows(AccessDeniedException.class,
                () -> service.editItem(new StockItem(item.getId(), "lençóis", "caixa", 0), actor));

        assertEquals(DENIED_MESSAGE, error.getMessage());
        assertEquals("toalhas", repository.findById(item.getId()).get().getName());
    }

    @Test
    @Tag("H4Y-51-AC-1")
    void onListItems_returnItems_whenManager() {
        service.createItem("toalhas", "unidade", manager());
        service.createItem("lençóis", "caixa", manager());

        List<StockItem> items = service.listItems(manager());

        assertEquals(2, items.size());
        assertTrue(items.stream().anyMatch(item -> item.getName().equals("toalhas")));
        assertTrue(items.stream().anyMatch(item -> item.getName().equals("lençóis")));
    }

    @Test
    @Tag("H4Y-51-AC-4")
    void onListItems_throwAccessDenied_whenNotManager() {
        User actor = receptionist();

        AccessDeniedException error = assertThrows(AccessDeniedException.class,
                () -> service.listItems(actor));

        assertEquals(DENIED_MESSAGE, error.getMessage());
    }

    /** A repository whose save fails with a duplicate error, as when another user saves first. */
    private static class DuplicateOnSaveRepository extends InMemoryItemRepository {

        @Override
        public StockItem save(StockItem item) {
            throw new DuplicateItemException();
        }
    }

    @Test
    @Tag("H4Y-51-AC-3")
    void onCreateItem_throwNameTaken_whenRepositoryReportsDuplicate() {
        StockService duplicateAtSave = new StockService(new DuplicateOnSaveRepository());

        assertThrows(StockItemNameTakenException.class,
                () -> duplicateAtSave.createItem("toalhas", "unidade", manager()));
    }

    @Test
    @Tag("H4Y-51-AC-7")
    void onEditItem_throwNameTaken_whenRepositoryReportsDuplicate() {
        StockService sameNameAtUpdate = new StockService(new FoundRepository(true));

        assertThrows(StockItemNameTakenException.class,
                () -> sameNameAtUpdate.editItem(new StockItem(1L, "toalhas", "unidade", 0), manager()));
    }

    @Test
    @Tag("H4Y-51-AC-8")
    void onEditItem_throwNotFound_whenUpdateChangesNoRow() {
        StockService goneAtUpdate = new StockService(new FoundRepository(false));

        assertThrows(StockItemNotFoundException.class,
                () -> goneAtUpdate.editItem(new StockItem(1L, "toalhas", "unidade", 0), manager()));
    }

    private StockItem towelsWithStock10() {
        return repository.save(new StockItem(null, "toalhas", "unidade", 10));
    }

    @Test
    @Tag("H4Y-90-AC-1")
    void onRegisterMovement_saveEntryAndRaiseStock_whenManagerRecordsEntry() {
        StockItem towels = towelsWithStock10();
        User manager = manager();

        StockMovement movement = service.registerMovement(towels, MovementType.ENTRY, 5, manager);

        assertEquals(MovementType.ENTRY, movement.getType());
        assertEquals(5, movement.getQuantity());
        assertEquals(LocalDate.now(), movement.getDateTime().toLocalDate());
        assertEquals(manager, movement.getUser());
        assertEquals(15, movement.getItem().getStockQuantity());
        assertEquals(15, repository.findById(towels.getId()).get().getStockQuantity());
        assertEquals(1, repository.movements().size());
        assertEquals(5, repository.movements().get(0).getQuantity());
    }

    @Test
    @Tag("H4Y-90-AC-1")
    void onRegisterMovement_throwAccessDenied_whenReceptionistOrRestaurantStaff() {
        StockItem towels = towelsWithStock10();
        User restaurant = new RestaurantStaff(3L, "Rita Melo", "rita", "hash");

        AccessDeniedException receptionistError = assertThrows(AccessDeniedException.class,
                () -> service.registerMovement(towels, MovementType.ENTRY, 5, receptionist()));
        assertThrows(AccessDeniedException.class,
                () -> service.registerMovement(towels, MovementType.ENTRY, 5, restaurant));

        assertEquals(DENIED_MESSAGE, receptionistError.getMessage());
        assertEquals(10, repository.findById(towels.getId()).get().getStockQuantity());
        assertTrue(repository.movements().isEmpty());
    }

    @Test
    @Tag("H4Y-90-AC-2")
    void onRegisterMovement_throw_whenQuantityZeroOrNegative() {
        StockItem towels = towelsWithStock10();

        IllegalArgumentException zero = assertThrows(IllegalArgumentException.class,
                () -> service.registerMovement(towels, MovementType.ENTRY, 0, manager()));
        IllegalArgumentException negative = assertThrows(IllegalArgumentException.class,
                () -> service.registerMovement(towels, MovementType.ENTRY, -3, manager()));

        assertEquals("A quantidade tem de ser maior que zero.", zero.getMessage());
        assertEquals("A quantidade tem de ser maior que zero.", negative.getMessage());
        assertEquals(10, repository.findById(towels.getId()).get().getStockQuantity());
        assertTrue(repository.movements().isEmpty());
    }

    @Test
    @Tag("H4Y-90-AC-2")
    void onRegisterMovement_throw_whenQuantityAboveMaximum() {
        StockItem towels = towelsWithStock10();

        IllegalArgumentException error = assertThrows(IllegalArgumentException.class,
                () -> service.registerMovement(towels, MovementType.ENTRY, 32768, manager()));

        assertEquals("A quantidade máxima é 32767.", error.getMessage());
        assertEquals(10, repository.findById(towels.getId()).get().getStockQuantity());
    }

    @Test
    @Tag("H4Y-90-AC-2")
    void onRegisterMovement_acceptQuantity_whenQuantityIsMaximum() {
        StockItem towels = towelsWithStock10();

        service.registerMovement(towels, MovementType.ENTRY, 32767, manager());

        assertEquals(32777, repository.findById(towels.getId()).get().getStockQuantity());
    }

    @Test
    @Tag("H4Y-90-AC-3")
    void onRegisterMovement_throwNotFound_whenItemDoesNotExist() {
        StockItem rockets = new StockItem(99L, "foguetes", "unidade", 0);

        assertThrows(StockItemNotFoundException.class,
                () -> service.registerMovement(rockets, MovementType.ENTRY, 5, manager()));
        assertThrows(StockItemNotFoundException.class,
                () -> service.registerMovement(new StockItem(null, "foguetes", "unidade", 0),
                        MovementType.ENTRY, 5, manager()));

        assertTrue(repository.movements().isEmpty());
    }

    @Test
    @Tag("H4Y-90-AC-3")
    void onRegisterMovement_throwNotFound_whenItemIsRemovedBeforeSave() {
        StockService goneAtSave = new StockService(new GoneAtMovementRepository());

        assertThrows(StockItemNotFoundException.class,
                () -> goneAtSave.registerMovement(new StockItem(1L, "toalhas", "unidade", 10),
                        MovementType.ENTRY, 5, manager()));
    }

    @Test
    @Tag("H4Y-90-AC-3")
    void onRegisterMovement_throw_whenNoItemOrTypeChosen() {
        StockItem towels = towelsWithStock10();

        IllegalArgumentException noItem = assertThrows(IllegalArgumentException.class,
                () -> service.registerMovement(null, MovementType.ENTRY, 5, manager()));
        IllegalArgumentException noType = assertThrows(IllegalArgumentException.class,
                () -> service.registerMovement(towels, null, 5, manager()));

        assertEquals("Escolha um item.", noItem.getMessage());
        assertEquals("Escolha o tipo de movimento.", noType.getMessage());
        assertTrue(repository.movements().isEmpty());
    }

    @Test
    @Tag("H4Y-90-AC-2")
    void onParseQuantity_returnNumber_whenTextIsWholeNumber() {
        assertEquals(5, service.parseQuantity(" 5 "));
        assertEquals(-3, service.parseQuantity("-3"));
        assertEquals(0, service.parseQuantity("0"));
    }

    @Test
    @Tag("H4Y-90-AC-2")
    void onParseQuantity_throw_whenTextIsNotWholeNumber() {
        for (String text : new String[] {null, "", "  ", "abc", "2,5", "1.5", "9999999999"}) {
            IllegalArgumentException error = assertThrows(IllegalArgumentException.class,
                    () -> service.parseQuantity(text));
            assertEquals("A quantidade tem de ser um número inteiro.", error.getMessage());
        }
    }

    /** A repository that finds item 1 but has no item at save time, as when another user removes it. */
    private static class GoneAtMovementRepository extends InMemoryItemRepository {

        @Override
        public Optional<StockItem> findById(long id) {
            return Optional.of(new StockItem(id, "toalhas", "unidade", 10));
        }

        @Override
        public boolean addMovement(StockMovement movement) {
            return false;
        }
    }

    /** A repository that finds item 1 but fails at update, as when another user changes it. */
    private static class FoundRepository extends InMemoryItemRepository {

        private final boolean duplicate;

        FoundRepository(boolean duplicate) {
            this.duplicate = duplicate;
        }

        @Override
        public Optional<StockItem> findById(long id) {
            return Optional.of(new StockItem(id, "velho", "unidade", 0));
        }

        @Override
        public boolean update(StockItem item) {
            if (duplicate) {
                throw new DuplicateItemException();
            }
            return false;
        }
    }
}
