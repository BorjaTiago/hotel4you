package pt.isep.hotel4you.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import pt.isep.hotel4you.data.InMemoryItemRepository;
import pt.isep.hotel4you.domain.Manager;
import pt.isep.hotel4you.domain.Receptionist;
import pt.isep.hotel4you.domain.StockItem;
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
        service.createItem("toalhas", "unidade", manager());
        StockItem sheets = service.createItem("lençóis", "unidade", manager());

        assertThrows(StockItemNameTakenException.class,
                () -> service.editItem(new StockItem(sheets.getId(), "toalhas", "unidade", 0), manager()));
        assertEquals("lençóis", repository.findById(sheets.getId()).get().getName());
    }

    @Test
    @Tag("H4Y-51-AC-8")
    void onEditItem_throwNotFound_whenMissing() {
        assertThrows(StockItemNotFoundException.class,
                () -> service.editItem(new StockItem(99L, "toalhas", "unidade", 0), manager()));
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
}
