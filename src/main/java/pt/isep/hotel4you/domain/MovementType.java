package pt.isep.hotel4you.domain;

/**
 * The kind of a stock movement. It says if the movement adds to the stock or takes from it.
 */
public enum MovementType {

    /** Items come into the stock. The stock goes up. */
    ENTRY,

    /** Items are used in the hotel. The stock goes down. */
    USE,

    /** Items leave the stock by discard, return or loss. The stock goes down. */
    EXIT
}
