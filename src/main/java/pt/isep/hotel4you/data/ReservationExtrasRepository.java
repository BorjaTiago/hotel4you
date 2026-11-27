package pt.isep.hotel4you.data;

import java.util.List;
import pt.isep.hotel4you.domain.ExtraService;
import pt.isep.hotel4you.domain.ExtraType;
import pt.isep.hotel4you.domain.ReservationExtra;

/** Reads and stores the extras of a reservation with their saved prices. */
public interface ReservationExtrasRepository {
    /**
     * Reads the included extras of one kind.
     * @param reservationId the reservation identifier
     * @param type the catalogue group
     * @return the extras in catalogue order
     */
    List<ReservationExtra> findByReservation(long reservationId, ExtraType type);

    /**
     * Adds an extra while the reservation is still reserved. Replaces the other view in the
     * same transaction when it is given. A repeat add keeps the price already saved.
     * @param reservationId the reservation identifier
     * @param extra the priced catalogue entry
     * @param replacedExtraId the other view, or null
     * @return false if the reservation cannot be edited any more, true otherwise
     */
    boolean add(long reservationId, ExtraService extra, Long replacedExtraId);

    /**
     * Removes the extra while the reservation is still reserved.
     * @param reservationId the reservation identifier
     * @param extraPriceId the catalogue identifier
     * @param type the catalogue group
     * @return false if the reservation cannot be edited any more, true otherwise
     */
    boolean remove(long reservationId, long extraPriceId, ExtraType type);
}
