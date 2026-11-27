package pt.isep.hotel4you.domain;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.time.LocalDateTime;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

/** Tests the rules of one card entry. */
class CardAccessRecordTest {

    private static final LocalDateTime WHEN = LocalDateTime.of(2026, 11, 20, 15, 30);

    @Test
    @Tag("H4Y-109-AC-1")
    void onCreate_keepValues_whenEntryIsInRoom() {
        CardAccessRecord record = new CardAccessRecord(5L, 7L, 12L, null, WHEN);

        assertEquals(5L, record.getId());
        assertEquals(7L, record.getCardId());
        assertEquals(12L, record.getRoomId());
        assertNull(record.getLocationId());
        assertEquals(WHEN, record.getEntryDateTime());
    }

    @Test
    @Tag("H4Y-109-AC-1")
    void onCreate_keepValues_whenEntryIsInLocation() {
        CardAccessRecord record = new CardAccessRecord(null, 7L, null, 3L, WHEN);

        assertNull(record.getRoomId());
        assertEquals(3L, record.getLocationId());
    }

    @Test
    @Tag("H4Y-109-AC-1")
    void onCreate_throw_whenEntryHasRoomAndLocation() {
        assertThrows(IllegalArgumentException.class, () -> new CardAccessRecord(null, 7L, 12L, 3L, WHEN));
    }

    @Test
    @Tag("H4Y-109-AC-1")
    void onCreate_throw_whenEntryHasNoRoomAndNoLocation() {
        assertThrows(IllegalArgumentException.class, () -> new CardAccessRecord(null, 7L, null, null, WHEN));
    }

    @Test
    @Tag("H4Y-109-AC-1")
    void onCreate_throw_whenEntryTimeIsNull() {
        assertThrows(IllegalArgumentException.class, () -> new CardAccessRecord(null, 7L, 12L, null, null));
    }
}
