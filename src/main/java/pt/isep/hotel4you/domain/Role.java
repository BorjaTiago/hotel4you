package pt.isep.hotel4you.domain;

/** Role of a person in the hotel system. */
public enum Role {
    /** A hotel guest. */
    CLIENT("Cliente"),
    /** A front desk employee. */
    RECEPTIONIST("Rececionista"),
    /** A hotel manager. */
    MANAGER("Gestor"),
    /** A restaurant employee. */
    RESTAURANT("Funcionário do restaurante"),
    /** A supplier of goods. */
    SUPPLIER("Fornecedor");

    private final String displayName;

    Role(String displayName) {
        this.displayName = displayName;
    }

    /**
     * Returns the name of the role to show on the screen.
     *
     * @return the name in Portuguese
     */
    public String displayName() {
        return displayName;
    }
}
