package pt.isep.hotel4you.service;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;
import pt.isep.hotel4you.data.DuplicateItemException;
import pt.isep.hotel4you.data.ItemRepository;
import pt.isep.hotel4you.data.NotEnoughStockException;
import pt.isep.hotel4you.domain.ExitReason;
import pt.isep.hotel4you.domain.MovementType;
import pt.isep.hotel4you.domain.Permission;
import pt.isep.hotel4you.domain.StockItem;
import pt.isep.hotel4you.domain.StockMovement;
import pt.isep.hotel4you.domain.User;

/**
 * Creates, edits and lists the stock items and records stock entries, uses and exits.
 * Only a manager may use it.
 */
public class StockService {

    private static final int MAX_NAME_LENGTH = 100;
    private static final int MAX_UNIT_LENGTH = 20;
    /** The largest quantity of one movement. The column {@code stock_movements.quantity} is a SMALLINT. */
    private static final int MAX_MOVEMENT_QUANTITY = 32767;
    private static final String NOT_ENOUGH_STOCK = "A quantidade é maior que o stock do item.";

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
     * An entry adds the quantity to the stock of the item. A use or an exit subtracts it.
     *
     * <p>The service reads the saved item first, so the stock checks use the stock of the
     * database, not the stock of the given item. If another user lowers the stock at the same
     * time, the database refuses a stock below zero and the user sees the same message.
     * If the item is deleted after the save and before the read, the movement stays saved and
     * the user sees "O item não existe.".
     *
     * @param item the chosen item
     * @param type the kind of movement
     * @param quantity the quantity, from 1 to {@value #MAX_MOVEMENT_QUANTITY}
     * @param exitReason the reason of an exit; it must be null for an entry or a use
     * @param user the logged-in user who records the movement
     * @return the saved movement, with the item and its stock read after the save
     * @throws AccessDeniedException if the user is not a manager
     * @throws IllegalArgumentException if no item or type is chosen, an exit has no reason,
     *         an entry or a use has a reason, the quantity is 0 or less or above the maximum,
     *         an entry makes the stock go above the maximum, or a use or an exit is larger
     *         than the stock of the item
     * @throws StockItemNotFoundException if the item does not exist
     * @throws IllegalStateException if the database is not available
     */
    public StockMovement registerMovement(StockItem item, MovementType type, int quantity,
            ExitReason exitReason, User user) {
        checkManage(user);
        if (item == null) {
            throw new IllegalArgumentException("Escolha um item.");
        }
        if (type == null) {
            throw new IllegalArgumentException("Escolha o tipo de movimento.");
        }
        checkExitReason(type, exitReason);
        if (quantity < 1) {
            throw new IllegalArgumentException("A quantidade tem de ser maior que zero.");
        }
        if (quantity > MAX_MOVEMENT_QUANTITY) {
            throw new IllegalArgumentException("A quantidade máxima é " + MAX_MOVEMENT_QUANTITY + ".");
        }
        if (item.getId() == null) {
            throw new StockItemNotFoundException();
        }
        StockItem stored = itemRepository.findById(item.getId())
                .orElseThrow(StockItemNotFoundException::new);
        checkStockLimits(stored, type, quantity);
        LocalDateTime now = LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS);
        boolean saved;
        try {
            saved = itemRepository.addMovement(
                    new StockMovement(stored, type, quantity, exitReason, now, user));
        } catch (NotEnoughStockException e) {
            throw new IllegalArgumentException(NOT_ENOUGH_STOCK, e);
        }
        if (!saved) {
            throw new StockItemNotFoundException();
        }
        StockItem changed = itemRepository.findById(item.getId())
                .orElseThrow(StockItemNotFoundException::new);
        return new StockMovement(changed, type, quantity, exitReason, now, user);
    }

    /** Only an exit has a reason, and an exit must have one (Q-34). */
    private static void checkExitReason(MovementType type, ExitReason exitReason) {
        if (type == MovementType.EXIT && exitReason == null) {
            throw new IllegalArgumentException("Escolha o motivo da saída.");
        }
        if (type != MovementType.EXIT && exitReason != null) {
            throw new IllegalArgumentException("Só a saída tem motivo.");
        }
    }

    /** An entry keeps the stock in the INT column. A use or an exit keeps the stock at 0 or more. */
    private static void checkStockLimits(StockItem stored, MovementType type, int quantity) {
        if (type == MovementType.ENTRY) {
            if ((long) stored.getStockQuantity() + quantity > Integer.MAX_VALUE) {
                throw new IllegalArgumentException("O stock do item ficaria acima do máximo.");
            }
        } else if (quantity > stored.getStockQuantity()) {
            throw new IllegalArgumentException(NOT_ENOUGH_STOCK);
        }
    }

    /**
     * Turns the text of the screen into a whole number. The screen has no number rule (RNF-07).
     *
     * @param text the text, for example "5" or "-3"
     * @return the number; {@link #registerMovement} checks the range
     * @throws IllegalArgumentException if the text is not a whole number
     */
    public int parseQuantity(String text) {
        try {
            return Integer.parseInt(text.trim());
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("A quantidade tem de ser um número inteiro.");
        }
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
