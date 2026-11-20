package pt.isep.hotel4you.data;

import java.util.List;
import java.util.Optional;
import pt.isep.hotel4you.domain.StockItem;
import pt.isep.hotel4you.domain.StockMovement;

/** Stores and finds the stock items of the hotel. */
public interface ItemRepository {

    /**
     * Finds an item by id.
     *
     * @param id the item id
     * @return the item, or empty if it does not exist
     * @throws IllegalStateException if the database is not available
     */
    Optional<StockItem> findById(long id);

    /**
     * Finds an item by name. The match is not case sensitive,
     * because the database uses the CI_AS collation.
     *
     * @param name the item name
     * @return the item, or empty if no item has this name
     * @throws IllegalStateException if the database is not available
     */
    Optional<StockItem> findByName(String name);

    /**
     * Lists all items.
     *
     * @return the items ordered by name
     * @throws IllegalStateException if the database is not available
     */
    List<StockItem> findAll();

    /**
     * Saves a new item.
     *
     * @param item the new item, its id is ignored
     * @return the saved item with its new id
     * @throws DuplicateItemException if another item has this name
     * @throws IllegalStateException if the database is not available
     */
    StockItem save(StockItem item);

    /**
     * Changes the name and the unit of a saved item. The quantity stays the same.
     *
     * @param item the item with its id and new values
     * @return true if the item was changed, false if no item has this id
     * @throws DuplicateItemException if another item has this name
     * @throws IllegalStateException if the database is not available
     */
    boolean update(StockItem item);

    /**
     * Saves a stock movement and adds its quantity to the stock of the item.
     * Both changes are saved together, or none is saved.
     * Only types that raise the stock are allowed now ({@link pt.isep.hotel4you.domain.MovementType#ENTRY}).
     * A type that lowers the stock needs a change here first.
     *
     * @param movement the movement, with the saved item, the type, the quantity, the date and the user
     * @return true if the movement was saved, false if no item has the id of the movement item
     * @throws IllegalStateException if the database is not available
     */
    boolean addMovement(StockMovement movement);
}
