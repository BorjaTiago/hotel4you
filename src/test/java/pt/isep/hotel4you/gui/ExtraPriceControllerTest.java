package pt.isep.hotel4you.gui;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.math.BigDecimal;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

/** Tests display formatting that supports the extra price table in AC-3. */
class ExtraPriceControllerTest {
    @ParameterizedTest
    @CsvSource(value = {"null|Por definir", "15.00|15,00", "0.00|0,00", "40.00|40,00"},
            delimiter = '|', nullValues = "null")
    @Tag("H4Y-93-AC-3")
    void onPriceText_formatAmount_whenTableShowsPrice(String value, String expected) {
        assertEquals(expected, ExtraPriceController.priceText(value == null ? null : new BigDecimal(value)));
    }
}
