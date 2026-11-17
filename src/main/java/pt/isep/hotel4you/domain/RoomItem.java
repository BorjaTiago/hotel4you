package pt.isep.hotel4you.domain;

/** A stock item placed in a room, with the quantity that the room holds. */
public class RoomItem {

    private final StockItem item;
    private final int quantity;

    /**
     * Creates a room item.
     *
     * @param item the stock item
     * @param quantity the quantity in the room
     */
    public RoomItem(StockItem item, int quantity) {
        this.item = item;
        this.quantity = quantity;
    }

    /**
     * Returns the stock item.
     *
     * @return the stock item
     */
    public StockItem getItem() {
        return item;
    }

    /**
     * Returns the quantity in the room.
     *
     * @return the quantity in the room
     */
    public int getQuantity() {
        return quantity;
    }
}
