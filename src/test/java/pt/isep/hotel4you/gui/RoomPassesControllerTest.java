package pt.isep.hotel4you.gui;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import pt.isep.hotel4you.domain.CardEntry;
import pt.isep.hotel4you.domain.CardType;

/** Tests the texts of the screen with the passes of a room (H4Y-156). */
class RoomPassesControllerTest {

    @Test
    @Tag("H4Y-156-AC-2")
    void onCardTypeText_showMaster_whenCardIsMaster() {
        assertEquals("Mestre", RoomPassesController.cardTypeText(CardType.MASTER));
        assertEquals("Cliente", RoomPassesController.cardTypeText(CardType.CLIENT));
    }

    @Test
    @Tag("H4Y-156-AC-6")
    void onResultMessage_showNoEntries_whenListIsEmpty() {
        assertEquals("O quarto não tem passagens.", RoomPassesController.resultMessage(List.of()));
    }

    @Test
    @Tag("H4Y-156-AC-6")
    void onResultMessage_showCount_whenRoomHasEntries() {
        CardEntry entry = new CardEntry("12345", CardType.CLIENT, "Quarto 301", LocalDateTime.of(2026, 11, 24, 9, 5));

        assertEquals("1 passagem.", RoomPassesController.resultMessage(List.of(entry)));
        assertEquals("2 passagens.", RoomPassesController.resultMessage(List.of(entry, entry)));
    }
}
