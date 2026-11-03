package pt.isep.hotel4you.domain;

/** A stock item of the hotel, for example towels, with its unit and quantity. */
public class StockItem {

    private final Long id;
    private final String name;
    private final String unit;
    private final int stockQuantity;

    /**
     * Creates a stock item.
     *
     * @param id the identifier, or null if the item is not saved yet
     * @param name the item name
     * @param unit the unit of measure, for example "caixa"
     * @param stockQuantity the quantity in stock
     */
    public StockItem(Long id, String name, String unit, int stockQuantity) {
        this.id = id;
        this.name = name;
        this.unit = unit;
        this.stockQuantity = stockQuantity;
    }

    /**
     * Returns the identifier.
     *
     * @return the identifier, or null if the item is not saved yet
     */
    public Long getId() {
        return id;
    }

    /**
     * Returns the item name.
     *
     * @return the item name
     */
    public String getName() {
        return name;
    }

    /**
     * Returns the unit of measure.
     *
     * @return the unit of measure
     */
    public String getUnit() {
        return unit;
    }

    /**
     * Returns the quantity in stock.
     *
     * @return the quantity in stock
     */
    public int getStockQuantity() {
        return stockQuantity;
    }
}
