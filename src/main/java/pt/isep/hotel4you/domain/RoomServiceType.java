package pt.isep.hotel4you.domain;

/** The three room service types that reception registers (RF-15, Q-33). */
public enum RoomServiceType {
    /** Food to the room. */
    FOOD("Comida", "Comida"),
    /** Drinks to the room. */
    DRINK("Bebida", "Bebidas"),
    /** Laundry of the guest clothes. */
    LAUNDRY("Lavandaria", "Lavandaria");

    private final String displayName;
    private final String catalogueName;

    RoomServiceType(String displayName, String catalogueName) {
        this.displayName = displayName;
        this.catalogueName = catalogueName;
    }

    /**
     * Returns the name to show on the screen.
     *
     * @return the name in Portuguese
     */
    public String displayName() {
        return displayName;
    }

    /**
     * Returns the name of the matching row in the table {@code extra_prices}.
     *
     * @return the catalogue name
     */
    public String catalogueName() {
        return catalogueName;
    }
}
