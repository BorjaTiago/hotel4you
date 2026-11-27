package pt.isep.hotel4you.service;

import java.util.List;
import pt.isep.hotel4you.domain.ExtraService;
import pt.isep.hotel4you.domain.ExtraType;
import pt.isep.hotel4you.domain.ReservationExtra;
import pt.isep.hotel4you.domain.User;

/** Manages paid services included in a reservation before check-in. */
public class ReservationServicesService {
    private final ReservationExtrasService extras;

    /**
     * Creates the service with the common rules for reservation extras.
     * @param extras the shared reservation extras operations
     */
    public ReservationServicesService(ReservationExtrasService extras) {
        this.extras = extras;
    }

    /**
     * Lists the paid service options.
     * @param actor the logged-in user
     * @return the paid services in the catalogue
     */
    public List<ExtraService> listCatalogue(User actor) {
        return extras.listCatalogue(ExtraType.PAID_SERVICE, actor);
    }

    /**
     * Lists the paid services already included with their prices per night.
     * @param reservationId the reservation identifier
     * @param actor the logged-in user
     * @return the included paid services
     */
    public List<ReservationExtra> listServices(long reservationId, User actor) {
        return extras.listIncluded(reservationId, ExtraType.PAID_SERVICE, actor);
    }

    /**
     * Adds a paid service at its current price per night.
     * @param reservationId the reservation identifier
     * @param extraPriceId the catalogue identifier
     * @param actor the logged-in user
     */
    public void addService(long reservationId, long extraPriceId, User actor) {
        extras.add(reservationId, extraPriceId, ExtraType.PAID_SERVICE, actor);
    }

    /**
     * Removes a paid service before check-in.
     * @param reservationId the reservation identifier
     * @param extraPriceId the catalogue identifier
     * @param actor the logged-in user
     */
    public void removeService(long reservationId, long extraPriceId, User actor) {
        extras.remove(reservationId, extraPriceId, ExtraType.PAID_SERVICE, actor);
    }
}
