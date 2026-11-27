package pt.isep.hotel4you.service;

import java.util.List;
import pt.isep.hotel4you.data.ExtraPriceRepository;
import pt.isep.hotel4you.data.ReservationExtrasRepository;
import pt.isep.hotel4you.data.ReservationRepository;
import pt.isep.hotel4you.domain.ExtraService;
import pt.isep.hotel4you.domain.ExtraType;
import pt.isep.hotel4you.domain.Permission;
import pt.isep.hotel4you.domain.Reservation;
import pt.isep.hotel4you.domain.ReservationExtra;
import pt.isep.hotel4you.domain.ReservationStatus;
import pt.isep.hotel4you.domain.User;

/** Applies the rules for room extras on a reservation. */
public class ReservationExtrasService {
    private static final String SEA_VIEW = "Vista mar";
    private static final String MOUNTAIN_VIEW = "Vista montanha";
    private static final String STATE_CHANGED = "A reserva mudou de estado. Não é possível alterar os extras.";

    private final ReservationRepository reservations;
    private final ExtraPriceRepository catalogue;
    private final ReservationExtrasRepository extras;

    /**
     * Creates the service with the existing reservation and price catalogue stores.
     * @param reservations the reservation store
     * @param catalogue the price catalogue
     * @param extras the store for extras and their saved prices
     */
    public ReservationExtrasService(ReservationRepository reservations, ExtraPriceRepository catalogue,
            ReservationExtrasRepository extras) {
        this.reservations = reservations;
        this.catalogue = catalogue;
        this.extras = extras;
    }

    /**
     * Lists the options for a room, including entries without a price.
     * @param actor the logged-in user
     * @return the room extras available in the catalogue
     */
    public List<ExtraService> listCatalogue(User actor) {
        return listCatalogue(ExtraType.ROOM_EXTRA, actor);
    }

    /**
     * Reads the room extras of the reservation with the saved price per night.
     * @param reservationId the reservation identifier
     * @param actor the logged-in user
     * @return the room extras included in this reservation
     */
    public List<ReservationExtra> listExtras(long reservationId, User actor) {
        return listIncluded(reservationId, ExtraType.ROOM_EXTRA, actor);
    }

    /**
     * Adds a priced room extra before check-in. A different view replaces the old view.
     * @param reservationId the reservation identifier
     * @param extraPriceId the catalogue identifier
     * @param actor the logged-in user
     */
    public void addExtra(long reservationId, long extraPriceId, User actor) {
        add(reservationId, extraPriceId, ExtraType.ROOM_EXTRA, actor);
    }

    /**
     * Removes a room extra before check-in.
     * @param reservationId the reservation identifier
     * @param extraPriceId the catalogue identifier
     * @param actor the logged-in user
     */
    public void removeExtra(long reservationId, long extraPriceId, User actor) {
        remove(reservationId, extraPriceId, ExtraType.ROOM_EXTRA, actor);
    }

    List<ExtraService> listCatalogue(ExtraType type, User actor) {
        checkPermission(actor);
        return catalogue.findAll().stream().filter(extra -> extra.getType() == type).toList();
    }

    List<ReservationExtra> listIncluded(long reservationId, ExtraType type, User actor) {
        findReservation(reservationId, actor);
        return extras.findByReservation(reservationId, type);
    }

    void add(long reservationId, long extraPriceId, ExtraType type, User actor) {
        checkEditable(reservationId, actor);
        List<ExtraService> entries = listCatalogue(type, actor);
        ExtraService extra = findEntry(entries, extraPriceId);
        if (extra.getPrice() == null) {
            throw new IllegalArgumentException("Defina o preço de " + extra.getName()
                    + " antes de o adicionar à reserva.");
        }

        Long replacedId = type == ExtraType.ROOM_EXTRA ? otherViewId(entries, extra.getName()) : null;
        if (!extras.add(reservationId, extra, replacedId)) {
            throw new IllegalStateException(STATE_CHANGED);
        }
    }

    void remove(long reservationId, long extraPriceId, ExtraType type, User actor) {
        checkEditable(reservationId, actor);
        findEntry(listCatalogue(type, actor), extraPriceId);
        if (!extras.remove(reservationId, extraPriceId, type)) {
            throw new IllegalStateException(STATE_CHANGED);
        }
    }

    private static ExtraService findEntry(List<ExtraService> entries, long id) {
        return entries.stream().filter(extra -> extra.getId() == id).findFirst()
                .orElseThrow(() -> new IllegalArgumentException("O extra escolhido não existe neste catálogo."));
    }

    private static Long otherViewId(List<ExtraService> entries, String name) {
        String other = switch (name) {
            case SEA_VIEW -> MOUNTAIN_VIEW;
            case MOUNTAIN_VIEW -> SEA_VIEW;
            default -> null;
        };
        return entries.stream().filter(extra -> extra.getName().equals(other))
                .map(ExtraService::getId).findFirst().orElse(null);
    }

    private void checkEditable(long reservationId, User actor) {
        if (findReservation(reservationId, actor).getStatus() != ReservationStatus.RESERVED) {
            throw new IllegalStateException("Só pode alterar extras de reservas ativas antes do check-in.");
        }
    }

    private Reservation findReservation(long reservationId, User actor) {
        checkPermission(actor);
        return reservations.findById(reservationId).orElseThrow(ReservationNotFoundException::new);
    }

    private static void checkPermission(User actor) {
        if (!Permission.MANAGE_RESERVATIONS.allows(actor)) {
            throw new AccessDeniedException("Não tem permissão para gerir reservas.");
        }
    }
}
