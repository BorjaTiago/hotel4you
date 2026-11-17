package pt.isep.hotel4you.service;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;
import pt.isep.hotel4you.data.DuplicateItemException;
import pt.isep.hotel4you.data.ItemRepository;
import pt.isep.hotel4you.domain.MovementType;
import pt.isep.hotel4you.domain.Permission;
import pt.isep.hotel4you.domain.StockItem;
import pt.isep.hotel4you.domain.StockMovement;
import pt.isep.hotel4you.domain.User;

/** Creates, edits and lists the stock items and records stock entries. Only a manager may use it. */
public class StockService {

    private static final int MAX_NAME_LENGTH = 100;
    private static final int MAX_UNIT_LENGTH = 20;
    /** The largest quantity of one movement. The column {@code stock_movements.quantity} is a SMALLINT. */
    private static final int MAX_MOVEMENT_QUANTITY = 32767;
    private static final String QUANTITY_TEXT = "[+-]?\\d{1,9}";

    private final ItemRepository itemRepository;

    /**
     * Creates the service.
     *
     * @param itemRepository the item store
     */
    public StockService(ItemRepository itemRepository) {
        this.itemRepository = itemRepository;
    }

    /**
     * Creates a stock item with zero stock.
     *
     * @param name the item name, the service trims it
     * @param unit the unit of measure, the service trims it
     * @param actor the logged-in user who asks
     * @return the new item with its id
     * @throws AccessDeniedException if the actor is not a manager
     * @throws IllegalArgumentException if the name is blank or longer than 100 characters,
     *         or the unit is blank or longer than 20 characters
     * @throws StockItemNameTakenException if another item has this name,
     *         also when the database finds the same name at save time
     * @throws IllegalStateException if the database is not available
     */
    public StockItem createItem(String name, String unit, User actor) {
        checkManage(actor);
        String cleanName = checkText(name, "nome", MAX_NAME_LENGTH);
        String cleanUnit = checkText(unit, "unidade", MAX_UNIT_LENGTH);
        checkNameFree(cleanName, null);
        try {
            return itemRepository.save(new StockItem(null, cleanName, cleanUnit, 0));
        } catch (DuplicateItemException e) {
            throw new StockItemNameTakenException();
        }
    }

    /**
     * Changes the name and the unit of an item. The stock quantity does not change.
     *
     * @param item the item with its id and the new name and unit
     * @param actor the logged-in user who asks
     * @throws AccessDeniedException if the actor is not a manager
     * @throws IllegalArgumentException if the name is blank or longer than 100 characters,
     *         or the unit is blank or longer than 20 characters
     * @throws StockItemNotFoundException if the item does not exist
     * @throws StockItemNameTakenException if another item has this name,
     *         also when the database finds the same name at save time
     * @throws IllegalStateException if the database is not available
     */
    public void editItem(StockItem item, User actor) {
        checkManage(actor);
        String cleanName = checkText(item.getName(), "nome", MAX_NAME_LENGTH);
        String cleanUnit = checkText(item.getUnit(), "unidade", MAX_UNIT_LENGTH);
        if (item.getId() == null) {
            throw new StockItemNotFoundException();
        }
        StockItem stored = itemRepository.findById(item.getId()).orElseThrow(StockItemNotFoundException::new);
        checkNameFree(cleanName, stored.getId());
        StockItem changed = new StockItem(stored.getId(), cleanName, cleanUnit, stored.getStockQuantity());
        boolean updated;
        try {
            updated = itemRepository.update(changed);
        } catch (DuplicateItemException e) {
            throw new StockItemNameTakenException();
        }
        if (!updated) {
            throw new StockItemNotFoundException();
        }
    }

    /**
     * Lists all stock items.
     *
     * @param actor the logged-in user who asks
     * @return the items ordered by name
     * @throws AccessDeniedException if the actor is not a manager
     * @throws IllegalStateException if the database is not available
     */
    public List<StockItem> listItems(User actor) {
        checkManage(actor);
        return itemRepository.findAll();
    }

    /**
     * Records a stock movement of an item with the date and time of now.
     * An entry adds the quantity to the stock of the item.
     *
     * @param item the chosen item, the service reads it again by its id
     * @param type the kind of movement
     * @param quantity the quantity, from 1 to 32767
     * @param user the logged-in user who records the movement
     * @return the saved movement, with the item and its new stock
     * @throws AccessDeniedException if the user is not a manager
     * @throws IllegalArgumentException if no item or type is chosen,
     *         or the quantity is 0, negative or above 32767
     * @throws StockItemNotFoundException if the item does not exist
     * @throws IllegalStateException if the database is not available
     */
    public StockMovement registerMovement(StockItem item, MovementType type, int quantity, User user) {
        checkManage(user);
        if (item == null) {
            throw new IllegalArgumentException("Escolha um item.");
        }
        if (type == null) {
            throw new IllegalArgumentException("Escolha o tipo de movimento.");
        }
        if (quantity <= 0) {
            throw new IllegalArgumentException("A quantidade tem de ser maior que zero.");
        }
        if (quantity > MAX_MOVEMENT_QUANTITY) {
            throw new IllegalArgumentException("A quantidade máxima é " + MAX_MOVEMENT_QUANTITY + ".");
        }
        if (item.getId() == null) {
            throw new StockItemNotFoundException();
        }
        StockItem stored = itemRepository.findById(item.getId()).orElseThrow(StockItemNotFoundException::new);
        LocalDateTime now = LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS);
        if (!itemRepository.addMovement(new StockMovement(stored, type, quantity, now, user))) {
            throw new StockItemNotFoundException();
        }
        StockItem changed = new StockItem(stored.getId(), stored.getName(), stored.getUnit(),
                stored.getStockQuantity() + quantity);
        return new StockMovement(changed, type, quantity, now, user);
    }

    /**
     * Turns the text of the screen into a whole number. The screen has no number rule (RNF-07).
     *
     * @param text the text, for example "5" or "-3"
     * @return the number; {@link #registerMovement} checks that it is above zero
     * @throws IllegalArgumentException if the text is not a whole number
     */
    public int parseQuantity(String text) {
        if (text == null || !text.trim().matches(QUANTITY_TEXT)) {
            throw new IllegalArgumentException("A quantidade tem de ser um número inteiro.");
        }
        return Integer.parseInt(text.trim());
    }

    private void checkManage(User actor) {
        if (!Permission.MANAGE_STOCK.allows(actor)) {
            throw new AccessDeniedException("Só o gestor pode gerir o stock.");
        }
    }

    private static String checkText(String text, String field, int maxLength) {
        if (text == null || text.isBlank()) {
            throw new IllegalArgumentException("Campo em falta: " + field + ".");
        }
        String clean = text.trim();
        if (clean.length() > maxLength) {
            throw new IllegalArgumentException("Campo " + field + ": máximo de " + maxLength + " caracteres.");
        }
        return clean;
    }

    /** Throws if another item (not the one with {@code ownId}) has this name. */
    private void checkNameFree(String name, Long ownId) {
        Optional<StockItem> same = itemRepository.findByName(name);
        if (same.isPresent() && !same.get().getId().equals(ownId)) {
            throw new StockItemNameTakenException();
        }
    }
}
