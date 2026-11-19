package pt.isep.hotel4you.domain;

import java.math.BigDecimal;

/** A room extra or paid service with a fixed extra price per night. */
public class ExtraService {
    private final Long id;
    private final String name;
    private final ExtraType type;
    private final BigDecimal price;

    /**
     * Creates a catalogue entry.
     * @param id the saved identifier, or null before saving
     * @param name the catalogue name
     * @param type the kind of extra cost
     * @param price the price per night, or null before the manager sets it
     */
    public ExtraService(Long id, String name, ExtraType type, BigDecimal price) {
        this.id = id;
        this.name = name;
        this.type = type;
        this.price = price;
    }

    /**
     * Returns the catalogue identifier.
     * @return the identifier, or null before saving
     */
    public Long getId() {
        return id;
    }

    /**
     * Returns the catalogue name.
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * Returns the kind of extra cost.
     * @return the extra type
     */
    public ExtraType getType() {
        return type;
    }

    /**
     * Returns the fixed extra price per night.
     * @return the price, or null if not set
     */
    public BigDecimal getPrice() {
        return price;
    }
}
