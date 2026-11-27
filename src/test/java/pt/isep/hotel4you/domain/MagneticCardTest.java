package pt.isep.hotel4you.domain;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDateTime;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

/** Tests the rules of the magnetic card. */
class MagneticCardTest {

    @Test
    @Tag("H4Y-109-AC-1")
    void onCreate_keepValues_whenClientCardOfReservation() {
        LocalDateTime blockedAt = LocalDateTime.of(2026, 11, 20, 10, 0);

        MagneticCard card = new MagneticCard(7L, "12345", CardType.CLIENT, 3L, null, true, blockedAt);

        assertEquals(7L, card.getId());
        assertEquals("12345", card.getNumber());
        assertEquals(CardType.CLIENT, card.getType());
        assertEquals(3L, card.getReservationId());
        assertNull(card.getUserId());
        assertTrue(card.isActive());
        assertEquals(blockedAt, card.getBlockedAt());
    }

    @Test
    @Tag("H4Y-109-AC-1")
    void onCreate_acceptStaffCard_whenClientTypeHasUser() {
        MagneticCard card = new MagneticCard(null, "12346", CardType.CLIENT, null, 2L, true, null);

        assertEquals(2L, card.getUserId());
        assertNull(card.getReservationId());
    }

    @Test
    @Tag("H4Y-109-AC-5")
    void onCreate_acceptMasterCard_whenOwnerIsUser() {
        MagneticCard card = new MagneticCard(null, "90001", CardType.MASTER, null, 1L, true, null);

        assertEquals(CardType.MASTER, card.getType());
        assertEquals(1L, card.getUserId());
    }

    @Test
    @Tag("H4Y-109-AC-1")
    void onCreate_throw_whenCardHasReservationAndUser() {
        assertThrows(IllegalArgumentException.class,
                () -> new MagneticCard(null, "12345", CardType.CLIENT, 3L, 2L, true, null));
    }

    @Test
    @Tag("H4Y-109-AC-1")
    void onCreate_throw_whenCardHasNoOwner() {
        assertThrows(IllegalArgumentException.class,
                () -> new MagneticCard(null, "12345", CardType.CLIENT, null, null, true, null));
    }

    @Test
    @Tag("H4Y-109-AC-5")
    void onCreate_throw_whenMasterCardBelongsToReservation() {
        assertThrows(IllegalArgumentException.class,
                () -> new MagneticCard(null, "90001", CardType.MASTER, 3L, null, true, null));
    }

    @Test
    @Tag("H4Y-109-AC-1")
    void onCreate_throw_whenNumberIsBlankOrNull() {
        assertThrows(IllegalArgumentException.class,
                () -> new MagneticCard(null, " ", CardType.CLIENT, 3L, null, true, null));
        assertThrows(IllegalArgumentException.class,
                () -> new MagneticCard(null, null, CardType.CLIENT, 3L, null, true, null));
    }

    @Test
    @Tag("H4Y-109-AC-1")
    void onCreate_throw_whenNumberHasMoreThan20Characters() {
        assertThrows(IllegalArgumentException.class,
                () -> new MagneticCard(null, "1".repeat(21), CardType.CLIENT, 3L, null, true, null));
    }

    @Test
    @Tag("H4Y-109-AC-1")
    void onCreate_throw_whenTypeIsNull() {
        assertThrows(IllegalArgumentException.class,
                () -> new MagneticCard(null, "12345", null, 3L, null, true, null));
    }

    @Test
    @Tag("H4Y-109-AC-1")
    void onIsBlocked_returnTrue_whenBlockedAtIsSet() {
        MagneticCard blocked = new MagneticCard(1L, "12345", CardType.CLIENT, 3L, null, false,
                LocalDateTime.of(2026, 11, 20, 10, 0));
        MagneticCard free = new MagneticCard(2L, "12346", CardType.CLIENT, 4L, null, true, null);

        assertTrue(blocked.isBlocked());
        assertFalse(free.isBlocked());
    }

    @Test
    @Tag("H4Y-109-AC-3")
    void onCreate_keepAccessCodeHash_whenHashIsGiven() {
        MagneticCard card = new MagneticCard(1L, "12345", CardType.CLIENT, 3L, null, true, null, "hash");

        assertEquals("hash", card.getAccessCodeHash());
    }

    @Test
    @Tag("H4Y-109-AC-3")
    void onCreate_haveNoAccessCodeHash_whenOldConstructorIsUsed() {
        MagneticCard card = new MagneticCard(1L, "12345", CardType.CLIENT, 3L, null, true, null);

        assertNull(card.getAccessCodeHash());
    }
}
