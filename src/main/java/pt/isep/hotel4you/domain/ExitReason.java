package pt.isep.hotel4you.domain;

/** The reason of a stock exit (Q-34). Only a {@link MovementType#EXIT} has a reason. */
public enum ExitReason {

    /** The items are thrown away. */
    DISCARD,

    /** The items go back to the supplier. */
    RETURN,

    /** The items are lost. */
    LOSS
}
