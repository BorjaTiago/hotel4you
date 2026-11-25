package pt.isep.hotel4you.gui;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import pt.isep.hotel4you.domain.CardEntry;
import pt.isep.hotel4you.domain.CardType;

/** Tests the texts of the screen with the places of a card (H4Y-155). */
class CardPlacesControllerTest {

    @Test
    @Tag("H4Y-155-AC-1")
    void onTimeText_showDateAndHour_whenEntryIsListed() {
        assertEquals("24/11/2026 09:05", CardPlacesController.timeText(LocalDateTime.of(2026, 11, 24, 9, 5)));
    }

    @Test
    @Tag("H4Y-155-AC-5")
    void onResultMessage_showNoEntries_whenListIsEmpty() {
        assertEquals("O cartão não tem passagens.", CardPlacesController.resultMessage(List.of()));
    }

    @Test
    @Tag("H4Y-155-AC-5")
    void onResultMessage_showCount_whenCardHasEntries() {
        CardEntry entry = new CardEntry("12345", CardType.CLIENT, "Quarto 202", LocalDateTime.of(2026, 11, 24, 9, 5));

        assertEquals("1 passagem.", CardPlacesController.resultMessage(List.of(entry)));
        assertEquals("2 passagens.", CardPlacesController.resultMessage(List.of(entry, entry)));
    }
}
