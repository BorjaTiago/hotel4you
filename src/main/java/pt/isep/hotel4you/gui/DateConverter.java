package pt.isep.hotel4you.gui;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.ResolverStyle;
import javafx.util.StringConverter;

/**
 * Shows and reads the dates of the date pickers as day/month/year (dd/mm/aaaa),
 * not in the format of the computer.
 */
public class DateConverter extends StringConverter<LocalDate> {

    private static final DateTimeFormatter FORMAT =
            DateTimeFormatter.ofPattern("dd/MM/uuuu").withResolverStyle(ResolverStyle.STRICT);

    /**
     * Gives the date as text.
     *
     * @param date the date, or null
     * @return the date as dd/mm/aaaa, or an empty text if the date is null
     */
    @Override
    public String toString(LocalDate date) {
        return date == null ? "" : FORMAT.format(date);
    }

    /**
     * Reads a date typed as dd/mm/aaaa.
     *
     * @param text the typed text, or null
     * @return the date, or null if the text is empty
     * @throws java.time.format.DateTimeParseException if the text is not a real date in this format
     */
    @Override
    public LocalDate fromString(String text) {
        return text == null || text.isBlank() ? null : LocalDate.parse(text.trim(), FORMAT);
    }
}
