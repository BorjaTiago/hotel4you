package pt.isep.hotel4you.domain;

import java.util.EnumSet;
import java.util.Set;

/** A function of the application and the roles that may use it. */
public enum Permission {
    /** Create and list staff accounts. */
    MANAGE_STAFF(Role.MANAGER),

    /** View the floors and create, edit and remove the zones. */
    MANAGE_FLOORS(Role.MANAGER);

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
