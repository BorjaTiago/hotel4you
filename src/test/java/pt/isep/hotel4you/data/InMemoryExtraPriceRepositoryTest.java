package pt.isep.hotel4you.data;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import pt.isep.hotel4you.domain.ExtraService;
import pt.isep.hotel4you.domain.ExtraType;

/** Tests that the saved catalogue fixture rejects entries without a saved identifier. */
class InMemoryExtraPriceRepositoryTest {
    @Test
    @Tag("H4Y-93-AC-3")
    void onAdd_rejectUnsavedEntry_whenIdIsNull() {
        var repository = new InMemoryExtraPriceRepository();
        var unsaved = new ExtraService(null, "Vista mar", ExtraType.ROOM_EXTRA, null);
        var error = assertThrows(IllegalArgumentException.class, () -> repository.add(unsaved));
        assertEquals("A catalogue entry must have an id.", error.getMessage());
        assertEquals(0, repository.findAll().size());
    }
}
