package pt.isep.hotel4you.domain;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.math.BigDecimal;
import java.time.LocalDate;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

/** Tests the total price of a room type for a period. */
class RoomTypeTest {
    private static final LocalDate START = LocalDate.of(2026, 11, 10);
    private static final LocalDate END = LocalDate.of(2026, 11, 12);

    @Test
    @Tag("H4Y-54-AC-1")
    void onTotalFor_returnPriceTimesNights_whenPeriodIsValid() {
        RoomType type = new RoomType(1L, "Double", 2, new BigDecimal("80.50"));

        BigDecimal total = type.totalFor(START, END);

        assertEquals(new BigDecimal("161.00"), total);
    }

    @Test
    @Tag("H4Y-54-AC-1")
    void onTotalFor_returnPrice_whenPeriodIsOneNight() {
        RoomType type = new RoomType(1L, "Double", 2, new BigDecimal("80.50"));

        BigDecimal total = type.totalFor(START, START.plusDays(1));

        assertEquals(new BigDecimal("80.50"), total);
    }

    @Test
    @Tag("H4Y-54-AC-1")
    void onTotalFor_countNights_whenPeriodCrossesNewYear() {
        RoomType type = new RoomType(1L, "Double", 2, new BigDecimal("100.00"));

        BigDecimal total = type.totalFor(LocalDate.of(2026, 12, 30), LocalDate.of(2027, 1, 2));

        assertEquals(new BigDecimal("300.00"), total);
    }

    @Test
    @Tag("H4Y-54-AC-1")
    void onTotalFor_countLeapDay_whenPeriodCrossesFebruary() {
        RoomType type = new RoomType(1L, "Double", 2, new BigDecimal("50.00"));

        BigDecimal total = type.totalFor(LocalDate.of(2028, 2, 27), LocalDate.of(2028, 3, 1));

        assertEquals(new BigDecimal("150.00"), total);
    }

    @Test
    @Tag("H4Y-54-AC-1")
    void onTotalFor_returnNull_whenBasePriceIsNull() {
        RoomType type = new RoomType(1L, "Double", 2, null);

        assertNull(type.totalFor(START, END));
    }

    @Test
    @Tag("H4Y-54-AC-4")
    void onTotalFor_throw_whenEndEqualsStart() {
        RoomType type = new RoomType(1L, "Double", 2, new BigDecimal("80.50"));

        IllegalArgumentException error = assertThrows(IllegalArgumentException.class,
                () -> type.totalFor(START, START));
        assertEquals("Período inválido", error.getMessage());
    }

    @Test
    @Tag("H4Y-54-AC-4")
    void onTotalFor_throw_whenEndIsBeforeStart() {
        RoomType type = new RoomType(1L, "Double", 2, new BigDecimal("80.50"));

        IllegalArgumentException error = assertThrows(IllegalArgumentException.class,
                () -> type.totalFor(END, START));
        assertEquals("Período inválido", error.getMessage());
    }

    @Test
    @Tag("H4Y-54-AC-4")
    void onTotalFor_throw_whenStartIsNull() {
        RoomType type = new RoomType(1L, "Double", 2, new BigDecimal("80.50"));

        IllegalArgumentException error = assertThrows(IllegalArgumentException.class,
                () -> type.totalFor(null, END));
        assertEquals("Período inválido", error.getMessage());
    }

    @Test
    @Tag("H4Y-54-AC-4")
    void onTotalFor_throw_whenEndIsNull() {
        RoomType type = new RoomType(1L, "Double", 2, new BigDecimal("80.50"));

        IllegalArgumentException error = assertThrows(IllegalArgumentException.class,
                () -> type.totalFor(START, null));
        assertEquals("Período inválido", error.getMessage());
    }
}
