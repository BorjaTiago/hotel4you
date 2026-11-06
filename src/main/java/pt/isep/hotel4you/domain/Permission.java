package pt.isep.hotel4you.domain;

import java.util.EnumSet;
import java.util.Set;

/** A function of the application and the roles that may use it. */
public enum Permission {
    /** Create and list staff accounts. */
    MANAGE_STAFF(Role.MANAGER),

    /** View the floors and create, edit and remove the zones. */
    MANAGE_FLOORS(Role.MANAGER),

    /** Register and list rooms. */
    MANAGE_ROOMS(Role.MANAGER),

    /** View and set the base price of the room types. */
    MANAGE_PRICES(Role.MANAGER),

    /** Create, edit and list the stock items. */
    MANAGE_STOCK(Role.MANAGER),

    /** See the room list with type, floor and base price. */
    LIST_ROOMS(Role.RECEPTIONIST, Role.MANAGER),

    /**
     * Register, list, search, edit and remove clients. Clients do not log in (Q-13), so the
     * receptionist or the manager does it (Q-54).
     */
    MANAGE_CLIENTS(Role.RECEPTIONIST, Role.MANAGER),

    /** Search free rooms by type and dates. */
    SEARCH_ROOMS(Role.RECEPTIONIST, Role.MANAGER),

    /** Create reservations and list the reservations of a client. */
    MANAGE_RESERVATIONS(Role.RECEPTIONIST, Role.MANAGER);

    private final Set<Role> allowedRoles;

    Permission(Role first, Role... others) {
        this.allowedRoles = EnumSet.of(first, others);
    }

    /**
     * Tells if the user may use this function.
     *
     * @param actor the logged-in user, can be null
     * @return true if the actor is logged in and has one of the allowed roles
     */
    public boolean allows(User actor) {
        return actor != null && allowedRoles.contains(actor.getRole());
    }
}
