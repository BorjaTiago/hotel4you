package pt.isep.hotel4you.domain;

import java.util.Locale;

/** The kind of a client feedback (RF-27). */
public enum FeedbackType {

    /** An idea of the client to improve the hotel. */
    SUGGESTION("Sugestão"),

    /** A problem that the client reports. */
    COMPLAINT("Reclamação");

    private final String displayName;

    FeedbackType(String displayName) {
        this.displayName = displayName;
    }

    /**
     * Returns the name that the screen shows.
     *
     * @return the Portuguese name, for example "Sugestão"
     */
    public String displayName() {
        return displayName;
    }

    /**
     * Returns the value of the column {@code feedback.type}.
     *
     * @return "suggestion" or "complaint"
     */
    public String databaseValue() {
        return name().toLowerCase(Locale.ROOT);
    }

    /**
     * Returns the type of a value of the column {@code feedback.type}.
     *
     * @param value "suggestion" or "complaint"
     * @return the feedback type
     * @throws IllegalArgumentException if the value is not known
     */
    public static FeedbackType fromDatabase(String value) {
        return valueOf(value.toUpperCase(Locale.ROOT));
    }
}
