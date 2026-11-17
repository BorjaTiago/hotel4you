package pt.isep.hotel4you.domain;

import java.time.LocalDateTime;

/** One change of the stock of an item, with its quantity, date and the user who recorded it. */
public class StockMovement {

    private final StockItem item;
    private final MovementType type;
    private final int quantity;
    private final LocalDateTime dateTime;
    private final User user;

    /**
     * Creates a stock movement.
     *
     * @param item the item that moved
     * @param type the kind of movement
     * @param quantity the quantity moved, always positive
     * @param dateTime the date and time of the movement
     * @param user the user who recorded the movement
     */
    public StockMovement(StockItem item, MovementType type, int quantity, LocalDateTime dateTime,
            User user) {
        this.item = item;
        this.type = type;
        this.quantity = quantity;
        this.dateTime = dateTime;
        this.user = user;
    }

    /**
     * Returns the item that moved.
     *
     * @return the item
     */
    public StockItem getItem() {
        return item;
    }

    /**
     * Returns the kind of movement.
     *
     * @return the movement type
     */
    public MovementType getType() {
        return type;
    }

    /**
     * Returns the quantity moved.
     *
     * @return the quantity, always positive
     */
    public int getQuantity() {
        return quantity;
    }

    /**
     * Returns the date and time of the movement.
     *
     * @return the date and time
     */
    public LocalDateTime getDateTime() {
        return dateTime;
    }

    /**
     * Returns the user who recorded the movement.
     *
     * @return the user
     */
    public User getUser() {
        return user;
    }
}
