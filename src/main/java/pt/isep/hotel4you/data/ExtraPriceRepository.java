package pt.isep.hotel4you.data;

import java.math.BigDecimal;
import java.util.List;
import pt.isep.hotel4you.domain.ExtraService;

/** Reads the extra catalogue and updates its prices per night. */
public interface ExtraPriceRepository {
    /**
     * Finds all catalogue entries, including those without a price.
     * @return the entries ordered by identifier, or an empty list
     * @throws IllegalStateException if the database is not available
     */
    List<ExtraService> findAll();

    /**
     * Updates the price of an existing entry without changing its name or type.
     * @param id the catalogue identifier
     * @param price the validated price per night
     * @return true if the entry was updated, false if the id does not exist
     * @throws IllegalStateException if the database is not available
     */
    boolean updatePrice(long id, BigDecimal price);
}
