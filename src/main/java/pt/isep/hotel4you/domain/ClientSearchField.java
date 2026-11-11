package pt.isep.hotel4you.domain;

/** The client data that the receptionist can search by. */
public enum ClientSearchField {
    /** The ID document number. */
    DOCUMENT("Documento"),
    /** The client name. */
    NAME("Nome"),
    /** The phone number. */
    PHONE("Telefone");

    private final String displayName;

    ClientSearchField(String displayName) {
        this.displayName = displayName;
    }

    /**
     * Returns the name that the screen shows.
     *
     * @return the Portuguese name of the field
     */
    public String displayName() {
        return displayName;
    }
}
