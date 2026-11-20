package pt.isep.hotel4you.domain;

/**
 * The kind of a stock movement. It says if the movement adds to the stock or takes from it.
 * Only {@link #ENTRY} exists now. The use and exit stories add the types that lower the stock.
 */
public enum MovementType {

    /** Items come into the stock. The stock goes up. */
    ENTRY
}
