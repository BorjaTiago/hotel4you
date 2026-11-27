package pt.isep.hotel4you.data;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import pt.isep.hotel4you.domain.ExtraService;
import pt.isep.hotel4you.domain.ExtraType;
import pt.isep.hotel4you.domain.ReservationExtra;
import pt.isep.hotel4you.domain.ReservationStatus;

/** Stores reservation extras in memory for service tests. */
public class InMemoryReservationExtrasRepository implements ReservationExtrasRepository {
    private final ReservationRepository reservations;
    private final Map<Long, List<ExtraService>> entries = new HashMap<>();
    public boolean checkInBeforeNextEdit;

    public InMemoryReservationExtrasRepository(ReservationRepository reservations) {
        this.reservations = reservations;
    }

    @Override
    public List<ReservationExtra> findByReservation(long reservationId, ExtraType type) {
        return entries.getOrDefault(reservationId, List.of()).stream()
                .filter(extra -> extra.getType() == type)
                .sorted(Comparator.comparing(ExtraService::getId))
                .map(extra -> new ReservationExtra(extra.getId(), extra.getName(), extra.getPrice()))
                .toList();
    }

    @Override
    public boolean add(long reservationId, ExtraService extra, Long replacedExtraId) {
        if (checkInBeforeNextEdit) {
            checkInBeforeNextEdit = false;
            reservations.checkIn(reservationId);
        }
        if (!isReserved(reservationId)) {
            return false;
        }
        List<ExtraService> selected = entries.computeIfAbsent(reservationId, id -> new ArrayList<>());
        selected.removeIf(value -> value.getId().equals(replacedExtraId));
        if (selected.stream().noneMatch(value -> value.getId().equals(extra.getId()))) {
            selected.add(extra);
        }
        return true;
    }

    @Override
    public boolean remove(long reservationId, long extraPriceId, ExtraType type) {
        if (!isReserved(reservationId)) {
            return false;
        }
        entries.computeIfAbsent(reservationId, id -> new ArrayList<>())
                .removeIf(value -> value.getType() == type && value.getId() == extraPriceId);
        return true;
    }

    private boolean isReserved(long id) {
        return reservations.findById(id).filter(row -> row.getStatus() == ReservationStatus.RESERVED).isPresent();
    }
}
