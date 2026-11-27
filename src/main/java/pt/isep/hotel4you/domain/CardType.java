package pt.isep.hotel4you.domain;

/** The two kinds of magnetic card. */
public enum CardType {
    /** A card of a guest or of a staff member. It opens only what its owner may enter. */
    CLIENT,
    /** The manager card. It opens every room and every zone. */
    MASTER
}
