package pt.isep.hotel4you.domain;

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
        return name().toLowerCase();
    }
}
