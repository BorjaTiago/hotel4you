package pt.isep.hotel4you.gui;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.time.LocalDate;
import org.junit.jupiter.api.Test;

/** Tests the day/month/year format of the date pickers. */
class DateConverterTest {
    private final DateConverter converter = new DateConverter();

    @Test
    void onToString_showDayMonthYear_whenDateIsSet() {
        assertEquals("08/11/2026", converter.toString(LocalDate.of(2026, 11, 8)));
    }

    @Test
    void onToString_returnEmpty_whenDateIsNull() {
        assertEquals("", converter.toString(null));
    }

    @Test
    void onFromString_readDayMonthYear_whenTextIsSet() {
        assertEquals(LocalDate.of(2026, 11, 8), converter.fromString(" 08/11/2026 "));
    }

    @Test
    void onFromString_returnNull_whenTextIsBlank() {
        assertNull(converter.fromString(" "));
        assertNull(converter.fromString(null));
    }
}
