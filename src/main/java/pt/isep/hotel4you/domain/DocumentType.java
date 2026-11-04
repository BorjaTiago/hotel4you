package pt.isep.hotel4you.domain;

import java.util.Locale;

/** The ID document types of a client, stored in SQL Server. */
public enum DocumentType {
    /** Portuguese citizen card. */
    CC("Cartão de Cidadão"),
    /** Passport. */
    PASSPORT("Passaporte"),
    /** Any other official ID document, for example a residence permit. */
    OTHER("Outro");

    private final String displayName;

    DocumentType(String displayName) {
        this.displayName = displayName;
    }

    /**
     * Returns the name that the screen shows.
     *
     * @return the Portuguese name of the type
     */
    public String displayName() {
        return displayName;
    }

    /**
     * Reads the stored type.
     *
     * @param value the database value
     * @return the matching type
     * @throws IllegalArgumentException if the value is not a known type
     */
    public static DocumentType fromDatabase(String value) {
        return valueOf(value.toUpperCase(Locale.ROOT));
    }
}
