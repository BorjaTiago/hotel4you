package pt.isep.hotel4you.gui;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDate;
import java.time.LocalDateTime;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import pt.isep.hotel4you.domain.Manager;
import pt.isep.hotel4you.domain.MovementType;
import pt.isep.hotel4you.domain.StockItem;
import pt.isep.hotel4you.domain.StockMovement;

/** Tests the texts of the stock entry form. */
class StockControllerTest {

    @Test
    @Tag("H4Y-90-AC-1")
    void onEntryMessage_showNewStock_whenEntryIsSaved() {
        StockItem towels = new StockItem(1L, "toalhas", "unidade", 15);
        StockMovement movement = new StockMovement(towels, MovementType.ENTRY, 5,
                LocalDateTime.of(2026, 11, 16, 10, 30),
                new Manager(1L, "Rui Costa", "rui", "hash"));

        assertEquals("Entrada registada: 5 unidade de toalhas. Stock atual: 15.",
                StockController.entryMessage(movement));
    }

    @Test
    @Tag("H4Y-90-AC-1")
    void onDateText_showDayMonthYear_whenDateGiven() {
        assertEquals("16/11/2026", StockController.dateText(LocalDate.of(2026, 11, 16)));
    }
}
