package pt.isep.hotel4you.gui;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDate;
import java.time.LocalDateTime;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import pt.isep.hotel4you.domain.ExitReason;
import pt.isep.hotel4you.domain.Manager;
import pt.isep.hotel4you.domain.MovementType;
import pt.isep.hotel4you.domain.StockItem;
import pt.isep.hotel4you.domain.StockMovement;

/** Tests the texts of the stock movement form. */
class StockControllerTest {

    private static StockMovement movement(StockItem item, MovementType type, int quantity,
            ExitReason reason) {
        return new StockMovement(item, type, quantity, reason,
                LocalDateTime.of(2026, 11, 16, 10, 30),
                new Manager(1L, "Rui Costa", "rui", "hash"));
    }

    @Test
    @Tag("H4Y-90-AC-1")
    void onMovementMessage_showNewStock_whenEntryIsSaved() {
        StockItem towels = new StockItem(1L, "toalhas", "unidade", 15);

        assertEquals("Entrada registada: 5 unidade de toalhas. Stock atual: 15.",
                StockController.movementMessage(movement(towels, MovementType.ENTRY, 5, null)));
    }

    @Test
    @Tag("H4Y-106-AC-1")
    void onMovementMessage_showReasonAndNewStock_whenExitIsSaved() {
        StockItem sheets = new StockItem(1L, "lençóis", "unidade", 14);

        assertEquals("Saída registada (descarte): 6 unidade de lençóis. Stock atual: 14.",
                StockController.movementMessage(
                        movement(sheets, MovementType.EXIT, 6, ExitReason.DISCARD)));
    }

    @Test
    @Tag("H4Y-106-AC-4")
    void onMovementMessage_showNewStock_whenUseIsSaved() {
        StockItem towels = new StockItem(2L, "toalhas", "unidade", 11);

        assertEquals("Uso registado: 4 unidade de toalhas. Stock atual: 11.",
                StockController.movementMessage(movement(towels, MovementType.USE, 4, null)));
    }

    @ParameterizedTest
    @CsvSource({"ENTRY, Entrada", "USE, Uso", "EXIT, Saída"})
    @Tag("H4Y-106-AC-1")
    void onTypeText_showPortugueseName_whenTypeGiven(MovementType type, String expected) {
        assertEquals(expected, StockController.typeText(type));
    }

    @ParameterizedTest
    @CsvSource({"DISCARD, Descarte", "RETURN, Devolução", "LOSS, Perda"})
    @Tag("H4Y-106-AC-3")
    void onReasonText_showPortugueseName_whenReasonGiven(ExitReason reason, String expected) {
        assertEquals(expected, StockController.reasonText(reason));
    }

    @Test
    @Tag("H4Y-90-AC-1")
    void onDateText_showDayMonthYear_whenDateGiven() {
        assertEquals("16/11/2026", StockController.dateText(LocalDate.of(2026, 11, 16)));
    }
}
